/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class for reseting user password using the secret question.
 *
 * @author Martin Trifonov
 * @version 1.1
 */
public class ResetPassword implements Initializable {

    /**
     * Vbox contaiing the fields for selecting user.
     */
    @FXML
    private VBox userSelect;
    /**
     * Vbox containing the fields for secret question.
     */
    @FXML
    private VBox secret;
    /**
     * Vbox containing the fields for reseting password.
     */
    @FXML
    private VBox password;
    /**
     * TextField for secret question.
     */
    @FXML
    private TextField secretQ;
    /**
     * TextField for secret answer.
     */
    @FXML
    private TextField secretA;
    /**
     * PasswordField for new Password.
     */
    @FXML
    private PasswordField newPassword;
    /**
     * PasswordField for check.
     */
    @FXML
    private PasswordField newPasswordRepeat;
    /**
     * Textfield for user id input.
     */
    @FXML
    private TextField username;

    /**
     * Initializes the controller class.
     *
     * @param url URL.
     * @param rb ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userSelect.setVisible(true);
        secret.setVisible(false);
        password.setVisible(false);
    }

    /**
     * Handles the action onclick of reset.
     */
    @FXML
    private void reset(ActionEvent event) {
        if (SceneController.getAccountManager().checkSecretAnswer(secretA.getText(), Integer.valueOf(username.getText()))) {
            secret.setVisible(false);
            password.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong Answer");
            alert.showAndWait();
        }
    }

    /**
     * Handles the action onclick of savePassword.
     */
    @FXML
    private void savePassword(ActionEvent event) {
        if (newPassword.getText().equals(newPasswordRepeat.getText())) {
            try {
                if (newPassword.getText().length() < 8) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Password has to be at least 8 characters long!");
                    alert.showAndWait();
                } else {
                    SceneController.getAccountManager().savePassword(newPassword.getText(), Integer.valueOf(username.getText()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Password changed");
                    alert.showAndWait();
                    Stage stage = (Stage) newPassword.getScene().getWindow();
                    stage.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(ResetPassword.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Handles the action onclick of select user.
     */
    @FXML
    private void selectUser(ActionEvent event) {
        if (SceneController.getAccountManager().isExist(Integer.valueOf(username.getText()))) {
            userSelect.setVisible(false);
            secret.setVisible(true);
            secretQ.setText(SceneController.getAccountManager().getSecretQuestion(Integer.valueOf(username.getText())));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No such account found");
            alert.showAndWait();
        }

    }

}
