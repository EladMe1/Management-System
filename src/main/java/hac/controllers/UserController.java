package hac.controllers;

import hac.repo.*;
import hac.repo.UserSessionBean;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;




@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserSessionBean sessionBean;

    @Autowired
    private LastLoginRepository lastLoginRepository;


    /**
     * Registers a new user.
     *
     * @param UserData the user data containing the user details
     * @return a ResponseEntity with the registration status
     */
    @PostMapping("/register")
    public ResponseEntity<String> RegisterUser(@RequestBody Map<String, Object> UserData) {
        // Access the data sent from the front end
        String firstName = (String) UserData.get("firstname");
        String lastName = (String) UserData.get("lastname");
        String email = (String) UserData.get("email");
        String Job = (String) UserData.get("job");
        String Password = (String) UserData.get("password");

        // Check if the email is already taken
        User existingUser = repository.findByEmail(email);
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Email already taken");
        }


        // Create a Purchase object from the extracted data
        User usr = new User(firstName, lastName, email, Job, Password,true);

        try {
            // Save the user to the database
            repository.save(usr);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }

    /**
     * Logs in a user.
     *
     * @param userData the user data containing the login credentials
     * @return a ResponseEntity with the login status and user information
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> userData) {
        String email = (String) userData.get("email");
        String password = (String) userData.get("password");

        User user = repository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid email or password"));
        }

        // Verify if the user is enabled
        if (!user.getEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Your account is disabled"));
        }

        // Verify the entered password with the stored password
        if (BCrypt.checkpw(password, user.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("isAdmin", email.equalsIgnoreCase("admin@example.com")); // Add isAdmin field

            // Save the email using the EmailSessionBean
            sessionBean.connect(email, sessionBean.getFirstName(), sessionBean.getLastName(), sessionBean.getJob());
            sessionBean.setEmail(email);
            sessionBean.setFirstName(user.getFirstName());
            sessionBean.setLastName(user.getLastName());
            sessionBean.setJob(user.getJob());

            // Check if the user has logged in before
            LastLogin lastLogin = lastLoginRepository.findByEmail(email);
            if (lastLogin == null) {
                // User is logging in for the first time, save the current login as last login
                lastLogin = new LastLogin();
                lastLogin.setUser(email);
                lastLogin.setLastLoginDateTime(LocalDateTime.now());
            } else {
                // User has logged in before, update the last login time
                lastLogin.setLastLoginDateTime(LocalDateTime.now());
            }
            lastLoginRepository.save(lastLogin);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid email or password"));
        }
    }


    /**
     * Resets the user's password.
     *
     * @param userData the user data containing the user details
     * @return a ResponseEntity with the password reset status and the new password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, Object> userData) {
        String firstName = (String) userData.get("firstName");
        String lastName = (String) userData.get("lastName");
        String email = (String) userData.get("email");
        String job = (String) userData.get("job");

        User user = repository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
        }

        if (!user.getFirstName().equalsIgnoreCase(firstName) || !user.getLastName().equalsIgnoreCase(lastName)
                || !user.getJob().equalsIgnoreCase(job)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "User data does not match"));
        }

        // Generate a random password
        String newPassword = generateRandomPassword();

        // Encrypt the new password
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Update the user's password in the database
        user.setPassword(hashedPassword);
        repository.save(user);

        // Create the response with the new password
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Password reset successful");
        response.put("newPassword", newPassword);
        return ResponseEntity.ok(response);
    }

    /**
     * Generates a random password.
     *
     * @return the generated random password
     */
    private String generateRandomPassword() {
        // Implement your logic to generate a random password
        // For example, you can use a library or write your own code to generate a random string
        // Here's a simple example to generate a random password with 8 characters:
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    // handling the home page:
    /**
     * Logs out the user.
     *
     * @return a ResponseEntity with the logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        sessionBean.logout();
        return ResponseEntity.ok("Logout successful");
    }

    /**
     * Updates the user's details.
     *
     * @param userData the user data containing the updated details
     * @return a ResponseEntity with the update status
     */
    @PostMapping("/update-details")
    public ResponseEntity<String> updateDetails(@RequestBody Map<String, String> userData) {
        String firstName = userData.get("firstName");
        String lastName = userData.get("lastName");
        String job = userData.get("job");

        // Retrieve the user from the database using sessionBean.getEmail()
        User user = repository.findByEmail(sessionBean.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Update the user details
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJob(job);

        try {
            // Save the updated user details to the database
            repository.save(user);
            return ResponseEntity.ok("Details updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating details");
        }
    }

    /**
     * Changes the user's password.
     *
     * @param passwordData the data containing the current and new passwords
     * @return a ResponseEntity with the password change status
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordData) {
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");

        // Retrieve the user from the database using sessionBean.getEmail()
        User user = repository.findByEmail(sessionBean.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Verify the current password
        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid current password");
        }

        // Encrypt the new password
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Update the user's password in the database
        user.setPassword(hashedPassword);

        try {
            // Save the updated password to the database
            repository.save(user);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
        }
    }

}