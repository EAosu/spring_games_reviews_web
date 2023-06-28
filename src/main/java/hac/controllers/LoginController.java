package hac.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LoginController {

    /**
     * @return - login page
     */
    @GetMapping(value = "/login")
    public String login() {

        return "login";
    }

    /**
     * Not used we don't handle errors by throwing exceptions but might occur in an unexpected behavior?
     */
    @RequestMapping("/errorpage")
    public String error(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorpage";
    }

    /** simple Error page. */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    /** We don't manually throw exceptions so this will not be directly invoked by use  */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {

        //logger.error("Exception during execution of SpringSecurity application", ex);
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");

        model.addAttribute("errorMessage", errorMessage);
        return "errorpage";
    }

}