package hac.controllers;

import hac.repo.LastLogin;
import hac.repo.LastLoginRepository;
import hac.repo.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class Default {

    @Autowired
    private UserSessionBean sessionBean;

    @Autowired
    private LastLoginRepository lastLoginRepository;

    /**
     * Handles the index page request.
     *
     * @param model the model object for the view
     * @return the name of the view template ("Login")
     */
    @GetMapping("/")
    public String index(Model model) {
        boolean isLoggedIn = false;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "Login";
    }

    /**
     * Handles the register page request.
     *
     * @param model the model object for the view
     * @return the name of the view template ("register")
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("validated", false);
        model.addAttribute("password", null);
        model.addAttribute("confirmPassword", null);
        return "register";
    }

    /**
     * Handles the forgot password page request.
     *
     * @param model the model object for the view
     * @return the name of the view template ("forgot-password")
     */
    @GetMapping("/forgot-password")
    public String forgot(Model model) {
        return "forgot-password";
    }

    /**
     * Handles the Home page request.
     *
     * @param model the model object for the view
     * @return the name of the view template ("Home") if the user is logged in,
     *         otherwise redirects to the login page
     */
    @GetMapping("/Home")
    public String Home(Model model) {
        String email = sessionBean.getEmail();
        String FirstName = sessionBean.getFirstName();
        String LastName = sessionBean.getLastName();
        String Job = sessionBean.getJob();
        if (email != null && !email.equals("guest")) { // Check if the user is logged in
            LastLogin lastLogin = lastLoginRepository.findByEmail(email);
            LocalDateTime lastLoginDateTime = lastLogin.getLastLoginDateTime();
            model.addAttribute("lastLoginDateTime", lastLoginDateTime);
            model.addAttribute("email", email);
            model.addAttribute("FirstName", FirstName);
            model.addAttribute("LastName", LastName);
            model.addAttribute("Job", Job);
            model.addAttribute("isLoggedIn", true);
            return "Home";
        } else {
            return "redirect:/"; // Redirect to the login page
        }
    }

    /**
     * Handles the not found page request.
     *
     * @return the name of the view template ("notFound")
     */
    @GetMapping("*")
    public String notFound() {
        return "notFound";
    }
}
