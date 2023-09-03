package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

/**
 *
 */
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "First name is mandatory")
    private String firstName;

    @NotEmpty(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Job is mandatory")
    private String Job;

    @NotEmpty(message = "Password is mandatory")
    private String Password;

    @NotNull(message = "enabled is mandatory")
    private boolean enabled;


    /**
     * Constructs a User object with the specified details.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param Job       the job of the user
     * @param Password  the password of the user
     */
    public User(String firstName, String lastName, String email, String Job, String Password, boolean enabled) {
        this.email = email;
        this.Job = Job;
        this.Password = BCrypt.hashpw(Password,BCrypt.gensalt());
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }

    public User() {

    }


    // getters and setters
    /**
     * Retrieves the ID of the user.
     *
     * @return the ID of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the job of the user.
     *
     * @return the job of the user
     */
    public String getJob() {
        return Job;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return Password;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id the ID of the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName=lastName;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email=email;
    }

    /**
     * Sets the job of the user.
     *
     * @param Job the job of the user
     */
    public void setJob(String Job) {
        this.Job=Job;
    }

    /**
     * Sets the password of the user.
     *
     * @param Password the password of the user
     */
    public void setPassword(String Password) {
        this.Password=Password;
    }

    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    public boolean getEnabled(){return enabled;}

}