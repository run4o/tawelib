package JavaFX;

import Core.User;
import static JavaFX.SceneController.getAccountManager;
import static JavaFX.SceneController.getResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.stage.Stage;

/**
 * Interface controller for the account creator interface in the login page.
 *
 * @author Martin Trifonov
 * @version 1.0
 */
public class LoginAccountCreation extends ResourceController implements Initializable {

    /**
     * file path for the avatar image.
     */
    private String path;
    /**
     * TextField for the first name of a new account.
     */
    @FXML
    private TextField firstName;
    /**
     * TextField for the surname of a new account.
     */
    @FXML
    private TextField surname;
    /**
     * TextField for the street name of a new account's address.
     */
    @FXML
    private TextField streetName;
    /**
     * TextField for the house number of a new account's address.
     */
    @FXML
    private TextField streetNumber;
    /**
     * TextField for the city of a new account's address.
     */
    @FXML
    private TextField city;
    /**
     * TextField for the county of a new account's address.
     */
    @FXML
    private TextField county;
    /**
     * TextField for the post code of a new account's address.
     */
    @FXML
    private TextField postCode;
    /**
     * TextField for the phone number of a new account.
     */
    @FXML
    private TextField phoneNumber;

    /**
     * TextField for the secret question of a new account.
     */
    @FXML

    private TextField secretQ;
    /**
     * TextField for the email of a new account.
     */
    @FXML
    private TextField email;
    /**
     * TextField for the secret answer of a new account.
     */
    @FXML
    private TextField secretA;

    /**
     * avatar image displayed on the avatar selection button.
     */
    @FXML
    private ImageView avatar;
    /**
     * ID of the user's avatar.
     */
    private int avatarID = 0;

    /**
     * TextField for the password of the new account.
     */
    @FXML
    private TextField passwordField;
    /**
     * TextField for the repeat password of the new account.
     */
    @FXML
    private TextField passwordFieldRepeat;

    /**
     * Checks if the password field are the same.
     */
    private boolean checkPassword() {
        return passwordFieldRepeat.getText().equals(passwordField.getText());
    }

    /**
     * Checks if the password has at least 8 characters.
     */
    private boolean isStrongPassword() {
        return passwordFieldRepeat.getText().length() >= 8;
    }

    /**
     * Checks if all fields are filled.
     * @returns error massage for missing fields.
     */
    private String checkFields() {
        String check = "";
        if (firstName.getText().isEmpty()) {
            check += "First Name is not filled! \n";
        }
        if (surname.getText().isEmpty()) {
            check += "Surname is not filled! \n";
        }
        if (streetName.getText().isEmpty()) {
            check += "Street Name is not filled! \n";
        }
        if (streetNumber.getText().isEmpty()) {
            check += "Street Number is not filled! \n";
        }
        if (city.getText().isEmpty()) {
            check += "City is not filled! \n";
        }
        if (county.getText().isEmpty()) {
            check += "County is not filled! \n";
        }
        if (postCode.getText().isEmpty()) {
            check += "Post Code is not filled! \n";
        }
        if (phoneNumber.getText().isEmpty()) {
            check += "Phone Number is not filled! \n";
        }
        if (email.getText().isEmpty()) {
            check += "Email is not filled! \n";
        }
        if (secretQ.getText().isEmpty() || secretA.getText().isEmpty()) {
            check += "Secret Question is not fully filled! \n";
        }
        return check;
    }

    /**
     * Adds a user or staff member to the database based on the information
     * entered in the text fields on the interface.
     *
     * @param event the event triggered by clicking the button.
     */
    public void handleCreateUserButtonAction(ActionEvent event) {
        if (!checkPassword()) {
            Alert userAdd = new Alert(Alert.AlertType.ERROR);
            userAdd.setContentText("Password doens't match, reenter!");
            userAdd.showAndWait();
        } else {
            if (!isStrongPassword()) {
                Alert userAdd = new Alert(Alert.AlertType.ERROR);
                userAdd.setContentText("Your password needs to be at least 8 characters!");
                userAdd.showAndWait();
            } else {
                String check = checkFields();
                if (check.length() > 1) {
                    Alert userAdd = new Alert(Alert.AlertType.ERROR);
                    userAdd.setContentText(check);
                    userAdd.showAndWait();
                } else {
                    //Boolean to check if user agreed to consent.
                    boolean isCheck = false;

                    //Create a popup alert with accept and do not accept button.
                    ButtonType foo = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
                    ButtonType bar = new ButtonType("Do not accept", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "By creating an account, the user "
                            + "acknowledges that their information is being stored on the Tawe-Lib database."
                            + "Your email adress will be used to send out notifications!", foo, bar);
                    alert.setTitle("User acknowledgment for data usage");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.orElse(bar) == foo) {
                        isCheck = true;
                    }

                    //Check if user accepted consent.
                    if (isCheck) {
                        int userID = 0;
                        User newAccount = new User(0, firstName.getText(), surname.getText(), phoneNumber.getText(),
                                streetNumber.getText(), streetName.getText(), county.getText(), city.getText(), postCode.getText(), "",
                                avatarID, 0, email.getText());
                        try {
                            newAccount.setAvatarID(getResourceManager().getImageID(path));
                            userID = getAccountManager().addAccount(newAccount);
                            getAccountManager().savePassword(passwordField.getText(), userID);
                            getAccountManager().updateSecret(secretQ.getText(), secretA.getText(), userID);
                        } catch (SQLException e) {
                            System.out.println("Invalid user in the database.");
                        } catch (Exception ex) {
                            System.out.println("User creation failed, try again?");
                        }
                        //Throw message if user is added and show UserID
                        Alert userAdd = new Alert(Alert.AlertType.INFORMATION);
                        userAdd.setContentText("User added. UserID: " + userID);
                        userAdd.showAndWait();
                        Stage stage = (Stage) email.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }

    /**
     * Sets the avatar for the user.
     *
     * @param event the event triggered by clicking the button.
     */
    public void handleSetAvatarButtonAction(ActionEvent event) {
        //set the image on the button to set the avatar
        path = setAvatar(event);
        avatar.setImage(new Image(path));
    }

    /**
     * initialises the values of the avatarID and the file path and the image on
     * the button to set the avatar
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
		try to set the values of avatarID and path and the image of avatar. If getting the avatarID fails,
		output an error.
         */
        try {
            String DEFAULT_URL = "/DefaultAvatars/Avatar1.png";
            avatarID = getResourceManager().getImageID(DEFAULT_URL);
            path = getResourceManager().getImageURL(avatarID);
            avatar.setImage(new Image(getResourceManager().getImageURL(avatarID)));
        } catch (SQLException e) {
            System.out.println("Default avatarID is invalid.");
        }
    }
}
