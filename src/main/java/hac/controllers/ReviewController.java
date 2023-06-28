package hac.controllers;

import hac.repo.Game;
import hac.repo.GameRepository;
import hac.repo.Review;
import hac.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;

    @Autowired
    public ReviewController(ReviewRepository reviewRepository, GameRepository gameRepository) {
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
    }

    // Implement your review-related methods here
//    @GetMapping("/all")
//    @ResponseBody
//    public List<Review> getAllReviews() {
//        return reviewRepository.findAll();
//    }

    /**
     *  returns all reviews on a game by a given game id
     * @param gameId - unique key for the gameRepository
     * @param model - inserting attributes to the page
     * @return game's reviews if exists,  error page else
     */
    @GetMapping("/user/game-reviews")
    public String getGameReviews(@RequestParam(value = "gameId") Long gameId, Model model) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            List<Review> reviews = game.getReviews();
            model.addAttribute("game", game);
            model.addAttribute("reviews", reviews);
            return "user/gamereviews";
        }
        model.addAttribute("errorMessage","Game could not be found");
        return "errorpage";
    }

    /**
     *  Returns a review form if game exists
     * @param gameId game's id
     * @param model to add attributes
     * @return error page if inputs are invalid, addReview page with the game's attribs else
     */
    @GetMapping("/user/addreview")
    public String getReviewForm(@RequestParam(value = "gameId") Long gameId, Model model) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if(gameOptional.isPresent()){
            model.addAttribute("gameId", gameId);
            model.addAttribute("title", gameOptional.map(Game::getTitle).orElse(""));
            return "user/addreview";
        }
        model.addAttribute("errorMessage","Game could not be found");
        return "errorpage";
    }

    /**
     * @return Review added successfully page
     */
    @GetMapping("/user/review-added")
    public String showReviewAddedPage() {
        return "user/addedreview";
    }

    /**
     * Method to add a new review based on given gameId and a given review object
     * @param review A review object
     * @param result result from validating the review
     * @param gameId an optional entry for gameRepo
     * @param model to add attributes
     * @return Success page on success, error page if review/game id is invalid
     */
    @PostMapping("/user/addreview")
    public String postReview(@Valid Review review,
                             BindingResult result,
                             @RequestParam(value="gameId") Long gameId,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "errorpage";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            boolean hasReviewed = game.getReviews().stream()
                    .anyMatch(rev -> rev.getUsername().equals(username));
            System.out.println(hasReviewed);
            if (!hasReviewed) {
                game.addReview(review);
                review.setTime(LocalDateTime.now());
                reviewRepository.save(review);
                gameRepository.save(game);
                return "redirect:/reviews/user/review-added";
            } else {
                model.addAttribute("errorMessage","You have already reviewed this game.");
                return "errorpage";
            }
        }
        return "redirect:/reviews/user/review-added";
    }

    /**
     * method to return all users reviews
     * @param model to add attributes
     * @return All reviews added by the authenticated user (by his username)
     */
    @GetMapping("/user/all")
    public String getUserReviews(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        List<Review> userReviews = reviewRepository.findAllByUsername(username);
        model.addAttribute("userReviews", userReviews);

        return "user/reviews";
    }

    /**
     * Get method to return an edit review page
     * @param id - potential entry for game repo
     * @param model to add attributes
     * @return editreview page with a review object or null as "review" attrib
     */
    @GetMapping("/user/edit/{id}")
    public String editReview(@PathVariable("id") Long id, Model model) {
        // Retrieve the review by ID and add it to the model
        Optional<Review> review = reviewRepository.findById(id);
        model.addAttribute("review", review.orElse(null));
        return "user/editreview";
    }

    /**
     *  A post method for editing an existing review
     * @param review - review obj
     * @param result result from validating the review's field
     * @param model to add attributes
     * @return error message on failure or all of the user's reviews
     */
    @PostMapping("/user/edit")
    public String editReview(@Valid Review review,
                             BindingResult result, Model model) {
        // Retrieve the existing review from the database
        if(result.hasErrors()){
            model.addAttribute("errors", result.getAllErrors());
            return "errorpage";
        }
        Optional<Review> existingReviewOptional = reviewRepository.findById(review.getId());
        if (existingReviewOptional.isPresent()) {
            Review existingReview = existingReviewOptional.get();
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());
            existingReview.setTime(LocalDateTime.now());
            existingReview.getGame().calculateAverageScore();
            reviewRepository.save(existingReview);

            return "redirect:/reviews/user/all";
        }

        model.addAttribute("message", "Review not found");
        return "errorpage";
    }

    /**
     * A post method for deleting a user's review by a given review id
     * @param id potential entry in review repo
     * @param model to add attributes
     * @return error page if entry not found, all users reviews if successfully deleted
     */
    @PostMapping("/user/delete/{id}")
    public String deleteReview(@PathVariable Long id, Model model) {
        // Delete the review by ID
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if(optionalReview.isPresent()){
            Review review = optionalReview.get();
            reviewRepository.delete(review);
            return "redirect:/reviews/user/all";
        }
        model.addAttribute("message", "An error");
        return "/user/errorpage";
    }

    /**
     *  Returns a special admin page with all reviews allows him to take action on those reviews
     * @param model to add all reviews
     * @return manage reviews page for the admin which contains all reviews in the review repo
     */
    @GetMapping("/admin/management")
    public String getReviewsControlPanel(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "/admin/managereviews";
    }

    /**
     * Post method to delete a review by a given review id
     * @param reviewId potential key
     * @return management page in both cases of existing and non-existing key
     */
    @PostMapping("/admin/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            reviewRepository.delete(review);
            return "redirect:/reviews/admin/management";
        }
        // Handle the case when the review is not found
        return "redirect:/reviews/admin/management";
    }
    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }
}
