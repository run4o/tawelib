package JavaFX;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays the dashboard of the current user.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class UserDashboardInterfaceController extends SceneController implements Initializable {

    /**
     * Button to return back to the homepage.
     */
    @FXML
    private Button homeButton;

    /**
     * A label that displays the current balance of the user.
     */
    @FXML
    private Label currentBalance;

    /**
     * Returns user to the home scene.
     *
     * @param event The event triggered by clicking the button.
     */
    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        if (homeButton.getText().contains("Home")) {
            handleSceneChangeButtonAction(event, SceneController.getHomeInterface());
        } else {
            handleSceneChangeButtonAction(event, SceneController.getUserDashboardInterface());
        }
    }

    /**
     * Displays Profile Image Selector subscene.
     */
    @FXML
    private void handleAvatarChangeButtonAction() {
        loadSubscene(SceneController.getAvatarChangeInterface());
        changeDashboardButton();
    }

    /**
     * Displays Transaction History subscene.
     */
    @FXML
    private void handleTransactionHistoryAction() {
        loadSubscene(SceneController.getTransactionHistoryInterface());
        changeDashboardButton();
    }

    /**
     * Displays Loan History subscene.
     */
    @FXML
    private void handleLoanAction() {
        loadSubscene(SceneController.getLoanHistoryController());
        changeDashboardButton();
    }

    /**
     * Displays Reserve History subscene.
     */
    @FXML
    private void handleReservedAction() {
        loadSubscene(SceneController.getReserveHistoryController());
        changeDashboardButton();
    }

    /**
     * Changes home button text to 'Dashboard'.
     */
    @FXML
    private void changeDashboardButton() {
        homeButton.setText("Dashboard");
    }

    /**
     * Displays Requested Resources subscene.
     */
    @FXML
    private void handleRequestedResourceAction() {
        loadSubscene(SceneController.getRequestedResource());
        changeDashboardButton();
    }

    /**
     * Displays Items Due subscene.
     */
    @FXML
    private void handleItemsDueAction() {
        loadSubscene(SceneController.getItemsDue());
        changeDashboardButton();
    }

    /**
     * Displays New Additions subscene.
     */
    @FXML
    private void handleNewAdditionsAction() {
        loadSubscene(SceneController.getNewAdditions());
        changeDashboardButton();
    }

    /**
     * Displays account editing subscene.
     */
    @FXML
    private void editAccount() {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(this.currentBalance.getScene().getWindow());

            Parent parent = FXMLLoader.load(getClass().getResource(SceneController.getAccountEditorInterface()));
            Scene scene = new Scene(parent);

            popup.setScene(scene);
            popup.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginInterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates Balance label.
     */
    private void updateBalanceLabel() {

        float balance;
        try {
            //get balance and round to 2 decimal places.
            balance = Math.round(getAccountManager().getAccountBalance(SceneController.USER_ID) * 100) / 100;
        } catch (SQLException e) {
            balance = 0.0F;
        }

        currentBalance.setText("�" + balance);

        //if the balance is less than 0, then change background color to red. Otherwise change to green.
        if (balance < 0) {
            currentBalance.setStyle("-fx-background-color: #ff644e; -fx-text-fill: WHITE;");
        } else {
            currentBalance.setStyle("-fx-background-color: #228022; -fx-text-fill: WHITE;");
        }
    }

    /**
     * method to initalize the balance label.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateBalanceLabel();

    }
}
