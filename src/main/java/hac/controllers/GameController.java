package hac.controllers;

import hac.repo.Game;
import hac.repo.GameRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     Retrieves search results based on the provided game information and populates the model with the results.
     At least one of the multiplayer or singleplayer options must be selected.
     @param game the Game object containing search criteria
     @param model the model object to add attributes
     @return the view name for displaying the search results or an error page if the selection is invalid
     */
    @PostMapping("/user/search")
    public String getSearchResults(Game game, Model model) {
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


    /**
     * @return a search form
     */
    @GetMapping("/user/search")
    public String getSearchForm() {
        return "user/search";
    }

    /**
     * Method to add a new game to gameRepository.
     * @param game game object
     * @param result validation results of the game
     * @param model to add attributes
     * @param redirectAttributes to add a Flash Attribute which disappears overtime
     * @return home page
     */
    @PostMapping("/add-game")
    public String postGame(@Valid Game game, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if(!isValidInput(game,result,model))    return "errorpage";
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("message","Game was added successfully.");
        return "redirect:/";
    }

    /**
     * @return An form page to add a game
     */
    @GetMapping("/add-game")
    public String getGameForm() {return "addgame";}
    public GameRepository getGameRepo() {return gameRepository;}

    /**
     * @param model to add attributes
     * @return a manage game page which contains all existing games
     */
    @GetMapping("/admin/management")
    public String getGamesControlPanel(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "admin/managegames";
    }

    /**
     *
     * @param id A potential game id
     * @param model to add attrib
     * @return error page if game not found. edit game form else
     */
    @GetMapping("/admin/edit/{id}")
    public String editGame(@PathVariable("id") Long id, Model model){
        Optional<Game> game = gameRepository.findById(id);
        if (game.isEmpty()){
            model.addAttribute("errorMessage", "Invalid game id");
            return "errorpage";
        }
        model.addAttribute("game", game.orElse(null));
        return "admin/editgame";
    }

    /**
     *  Method to edit existing game by given game object
     * @param game - should be the same game as before so it's gameId will also be the entry
     * @param result validation result of the game object
     * @param model to add attribs
     * @return an admin game control centre in both cases
     */
    @PostMapping("/admin/edit")
    public String editGame(@Valid Game game,BindingResult result, Model model) {
        if(!isValidInput(game,result,model))    return "errorpage";
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

    /**
     * Method to delete an existing game
     * @param id Potential entry
     * @param model to add attribs
     * @return Homepage if game deleted, error page otherwise
     */
    @PostMapping("/admin/delete/{id}")
    public String deleteGame(@PathVariable Long id, Model model) {
        // Find the game by ID
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            // Delete the game
            gameRepository.delete(game);

            return "redirect:/games/admin/management";
        }

        model.addAttribute("message", "An error");
        return "/user/errorpage";
    }

    /**
     * A private function to validate a games input
     * @param game  Game obj
     * @param result existing results of validating this game
     * @param model to add attribs
     * @return Boolean. true if the game is valid
     */
    private Boolean isValidInput(Game game,BindingResult result, Model model){
        Boolean isValid = true;
        if (!game.isValidSelection()) {
            result.rejectValue("multiplayer", "game.selection.invalid",
                    "Please select Multiplayer or Singleplayer");
            isValid=false;
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            isValid=false;
        }
        return isValid;
    }


}
