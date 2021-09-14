package JavaFX;

import Core.Copy;
import Core.LoanEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Responsible for handling the logs of each resource.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class ResourceLogController extends SceneController implements Initializable {

    /**
     * ScrollPane to display the log of a resource.
     */
    @FXML
    private ScrollPane scrollPane;
    /**
     * TextField for the ID of a resource.
     */
    @FXML
    private TextField textResourceID;
    /**
     * RadioButton to decide whether all copies of a resource are displayed.
     */
    @FXML
    private RadioButton allCopiesRadio;
    /**
     * RadioButton to decide whether loan history of a resource is displayed.
     */
    @FXML
    private RadioButton loanHistoryRadio;
    /**
     * RadioButton to decide whether overdue copies of a resource are displayed.
     */
    @FXML
    private RadioButton overdueCopies;
    /**
     * ToggleGroup to contain all of the RadioButtons.
     */
    private ToggleGroup radioGroup;

    /**
     * Initializes the RadioButtons into a single ToggleGroup.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Add radio buttons to group.
        radioGroup = new ToggleGroup();

        allCopiesRadio.setToggleGroup(radioGroup);
        loanHistoryRadio.setToggleGroup(radioGroup);
        overdueCopies.setToggleGroup(radioGroup);

    }

    /**
     * Handles the searching of a resource copy.
     */
    @FXML
    private void handleSearchAction() {

        try {

            int resourceID = Integer.parseInt(textResourceID.getText());

            //if the get all copies radio button has been selected then list all copies.
            if (radioGroup.getSelectedToggle() == allCopiesRadio) {
                listAllCopies(resourceID);

            } else if (radioGroup.getSelectedToggle() == loanHistoryRadio) {
                //if all loan history selected then list all loan history.
                listloanHistoryRadio(resourceID);

            } else if (overdueCopies.getToggleGroup() == radioGroup) {
                //if all loan history selected then list all loan history.
                listOverdueCopies(resourceID);
            } else {
                //no radio button selected!
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select an option!");
                alert.showAndWait();
            }

        } catch (NumberFormatException n) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a numerical resource identifier.");
            alert.showAndWait();

        }

    }

    /**
     * Handles the listing of the copies specified.
     *
     * @param resourceID the unique ID of the resource.
     */
    private void listAllCopies(int resourceID) {

        //the copies to list
        Copy[] resourceCopies = getResourceManager().getCopies(resourceID);

        //wraps labels in a vbox
        VBox vBox = new VBox();

        //for every copy, add the toString to the label.
        for (Copy resourceCopy : resourceCopies) {
            //create label and style
            Label copyLabel = new Label(resourceCopy.toString());
            copyLabel.setMinWidth(300);
            copyLabel.getStylesheets().add("/Resources/CoreStyle.css");
            copyLabel.getStyleClass().add("UniversalButton");

            vBox.getChildren().add(copyLabel);
        }

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        scrollPane.setContent(vBox);

    }

    /**
     * Handles the loan history option for listing.
     *
     * @param resourceID the unique ID of the resource.
     */
    private void listloanHistoryRadio(int resourceID) {

        //the copies to list
        LoanEvent[] loanHistory = getResourceFlowManager().
                getBorrowHistoryByResource(resourceID);

        //wraps labels in a vbox
        VBox vBox = new VBox();

        //for every loan history, add to label.
        for (LoanEvent loanEvent : loanHistory) {
            //create label and style
            Label copyLabel = new Label(loanEvent.toString());
            copyLabel.setMinWidth(300);
            copyLabel.getStylesheets().add("/Resources/CoreStyle.css");
            copyLabel.getStyleClass().add("UniversalButton");

            //add label to vbox.
            vBox.getChildren().add(copyLabel);
        }

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        scrollPane.setContent(vBox);

    }

    /**
     * Handles the option for listing overdue copies.
     *
     * @param resourceID the unique ID of the resource.
     */
    private void listOverdueCopies(int resourceID) {

        //the copies to list
        Copy[] resourceCopies = getResourceFlowManager().showOverdueCopies(resourceID);

        //wraps labels in a vbox
        VBox vBox = new VBox();

        //for every copy, add the toString to the label.
        for (Copy resourceCopy : resourceCopies) {
            //create label and style
            Label copyLabel = new Label(resourceCopy.toString());
            copyLabel.setMinWidth(300);
            copyLabel.getStylesheets().add("/Resources/CoreStyle.css");
            copyLabel.getStyleClass().add("UniversalButton");

            vBox.getChildren().add(copyLabel);
        }

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        scrollPane.setContent(vBox);

    }
}
