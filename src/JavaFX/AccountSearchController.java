package JavaFX;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Performs User search by User ID.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 */
public class AccountSearchController extends SceneController {

    /**
     * TextField for the user ID of the account to be searched for.
     */
    @FXML
    private TextField accountName;

    /**
     * Takes in a User ID and loads the Resource Flow Interface for the specific
     * User.
     */
    @FXML
    private void handleAccountSearchButtonAction() {

        /*
		try to get the entered user ID and pass it into the resource flow manager interface in order to view
		the selected user. If the entered value is not numerical, show the user an error.
         */
        try {
            //get entered user ID.
            /**
             * user ID of the account to be searched for.
             */
            int userID = Integer.parseInt(accountName.getText());

            /*
			if the account exists in the database, change subscene to the resource flow manager. If not, show the user
			an error.
             */
            if (getAccountManager().isExist(userID)) {
                //set the user ID in resource flow manager to select a user for operations.
                getResourceFlowManager().setUserID(userID);
                loadSubscene(SceneController.getResourceFlowInterface());
            } else {
                //create alert that user does not exist.
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Specified user does not exist.");
                alert.showAndWait();
            }
        } catch (NumberFormatException n) {

            //create alert if non integer is typed.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Must be a numeric value");
            alert.showAndWait();

        }
    }
}
