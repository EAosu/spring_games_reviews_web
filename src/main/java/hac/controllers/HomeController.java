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
    public String home(Model model) {
        List<Review> allReviews = reviewController.getAllReviews();
        List<Game> allGames = gameController.getAllGames();

        model.addAttribute("allReviews", allReviews);
        model.addAttribute("allGames", allGames);

        return "homepage";
    }
}
