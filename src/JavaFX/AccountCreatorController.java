package JavaFX;

import Core.DateManager;
import Core.Staff;
import Core.User;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface controller for the account creator interface.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @author Matt LLewellyn(V.2.0)
 * @version 1.0
 */
public class AccountCreatorController extends ResourceController implements Initializable {

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
     * TextField for the balance of a new account.
     */
    @FXML
    private TextField balance;

    /**
     * avatar image displayed on the avatar selection button.
     */
    @FXML
    private ImageView avatar;
    /**
     * Radio button to set created account as a user.
     */
    @FXML
    private RadioButton user;
    /**
     * Radio button to set created account as a staff.
     */
    @FXML
    private RadioButton staff;
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
     * Checks if password fields match.
     */
    private boolean checkPassword() {
        return passwordFieldRepeat.getText().equals(passwordField.getText());
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
            //Boolean to check if user agreed to consent.
            boolean isCheck = false;

            //Create a popup alert with accept and do not accept button.
            ButtonType foo = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
            ButtonType bar = new ButtonType("Do not accept", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "By creating an account, the user "
                    + "acknowledges that their information is being stored on the Tawe-Lib database.", foo, bar);
            alert.setTitle("User acknowledgment for data usage");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(bar) == foo) {
                isCheck = true;
            }

            //Check if user accepted consent.
            if (isCheck) {
                int userID = 0;
                //if the radio button for user is selected, add the details of a user to the database.
                if (user.isSelected()) {
                    User newAccount = new User(0, firstName.getText(), surname.getText(), phoneNumber.getText(),
                            streetNumber.getText(), streetName.getText(), county.getText(), city.getText(), postCode.getText(), "",
                            avatarID, 0, email.getText());
                    /*
			try to set the user's avatar, add them to the database and then set their balance. If adding user to
			 database fails, output an error.
                     */
                    try {
                        newAccount.setAvatarID(getResourceManager().getImageID(path));
                        userID = getAccountManager().addAccount(newAccount);
                        getAccountManager().changeBalance(userID, Float.parseFloat(balance.getText()));
                        getAccountManager().savePassword(passwordField.getText(), userID);
                        getAccountManager().updateSecret(secretQ.getText(), secretA.getText(), userID);
                    } catch (SQLException e) {
                        System.out.println("Invalid user in the database.");
                    } catch (Exception ex) {
                        System.out.println("User creation failed, try again?");
                    }
                    //if the radio button for staff is selected, add the details of a user as a staff member to the database.
                } else if (staff.isSelected()) {
                    Staff newAccount = new Staff(0, firstName.getText(), surname.getText(), phoneNumber.getText(),
                            streetNumber.getText(), streetName.getText(), county.getText(), city.getText(), postCode.getText(),
                            DateManager.returnCurrentDate(), 0, "", avatarID, 0, email.getText());
                    /*
			try to set the user's avatar, add them to the database and then set their balance. If adding user to
			 database fails, output an error.
                     */
                    try {
                        newAccount.setAvatarID(getResourceManager().getImageID(path));
                        userID = getAccountManager().addAccount(newAccount)[0];
                        getAccountManager().changeBalance(userID, Float.parseFloat(balance.getText()));
                        getAccountManager().savePassword(passwordField.getText(), userID);
                        getAccountManager().updateSecret(secretQ.getText(), secretA.getText(), userID);
                    } catch (SQLException e) {
                        System.out.println("failed");
                    } catch (Exception ex) {
                        Logger.getLogger(AccountCreatorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Throw message if user is added and show UserID
                Alert userAdd = new Alert(Alert.AlertType.INFORMATION);
                userAdd.setContentText("User added. UserID: " + userID);
                userAdd.showAndWait();
                handleSceneChangeButtonAction(event, getStaffInterface());
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
