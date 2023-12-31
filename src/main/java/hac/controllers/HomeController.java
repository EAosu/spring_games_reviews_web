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

    /**
     * Returns the homepage with all reviews in descending order. and the top 10 games by their avg score
     * @param model  to add attribs
     * @return the homepage
     */
    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Game> topGames = gameController.getGameRepo().findTop10GamesOrderByAverageReviewScore();
        List<Review> latestReviews = reviewController.getReviewRepository().findAllByOrderByTimeDesc();

        model.addAttribute("topGames", topGames);
        model.addAttribute("latestReviews", latestReviews);

        return "homepage";
    }
}
