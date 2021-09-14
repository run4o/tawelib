package JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Interface controller for the main staff interface.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @author Martin Trifonov
 * @author Fraser Barrass (V2.0)
 * @version 2.0
 */
public class StaffInterfaceController extends SceneController implements Initializable {

    /**
     * Button to logout of the staff interface.
     */
    @FXML
    private Button logoutButton;
    /**
     * Label to display a welcome message to the user.
     */
    @FXML
    private Label usernameDisplay;

    /**
     * Handles the action of clicking the button to logout of the staff
     * interface or return to the staff interface from a subscene.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    protected void handleLogoutButtonAction(ActionEvent event) {
        //If button is in logout mode, logout. Otherwise go to the staff interface.
        if (logoutButton.getText().equals("Logout")) {
            handleSceneChangeButtonAction(event, SceneController.getMainInterface());
        } else {
            handleSceneChangeButtonAction(event, SceneController.getStaffInterface());
        }
    }

    /**
     * Handles the action of clicking the button to change to the resources
     * interface.
     */
    @FXML
    protected void handleResourcesButtonAction() {
        loadSubscene(SceneController.getResourceInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the button to change to the accounts
     * interface.
     */
    @FXML
    protected void handleAccountsButtonAction() {
        loadSubscene(SceneController.getAccountsSearchInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the button to change to the account
     * creator interface.
     */
    @FXML
    protected void handleAccountCreatorButtonAction() {
        loadSubscene(SceneController.getAccountCreatorInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Initialises the label in the interface to display a message to the user
     * based on the account currently logged in.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameDisplay.setText("Welcome, " + getAccountManager().getAccount(SceneController.USER_ID).getFirstName());
    }

    /**
     * Shows event management scene.
     */
    @FXML
    public void handleEvents() {
        loadSubscene(SceneController.getEventManagementInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * displays the requested copies.
     */
    @FXML
    public void handleCopiesAction() {
        loadSubscene(SceneController.getCopyLogInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles displaying the overdue copies.
     */
    @FXML
    public void handleAllOverdueCopies() {
        loadSubscene(SceneController.getListOverdueCopiesInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles displaying staff stats.
     */
    @FXML
    public void handleStaffStats() {
        loadSubscene(SceneController.getStaffStats());
        changeLogoutToHome(logoutButton);
    }
}
