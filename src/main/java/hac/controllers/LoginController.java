package hac.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String login() {

        //return login.html located in /resources/templates
        return "login";
    }
    @PostMapping(value="/login")
    public String postLogin(@RequestAttribute("username") String username, @RequestAttribute("password") String password) {
        System.out.println("Here!!!!!!!");
        return "redirect:/homepage";
    }

}