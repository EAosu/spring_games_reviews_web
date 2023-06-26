package hac.controllers;

import hac.repo.Game;
import hac.repo.GameRepository;
import hac.repo.Review;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;

    @Autowired
    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping("/user/search")
    public String getSearchResults(@ModelAttribute("game") Game game, Model model) {
        // Must provide at least one of multiplayer/singleplayer
        if (!game.isValidSelection()) {
            model.addAttribute("errorMessage", "Please select Multiplayer or Singleplayer");
            return "errorpage";
        }

        List<Game> searchResults;

        if (!game.getTitle().isEmpty() && game.getGenre() != null) {
            // Title and genre are provided
            searchResults = gameRepository.findByTitleGenreAndSingleplayerOrMultiplayer(
                    game.getTitle(), game.getGenre(), game.getSingleplayer() != null, game.getMultiplayer() != null);
        } else if (!game.getTitle().isEmpty()) {
            // No genre provided, title is provided
            searchResults = gameRepository.findByTitleAndSingleplayerOrMultiplayer(
                    game.getTitle(), game.getSingleplayer() != null, game.getMultiplayer() != null);
        } else if (game.getGenre() != null) {
            // No title provided, genre is provided
            searchResults = gameRepository.findByGenreAndSingleplayerOrMultiplayer(
                    game.getGenre(), game.getSingleplayer() != null, game.getMultiplayer() != null);
        } else {
            // No title and genre provided
            searchResults = gameRepository.findBySingleplayerOrMultiplayer(
                    game.getSingleplayer() != null, game.getMultiplayer() != null);
        }

        model.addAttribute("searchResults", searchResults);
        return "user/search";
    }


    @GetMapping("/user/search")
    public String getSearchForm() {
        return "user/search";
    }

    @PostMapping("/add-game")
    public String postGame(@Valid @ModelAttribute("game") Game game,
                           BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (!game.isValidSelection()) {
            result.rejectValue("multiplayer", "game.selection.invalid",
                    "Please select Multiplayer or Singleplayer");
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "errorpage";
        }
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("message","Game was added successfully.");
        return "redirect:/";
//        return "/user/search";
    }
    @GetMapping("/add-game")
    public String getGameForm() {return "addgame";}
    public GameRepository getGameRepo() {return gameRepository;}

    @GetMapping("/admin/management")
    public String getGamesControlPanel(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "admin/managegames";
    }
    @GetMapping("/admin/edit/{id}")
    public String editGame(@PathVariable("id") Long id, Model model){
        Optional<Game> game = gameRepository.findById(id);
        model.addAttribute("game", game.orElse(null));
        return "admin/editgame";
    }
    @PostMapping("/admin/edit")
    public String editGame(@ModelAttribute("game") Game game, Model model) {
        // Retrieve the existing review from the database
        Optional<Game> existingGameOptional = gameRepository.findById(game.getId());
        if (existingGameOptional.isPresent()) {
            Game existingGame = existingGameOptional.get();

            // Update the review with the new data-
            existingGame.setTitle(game.getTitle());
            existingGame.setGenre(game.getGenre());
            existingGame.setMultiplayer(game.getMultiplayer());
            existingGame.setSingleplayer(game.getSingleplayer());

            // Save the updated review in the database
            gameRepository.save(existingGame);

            return "redirect:/games/admin/management";
        }

        return "redirect:/games/admin/management";
    }
}
