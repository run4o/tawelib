package JavaFX;

import Core.Copy;
import Core.Resource;
import Core.Staff;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Displays resources reserved by a user.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class ReservedInterfaceController extends SceneController implements Initializable {

    /**
     * ScrollPane to display the resources reserved by the user.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * Initialises the size and contents of the ScrollPane.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        setReservedListing();
    }

    /**
     * Unreserves a copy and gives an alert of the completion status.
     *
     * @param copyID The copy ID of the copy.
     */
    private void unReserve(int copyID) {

        try {
            getResourceFlowManager().unreserveCopy(copyID, getResourceFlowManager().getUserID());
            setReservedListing();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Successfully unreserved the copy: " + copyID);
            alert.showAndWait();

        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unreserve Failed: Error in the Database!");
            alert.showAndWait();

        } catch (IllegalStateException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }
    }

    /**
     * Borrows a copy and gives an alert of the completion status.
     *
     * @param copyID The copy ID of the copy.
     */
    private void borrow(int copyID) {

        try {
            getResourceFlowManager().borrowFromReserve(copyID, getResourceFlowManager().getUserID());
            setReservedListing();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Successfully borrowed the copy: " + copyID);
            alert.showAndWait();
        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Borrow Failed: Error in the Database!");
            alert.showAndWait();

        } catch (IllegalStateException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * Lists all resources reserved by the user.
     */
    public void setReservedListing() {

        FlowPane root = new FlowPane();

        //array of reserved copies
        Copy[] reservedCopies;

        //try to get all reserved copies
        try {
            reservedCopies = getResourceFlowManager().getReservedCopies(getResourceFlowManager().getUserID());
        } catch (SQLException e) {
            reservedCopies = new Copy[0];
        }

        GridPane[] reservedPanes = new GridPane[reservedCopies.length];

        //for every reserved copy. Add the copy to the scroll pane.
        for (int iCount = 0; iCount < reservedCopies.length; iCount++) {

            final int COPY_ID = reservedCopies[iCount].getCopyID();
            Resource resource = null;
            String resourceOut = null;

            //try and get the resource object.
            try {
                resource = getResourceManager().getResourceList("SELECT * FROM Resource WHERE "
                        + "RID = " + reservedCopies[iCount].getResourceID())[0];

                //if the resource is not null then get the summary of the resource.
                if (resource != null) {
                    resourceOut = resource.toString();
                }
            } catch (SQLException e) {
                resourceOut = "";
            }

            //The styling of the label
            Label label = new Label("CopyID: " + COPY_ID + "\n" + resourceOut);
            label.getStylesheets().add("/Resources/CoreStyle.css");
            label.getStyleClass().add("ScrollListItem");
            /*label.setMinHeight(300);*/
            label.setPrefWidth(380);
            label.setAlignment(Pos.CENTER);

            //Styling of the grid pane containing the description and buttons(if added).
            reservedPanes[iCount] = new GridPane();
            reservedPanes[iCount].setAlignment(Pos.CENTER);
            reservedPanes[iCount].add(label, 0, 0);
            reservedPanes[iCount].getStylesheets().add("/Resources/CoreStyle.css");
            reservedPanes[iCount].getStyleClass().add("SubScene");
            reservedPanes[iCount].setPrefWidth(580);

            //if the logged in user is a staff member who is managing another user then add an option to borrow
            if (getAccountManager().getAccount(SceneController.USER_ID) instanceof Staff) {

                //Create a grid pane to contain all of the buttons.
                GridPane buttonPane = new GridPane();

                //Create borrow from reserve button
                Button borrowButton = new Button("Borrow");
                borrowButton.getStylesheets().add("/Resources/CoreStyle.css");
                borrowButton.getStyleClass().add("UniversalButton");
                borrowButton.setPrefWidth(150);
                //Set on action
                borrowButton.setOnAction((event) -> borrow(COPY_ID));

                //Create un-reserve button
                Button unReserveButton = new Button("Un-Reserve");
                unReserveButton.getStylesheets().add("/Resources/CoreStyle.css");
                unReserveButton.getStyleClass().add("UniversalButton");
                unReserveButton.setPrefWidth(150);
                //Set on action
                unReserveButton.setOnAction((event) -> unReserve(COPY_ID));

                buttonPane.add(borrowButton, 0, 0);
                buttonPane.add(unReserveButton, 0, 1);

                reservedPanes[iCount].add(buttonPane, 1, 0);
            }

            //create separators and add padding.
            Separator separator = new Separator();
            Separator separator2 = new Separator();
            separator.setPadding(new Insets(20, 0, 20, 0));
            separator2.setPadding(new Insets(20, 0, 20, 0));

            //add separators to new row.
            reservedPanes[iCount].add(separator, 0, 2);
            reservedPanes[iCount].add(separator2, 1, 2);

        }

        //add the labels to the scroll pane.
        root.getChildren().addAll(reservedPanes);
        //root.setSpacing(10);
        scrollPane.setContent(root);

    }
}
