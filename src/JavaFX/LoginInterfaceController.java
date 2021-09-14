package JavaFX;

import Core.AuthenticationManager;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Interface controller for the login screen.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class LoginInterfaceController extends SceneController {

    /**
     * TextField for the user ID to login.
     */
    @FXML
    private TextField loginUsername;

    /**
     * Password field for inputing password.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Handles the action of clicking the button to login to the user or staff
     * interface.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        AuthenticationManager login = new AuthenticationManager(loginUsername.getText(), getDatabase());
        SceneController.USER_ID = Integer.parseInt(loginUsername.getText());
        getResourceFlowManager().setUserID(SceneController.USER_ID);
        try {
            if (login.authenticate(loginKey(), loginUsername.getText())) {
                try {
                    if (login.isStaff()) {
                        handleSceneChangeButtonAction(event, SceneController.getStaffInterface());
                    } else {
                        handleSceneChangeButtonAction(event, SceneController.getHomeInterface());
                    }
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Incorect password");
                    alert.showAndWait();
                }
            } else {

                throw new IllegalArgumentException("Invalid user id");
            }
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid User ID");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong password");
            alert.showAndWait();
        }
    }

    /**
     * When 'Enter' button is pressed, executes the action of the Login Button.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    protected void onEnter(ActionEvent event) {
        handleLoginButtonAction(event);
    }

    /**
     * Gets the input of the password field.
     */
    private String loginKey() {
        return passwordField.getText();
    }

    /**
     * Create new account button onclick action.
     */
    @FXML
    private void createAccount(ActionEvent event) {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(loginUsername.getScene().getWindow());

            Parent parent = FXMLLoader.load(getClass().getResource(SceneController.getLoginUserCreation()));
            Scene scene = new Scene(parent);

            popup.setScene(scene);
            popup.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginInterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reset password button onclick action.
     */
    @FXML
    private void resetPassword(ActionEvent event) {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(loginUsername.getScene().getWindow());

            Parent parent = FXMLLoader.load(getClass().getResource(SceneController.getResetPassword()));
            Scene scene = new Scene(parent);

            popup.setScene(scene);
            popup.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginInterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
