package hac.controllers;

import hac.repo.Game;
import hac.repo.GameRepository;
import hac.repo.Review;
import hac.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-review")
    public String addReview(@RequestParam("gameId") Long gameId,
                            @RequestParam("rating") int rating,
                            @RequestParam("comment") String comment) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Review review = new Review(gameId, rating, comment, LocalDateTime.now());
            reviewRepository.save(review);
        }
        return "redirect:/reviews/all";
    }
}
