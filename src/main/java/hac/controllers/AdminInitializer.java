package hac.controllers;
import hac.repo.User;
import hac.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Initializes the admin user if it does not exist in the repository.
 */
@Component
public class AdminInitializer {
    @Autowired
    private UserRepository repository;

    /**
     * Constructs an AdminInitializer with the specified UserRepository.
     *
     * @param repository the UserRepository used to access user data.
     */
    public AdminInitializer(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Listens to the ContextRefreshedEvent and adds the admin user if it does not exist.
     *
     * @param event the ContextRefreshedEvent triggered when the application context is refreshed.
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Check if the admin user already exists
        User existingAdmin = repository.findByEmail("admin@example.com");
        if (existingAdmin != null) {
            return; // Admin user already exists, no need to add again
        }

        // Create the admin user
        User adminUser = new User("Admin", "User", "admin@example.com", "Admin", "password",true);

        try {
            // Save the admin user to the database
            repository.save(adminUser);
            System.out.println("Admin user added successfully");
        } catch (Exception e) {
            System.out.println("Error adding admin user: " + e.getMessage());
        }
    }
}
