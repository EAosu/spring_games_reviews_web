package hac.controllers;

import hac.repo.Game;
import hac.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;

    @Autowired
    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Implement your game-related methods here
    @GetMapping("/all")
    @ResponseBody
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @PostMapping("/search")
    public String getSearchResults(@RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "genre", required = false) String genre,
                                   @RequestParam(value = "multiplayer", required = false) Boolean multiplayer,
                                   @RequestParam(value = "singleplayer", required = false) Boolean singleplayer,
                                   Model model) {

        List<Game> searchResults;

        if (title != null && genre != null && multiplayer != null && singleplayer != null) {
            // All fields provided
            searchResults = gameRepository.findByTitleContainingIgnoreCaseAndGenreIgnoreCaseAndMultiplayerAndSingleplayer(
                    title, genre, multiplayer, singleplayer);
        } else if (title != null) {
            // Only title provided
            searchResults = gameRepository.findByTitleContainingIgnoreCase(title);
        } else {
            // No search criteria provided, retrieve all games
            searchResults = gameRepository.findAll();
        }

        model.addAttribute("searchResults", searchResults);

        return "search";
    }

    @GetMapping("/search")
    public String getSearchForm() {
        return "search";
    }

    @PostMapping("/add-game")
    public String postGame(@RequestParam("title") String title,
                          @RequestParam("genre") String genre,
                          @RequestParam(value = "multiplayer", defaultValue = "false") Boolean multiplayer,
                          @RequestParam(value = "singleplayer", defaultValue = "false") Boolean singleplayer) {
        Game game = new Game(title, genre, multiplayer, singleplayer);
        gameRepository.save(game);
        return "redirect:/games/search";
    }

    @GetMapping("/add-game")
    public String getGameForm() {
        return "addgame";
    }
}
