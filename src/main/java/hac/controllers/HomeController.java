package hac.controllers;

import hac.repo.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final ReviewController reviewController;
    private final GameController gameController;

    public HomeController(ReviewController reviewController, GameController gameController) {
        this.reviewController = reviewController;
        this.gameController = gameController;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Game> topGames = gameController.getGameRepo().findTop10GamesOrderByAverageReviewScore();

        // Calculate average score for each game
        for (Game game : topGames) {
            Double averageScore = calculateAverageScore(game);
            game.setAverageScore(averageScore);
        }

        List<Review> allReviews = reviewController.getReviewRepository().findAllByOrderByTimeDesc();

        model.addAttribute("topGames", topGames);
        model.addAttribute("latestReviews", allReviews);

        return "homepage";
    }

    private Double calculateAverageScore(Game game) {
        List<Review> reviews = game.getReviews();
        if (reviews.isEmpty()) {
            return 0.0;
        }
        int totalScore = 0;
        for (Review review : reviews) {
            totalScore += review.getRating();
        }
        return (double) totalScore / reviews.size();
    }
}
