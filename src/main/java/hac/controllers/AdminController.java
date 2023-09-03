package hac.controllers;
import hac.repo.User;
import hac.repo.UserRepository;
import hac.repo.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing admin-related operations.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserRepository repository;

    @Autowired
    private UserSessionBean sessionBean;

    /**
     * Constructs an AdminController with the specified UserRepository.
     *
     * @param repository the UserRepository used to access user data.
     */
    @Autowired
    public AdminController(UserRepository repository) {
        this.repository = repository;
    }


    /**
     * Handles the GET request for the admin dashboard.
     *
     * @param model the Model object to hold data for the view.
     * @return the view name for the admin dashboard.
     */
    @GetMapping
    public String admin(Model model) {
        String loggedInEmail = sessionBean.getEmail();
        String adminEmail = "admin@example.com"; // Replace with the admin's email

        if (!loggedInEmail.equals(adminEmail)) {
            return "redirect:/";
        }

        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }


    /**
     * Handles the POST request to add a new user.
     *
     * @param email     the email of the user.
     * @param firstName the first name of the user.
     * @param lastName  the last name of the user.
     * @param job       the job of the user.
     * @param password  the password of the user.
     * @return the view name to redirect after adding the user.
     */
    @PostMapping("/add")
    public String addUser(@RequestParam("email") String email,
                          @RequestParam("firstName") String firstName,
                          @RequestParam("lastName") String lastName,
                          @RequestParam("job") String job,
                          @RequestParam("password") String password) {

        // Check if the email already exists in the database
        User existingUser = repository.findByEmail(email);
        if (existingUser != null) {
            // Email already taken
            return "redirect:/admin";
        }

        // Email is not taken, proceed with adding the new user
        User user = new User(firstName, lastName, email, job, password, true);
        repository.save(user);
        return "redirect:/admin";
    }


    /**
     * Handles the GET request to edit a user.
     *
     * @param id    the ID of the user to edit.
     * @param model the Model object to hold data for the view.
     * @return the view name for editing the user.
     */
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            return "edit-user";
        } else {
            return "redirect:/admin";
        }
    }

    /**
     * Handles the POST request to update a user.
     *
     * @param id        the ID of the user to update.
     * @param firstName the updated first name of the user.
     * @param lastName  the updated last name of the user.
     * @param job       the updated job of the user.
     * @return the view name to redirect after updating the user.
     */
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("job") String job) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setJob(job);
            repository.save(user);
        }
        return "redirect:/admin";
    }

    /**
     * Handles the GET request to delete a user.
     *
     * @param id the ID of the user to delete.
     * @return the view name to redirect after deleting the user.
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/admin";
    }

    /**
     * Handles the GET request to log out the admin user.
     *
     * @return the view name to redirect after logging out.
     */
    @GetMapping("/logout")
    public String logout() {
        sessionBean.logout();
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("query") String query, Model model) {
        String loggedInEmail = sessionBean.getEmail();
        String adminEmail = "admin@example.com"; // Replace with the admin's email

        if (!loggedInEmail.equals(adminEmail)) {
            return "redirect:/";
        }

        List<User> users = repository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query, query);
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/enable/{id}")
    public String enableUser(@PathVariable("id") Long id) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(true);
            repository.save(user);
        }
        return "redirect:/admin";
    }

    @GetMapping("/disable/{id}")
    public String disableUser(@PathVariable("id") Long id) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(false);
            repository.save(user);
        }
        return "redirect:/admin";
    }


}
