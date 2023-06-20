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

    @GetMapping("/user/game-reviews")
    public String getGameReviews(@RequestParam(value = "gameId") Long gameId, Model model) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            List<Review> reviews = game.getReviews();
            model.addAttribute("game", game);
            model.addAttribute("reviews", reviews);
        }
        return "user/gamereviews";
    }

    @GetMapping("/user/add-review")
    public String getReviewForm(@RequestParam(value = "gameId") Long gameId, Model model) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        model.addAttribute("gameId", gameId);
        model.addAttribute("title", gameOptional.map(Game::getTitle).orElse(""));
        return "user/addreview";
    }

    @PostMapping("/user/add-review")
    public String postReview(@Valid @ModelAttribute("review") Review review,
                             @RequestParam(value="gameId") Long gameId,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Got errors");
            model.addAttribute("errors", result.getAllErrors());
            return "errorpage"; // Replace with your error page
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



    @GetMapping("/user/review-added")
    public String showReviewAddedPage() {
        return "user/addedreview";
    }
}
