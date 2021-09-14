package JavaFX;

import Core.Copy;
import Core.DateManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Lists all items currently due.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class ItemsDueInterfaceController extends SceneController implements Initializable {

    /**
     * ScrollPane used to display items due.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * handles retrieving items due.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        int userID = getResourceFlowManager().getUserID();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        VBox root = new VBox();

        Copy[] copiesOnLoan = getResourceFlowManager().getBorrowedCopies(userID);
        GridPane[] copyContainer = new GridPane[copiesOnLoan.length];

        //For every copy create the grid pane and the labels inside it.
        for (int iCount = 0; iCount < copiesOnLoan.length; iCount++) {

            //The labels to be contained in the grid pane (copy container).
            Label titleLabel = new Label();
            Label contentLabel = new Label();

            //Setup the copy container and add styling.
            copyContainer[iCount] = new GridPane();
            copyContainer[iCount].getStylesheets().add("/Resources/CoreStyle.css");
            copyContainer[iCount].getStyleClass().add("ScrollListItem");
            copyContainer[iCount].setMinHeight(400);
            copyContainer[iCount].setMinWidth(300);
            copyContainer[iCount].setAlignment(Pos.CENTER);

            //Setup content label and styling.
            contentLabel.getStylesheets().add("/Resources/CoreStyle.css");
            contentLabel.getStyleClass().add("ScrollListItem");
            contentLabel.setMinWidth(300);
            contentLabel.setMinHeight(200);

            //Setup title label and styling.
            titleLabel.getStylesheets().add("/Resources/CoreStyle.css");
            titleLabel.getStyleClass().add("ScrollTitle");
            titleLabel.setMinHeight(100);

            //try and get resource title of the copy.
            try {
                titleLabel.setText("\n" + getResourceManager().
                        getResourceList("SELECT * FROM Resource WHERE RID = "
                                + copiesOnLoan[iCount].getResourceID())[0].getTitle());
            } catch (SQLException e) {
                titleLabel.setText("");
            }

            //Text to be displayed
            String contentText = copiesOnLoan[iCount].toString();

            //If the copy has a due date and is overdue then make color red and add to text that copy is overdue.
            //Otherwise make green.
            if (copiesOnLoan[iCount].getDueDate() != null
                    && copiesOnLoan[iCount].calculateDaysOffset(DateManager.returnCurrentDate()) < 0) {

                copyContainer[iCount].setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");

                contentText += "\nCopy overdue by "
                        + copiesOnLoan[iCount].calculateDaysOffset(DateManager.returnCurrentDate())
                        + " days!";

            } else {
                copyContainer[iCount].setStyle("-fx-background-color: #2acb5a; -fx-text-fill: WHITE;");
            }

            //Set the text in the contentLabel
            contentLabel.setText(contentText);

            //Set the titleLabel and contentLabel in the copyContainer.
            copyContainer[iCount].add(titleLabel, 1, 1);
            copyContainer[iCount].add(contentLabel, 1, 2);

        }

        root.getChildren().addAll(copyContainer);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        scrollPane.setContent(root);

    }
}
