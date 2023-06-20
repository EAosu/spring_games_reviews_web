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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @GetMapping("/all")
    @ResponseBody
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @PostMapping("/user/add-review")
    public String addReview(@RequestParam(value = "gameId") Long gameId,
                            @RequestParam(value = "rating") Integer rating,
                            @RequestParam("comment") String comment) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Review review = new Review(gameId, rating, comment, LocalDateTime.now(), username);
            reviewRepository.save(review);
        }
        return "addedreview";
    }
}
