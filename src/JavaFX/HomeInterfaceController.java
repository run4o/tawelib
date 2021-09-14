package JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import Core.AccountManager;

/**
 * Interface controller for the main user interface home scene.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @author Rory Richards (V2.0) , Martin Trifonov
 * @author Fraser Barrass (V2.0)
 * @version 2.0
 */
public class HomeInterfaceController extends SceneController implements Initializable {

    /**
     * Button to log out of the system.
     */
    @FXML
    private Button logoutButton;
    /**
     * Label to display a greeting to the user.
     */
    @FXML
    private Label usernameDisplay;
    /**
     * avatar image displayed on the dashboard button.
     */
    @FXML
    private ImageView avatarImage;
    /**
     * label to display the current balance of the user.
     */
    @FXML
    private Label currentBalance;

    /**
     * Handles the action of clicking the button to open the user dashboard
     * interface.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    protected void handleDashboardButtonAction(ActionEvent event) {
        handleSceneChangeButtonAction(event, SceneController.getUserDashboardInterface());
    }

    /**
     * Handles the action of clicking the button to logout of the user
     * interface, or to return to the home scene if another scene is displayed.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    protected void handleLogoutButtonAction(ActionEvent event) {
        if (logoutButton.getText().equals("Logout")) {
            //get the account manager
            AccountManager am = SceneController.getAccountManager();

            //get the current time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = dateFormat.format(new Date());

            //set the lastlogin to now
            am.setLastLogin(SceneController.USER_ID, now);

            handleSceneChangeButtonAction(event, SceneController.getMainInterface());
        } else {
            handleSceneChangeButtonAction(event, SceneController.getHomeInterface());
        }
    }

    /**
     * Handles the action of clicking the button to load the book menu.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleBookMenuButtonAction(ActionEvent event) {
        loadSubscene(getBookSearchInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the button to load the DVD menu.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleDVDMenuButtonAction(ActionEvent event) {
        loadSubscene(getDvdSearchInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the button to load the laptop menu.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleLaptopMenuButtonAction(ActionEvent event) {
        loadSubscene(getLaptopSearchInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the button to load the video game menu.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleVideoGameMenuButtonAction(ActionEvent event) {
        loadSubscene(getVideoGameSearchInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the button to list all resources.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleListAllButtonAction(ActionEvent event) {
        loadSubscene(getListAllInterface());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the popular button to get popular
     * statistics.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handlePopularListingAction(ActionEvent event) {
        loadSubscene(getPopularListing());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the events button.
     */
    @FXML
    public void goToEvents() {
        loadSubscene(getEventView());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Handles the action of clicking the statistics button to get statistics.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleUserStats(ActionEvent event) {
        loadSubscene(SceneController.getUserStats());
        changeLogoutToHome(logoutButton);
    }

    /**
     * Changes the text of the button to logout to say "Home".
     *
     * @param logoutButton the button to logout.
     */
    @FXML
    public void changeLogoutToHome(Button logoutButton) {
        logoutButton.setText("Home");
    }

    /**
     * Gets the user's current account balance, assigns it to a float and then
     * sets the text of the currentBalance label to display the balance.
     */
    @FXML
    private void updateBalanceLabel() {

        float balance;
        try {
            //get balance and round to 2 decimal places.
            balance = Math.round(getAccountManager().getAccountBalance(SceneController.USER_ID) * 100) / 100;
        } catch (SQLException e) {
            balance = 0.0F;
        }

        currentBalance.setText("£" + balance);

        //if the balance is less than 0, then change background color to red. Otherwise change to green.
        if (balance < 0) {
            currentBalance.setStyle("-fx-background-color: #ff644e; -fx-text-fill: WHITE;");
        } else {
            currentBalance.setStyle("-fx-background-color: #228022; -fx-text-fill: WHITE;");
        }
    }

    /**
     * Initialises the label in the interface to display the first name of the
     * user based on the account currently logged in.
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameDisplay.setText("Welcome, " + getAccountManager().
                getAccount(SceneController.USER_ID).getFirstName());
        try {
            avatarImage.setImage(new Image(getResourceManager().
                    getImageURL(getAccountManager().getAccount(SceneController.USER_ID).getAvatarID())));
        } catch (SQLException e) {
            avatarImage.setImage(new Image("/DefaultAvatar/Avatar1.png"));
        }

        updateBalanceLabel();
    }
}
