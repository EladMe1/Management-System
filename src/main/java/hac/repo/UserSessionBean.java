package hac.repo;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

@Component
@SessionScope
public class UserSessionBean implements Serializable {

    private boolean connected;
    private String email;
    private String firstName;
    private String lastName;
    private String job;

    public UserSessionBean() {
        connected = false;
        email = "guest";
    }

    /**
     * Sets the email of the user.
     *
     * @param emailUser the email of the user
     */
    public void setEmail(String emailUser) {
        email = emailUser;
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
     * Sets the connected state of the user.
     *
     * @param state the connected state of the user
     */
    public void setConnected(boolean state) {
        connected = state;
    }

    /**
     * Retrieves the connected state of the user.
     *
     * @return the connected state of the user
     */
    public boolean getConnected() {
        return connected;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
     * Sets the last name of the user.
     *
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * Sets the job of the user.
     *
     * @param job the job of the user
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * Retrieves the job of the user.
     *
     * @return the job of the user
     */
    public String getJob() {
        return job;
    }

    /**
     * Connects the user with the specified details.
     *
     * @param emailUser  the email of the user
     * @param firstName  the first name of the user
     * @param lastName   the last name of the user
     * @param job        the job of the user
     */
    public void connect(String emailUser, String firstName, String lastName, String job) {
        setConnected(true);
        setEmail(emailUser);
        setFirstName(firstName);
        setLastName(lastName);
        setJob(job);
    }

    /**
     * Logs out the user by resetting the session data.
     */
    public void logout() {
        connected = false;
        email = "guest";
        firstName = null;
        lastName = null;
        job = null;
    }
}
