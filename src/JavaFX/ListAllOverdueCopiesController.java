package JavaFX;

import Core.Copy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Lists all overdue copies.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class ListAllOverdueCopiesController extends SceneController implements Initializable {

    /**
     * The border pane than contains the scroll pane.
     */
    @FXML
    private BorderPane borderPane;

    /**
     * Handles retrieving overdue copies.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //get all overdue copies
        Copy[] copies = getResourceFlowManager().showOverdueCopies();

        //Wrap all labels in a vBox
        VBox vBox = new VBox();

        //For every loan history event. Add to a label.
        for (int iCount = 0; iCount < copies.length; iCount++) {

            //create message to display
            String message = "Borrowed by User ID - " + copies[iCount].getCurrentBorrowerID();
            message += copies[iCount].toString();

            //create label and style
            Label label = new Label(message);
            label.getStylesheets().add("/Resources/CoreStyle.css");
            label.getStyleClass().add("ScrollListItem");
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(560);

            //add label to vbox.
            vBox.getChildren().add(label);

        }

        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        //Make listing center.
        HBox hBox = new HBox(vBox);
        hBox.setAlignment(Pos.CENTER);

        //wrap in hbox
        ScrollPane scrollPane = new ScrollPane(hBox);
        //scroll pane styling
        scrollPane.getStylesheets().add("/Resources/CoreStyle.css");
        scrollPane.getStyleClass().add("UserDashboard");

        //wrap scroll pane in border pane
        borderPane.setCenter(scrollPane);

    }
}
