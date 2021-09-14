package JavaFX;

import Core.AuthenticationManager;
import Core.User;
import static JavaFX.SceneController.getDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Handles editing data of an existing user in the database.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class AccountEditorController extends ResourceController implements Initializable {

    /**
     * user ID of the user to be edited.
     */
    private final int id = getResourceFlowManager().getUserID();
    /**
     * file path for the avatar image.
     */
    private String path;
    /**
     * TextField for the first name of the account.
     */
    @FXML
    private TextField firstName;
    /**
     * TextField for the surname of the account.
     */
    @FXML
    private TextField surname;
    /**
     * TextField for the street name of the account's address.
     */
    @FXML
    private TextField streetName;
    /**
     * TextField for the house number of the account's address.
     */
    @FXML
    private TextField streetNumber;
    /**
     * TextField for the city of the account's address.
     */
    @FXML
    private TextField city;
    /**
     * TextField for the county of the account's address.
     */
    @FXML
    private TextField county;
    /**
     * TextField for the post code of the account's address.
     */
    @FXML
    private TextField postCode;
    /**
     * TextField for the phone number of the account.
     */
    @FXML
    private TextField phoneNumber;
    /**
     * avatar image displayed on the avatar selection button.
     */
    @FXML
    private ImageView avatar;

    /**
     * TextField for the new Password of the account.
     */
    @FXML
    private PasswordField newPassword;
    /**
     * TextField for the current Password of the account.
     */
    @FXML
    private PasswordField currentPassword;

    /**
     * TextField for the email of the account.
     */
    @FXML
    private TextField email;

    /**
     * TextField for the secret Question of the account.
     */
    @FXML
    private TextField secretQ;

    /**
     * TextField for the secret answer of the account.
     */
    @FXML
    private TextField secretA;

    /**
     * Sets the avatar for the user.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    private void handleSetAvatarButtonAction(ActionEvent event) {
        //set the image on the button to set the avatar
        path = setAvatar(event);
        avatar.setImage(new Image(path));
    }

    /**
     * Saves all details set in text fields to respective variables, to change
     * the values in the database.
     */
    @FXML
    public void handleSaveAction() {
        try {
            //Changes are saved only if the current password matches.
            AuthenticationManager login = new AuthenticationManager(String.valueOf(SceneController.USER_ID), getDatabase());
            if (login.authenticate(currentPassword.getText(), String.valueOf(id))) {
                if (!newPassword.getText().isEmpty() && newPassword.getText().length() < 8) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("New password has to be at least 8 characters long");
                    alert.showAndWait();
                } else {
                    try {
                        //get the account.
                        User account = getAccountManager().getAccount(id);
                        /*
                    check for each text field if it is empty or not. If not, assign its text value to the equivalent attribute
                    of the user in the database.
                         */
                        if (!(firstName.getText().isEmpty())) {
                            account.setFirstName(firstName.getText());
                        }
                        if (!(surname.getText().isEmpty())) {
                            account.setLastName(surname.getText());
                        }
                        if (!(streetName.getText().isEmpty())) {
                            account.setStreetName(streetName.getText());
                        }
                        if (!(streetNumber.getText().isEmpty())) {
                            account.setStreetNum(streetNumber.getText());
                        }
                        if (!(city.getText().isEmpty())) {
                            account.setCity(city.getText());
                        }
                        if (!(county.getText().isEmpty())) {
                            account.setCounty(county.getText());
                        }
                        if (!(postCode.getText().isEmpty())) {
                            account.setPostCode(postCode.getText());
                        }
                        if (!(phoneNumber.getText().isEmpty())) {
                            account.setTelNum(phoneNumber.getText());
                        }
                        if (!(email.getText().isEmpty())) {
                            account.setEmail(email.getText());
                            System.out.println(email.getText());
                        }
                        //Account Security
                        if (!(newPassword.getText().isEmpty())) {
                            SceneController.getAccountManager().savePassword(newPassword.getText(), id);
                        }
                        if (!secretQ.getText().isEmpty() && !secretA.getText().isEmpty()) {
                            SceneController.getAccountManager().updateSecret(secretQ.getText(), secretA.getText(), id);
                        } else if (!secretQ.getText().isEmpty() || !secretA.getText().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Secret Question was not saved as one of the fields was not filled. Old values remain");
                            alert.showAndWait();

                        }
                        //set the user's avatar in the database.
                        account.setAvatarID(getResourceManager().getImageID(path));

                        //edit the account with the new details.
                        getAccountManager().editAccount(account);

                        //Show the user a window indicating that their save was successful.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Save success.");
                        alert.showAndWait();

                        Stage stage = (Stage) email.getScene().getWindow();
                        stage.close();

                    } catch (SQLException e) {
                        //sql error in database.
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Error in database!");
                        alert.showAndWait();
                    } catch (IllegalArgumentException e) {
                        //image specified does not exist.
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Image specified must already exist!");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Current password doesn't match");
            alert.showAndWait();
        }
    }

    /**
     * Cancels all changes and returns back to Resource Flow Interface.
     */
    @FXML
    public void handleCancelAction() {
        Stage stage = (Stage) this.avatar.getScene().getWindow();
        stage.close();
    }

    /**
     * Initialises the interface to display the current details of the user in
     * the text fields.
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User account = getAccountManager().getAccount(id);
        firstName.setText(account.getFirstName());
        surname.setText(account.getLastName());
        streetName.setText(account.getStreetName());
        streetNumber.setText(account.getStreetNum());
        city.setText(account.getCity());
        county.setText(account.getCounty());
        postCode.setText(account.getPostCode());
        phoneNumber.setText(account.getTelNum());
        email.setText(account.getEmail());
        /*
		try to get the file path of the existing avatar for the user and set the image for the select avatar button to
		the image at that path. If getting the URL from the database fails, tell the user that the avatar
		could not be loaded.
         */
        try {
            path = getResourceManager().getImageURL(account.getAvatarID());
            avatar.setImage(new Image(path));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Couldn't load an avatar.");
            alert.showAndWait();
        }
    }
}
