package JavaFX;

import Core.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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
 * Handles user specific operations regarding resources.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class ResourceFlowController extends SceneController implements Initializable {

    /**
     * profile image of the user displayed on the interface.
     */
    @FXML
    private ImageView profileImage;
    /**
     * Text box for the name of the user.
     */
    @FXML
    private Text textName;
    /**
     * TextField for the ID of the user.
     */
    @FXML
    private TextField textIDField;
    /**
     * Copy ID for a selected copy.
     */
    private int selectedCopyID = -1;

    /**
     * Initialises the details of the user in the interface.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Set the users profile image.
            int id = getResourceFlowManager().getUserID();
            User user = getAccountManager().getAccount(id);

            //set profile image
            int imageID = user.getAvatarID();
            String imageURL = getResourceManager().getImageURL(imageID);
            profileImage.setImage(new Image(imageURL));

            //set name
            String name = user.getFirstName() + " " + user.getLastName();
            textName.setText(name);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("User has an undefined profile image.");
            alert.showAndWait();
        }
    }

    /**
     * Displays Transaction Manager subscene.
     */
    @FXML
    private void handleTransactionAction() {
        loadSubscene(SceneController.getTransactionManagerInterface());
    }

    /**
     * Displays loan history subscene.
     */
    @FXML
    private void handleViewLoanHistoryAction() {
        loadSubscene(SceneController.getLoanHistoryController());
    }

    /**
     * Displays items due subscene.
     */
    @FXML
    private void handleDueItemsAction() {
        loadSubscene(SceneController.getItemsDue());
    }

    /**
     * Displays reserve history subscene.
     */
    @FXML
    private void handleReservedResourcesAction() {
        loadSubscene(SceneController.getReserveHistoryController());
    }

    /**
     * Displays account editor subscene.
     */
    @FXML
    private void handleEditAccountAction() {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(this.profileImage.getScene().getWindow());

            Parent parent = FXMLLoader.load(getClass().getResource(SceneController.getAccountEditorInterface()));
            Scene scene = new Scene(parent);

            popup.setScene(scene);
            popup.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginInterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Takes the entered user ID, validates it and marks it as the selected ID
     * upon which operations will be performed.
     */
    @FXML
    private void handleSelectIDAction() {

        try {

            String textData = textIDField.getText();

            int copyID = Integer.parseInt(textData);

            if (getResourceManager().getCopy(copyID) != null) {

                selectedCopyID = copyID;
                textIDField.setStyle("-fx-background-color: #2acb5a; -fx-text-fill: WHITE;");

            } else {
                textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Specified copy does not exist!");
                alert.showAndWait();
            }

        } catch (NumberFormatException n) {
            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Non numeric value entered!");
            alert.showAndWait();
        }
    }

    /**
     * Borrows a copy of specified resource to selected user.
     */
    @FXML
    private void handleBorrowCopyAction() {

        try {
            if (selectedCopyID != -1) {

                getResourceFlowManager().borrowCopy(selectedCopyID, getResourceFlowManager().getUserID());

                textIDField.setStyle("-fx-background-color: #2acb5a; -fx-text-fill: WHITE;");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully borrowed the copy: " + selectedCopyID);
                alert.showAndWait();

                selectedCopyID = -1;

            } else {
                textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("No copy selected!");
                alert.showAndWait();
            }
        } catch (SQLException e) {

            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Borrow Failed: Error in the Database!");
            alert.showAndWait();

        } catch (IllegalStateException e) {

            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } finally {
            textIDField.setText("");
        }
    }

    /**
     * Returns a copy for the specific user.
     */
    @FXML
    private void handleReturnCopyAction() {

        try {
            if (selectedCopyID != -1) {

                getResourceFlowManager().returnCopy(selectedCopyID, getResourceFlowManager().getUserID());

                textIDField.setStyle("-fx-background-color: #2acb5a; -fx-text-fill: WHITE;");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully return the copy: " + selectedCopyID);
                alert.showAndWait();

                selectedCopyID = -1;

            } else {
                textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("No copy selected!");
                alert.showAndWait();
            }
        } catch (SQLException e) {

            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Return Failed: Error in the Database!");
            alert.showAndWait();

        } catch (IllegalStateException e) {

            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } finally {
            textIDField.setText("");
        }
    }

    /**
     * Unreserves a resource for the specified user.
     */
    @FXML
    private void handleUnReserveAction() {

        try {
            if (selectedCopyID != -1) {

                getResourceFlowManager().unreserveCopy(selectedCopyID, getResourceFlowManager().getUserID());
                textIDField.setStyle("-fx-background-color: #2acb5a; -fx-text-fill: WHITE;");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully unreserved the copy: " + selectedCopyID);
                alert.showAndWait();

                selectedCopyID = -1;

            } else {
                textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("No copy selected!");
                alert.showAndWait();
            }
        } catch (SQLException e) {

            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unreserve Failed: Error in the Database!");
            alert.showAndWait();

        } catch (IllegalStateException e) {

            textIDField.setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } finally {
            textIDField.setText("");
        }
    }
}
