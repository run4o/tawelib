package JavaFX;

import Core.LoanEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Lists all loaned items to date for the specific user.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class LoanHistoryController extends SceneController implements Initializable {

    /**
     * scrollPane to display the loan history.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * handles retrieving resources and their loan history.
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

        LoanEvent[] loanHistory = getResourceFlowManager().getBorrowHistory(userID);

        Label[] loanText = new Label[loanHistory.length];

        //For every loan history event. Add to a label.
        for (int iCount = 0; iCount < loanHistory.length; iCount++) {
            loanText[iCount] = new Label(loanHistory[iCount].toString());
            loanText[iCount].getStylesheets().add("/Resources/CoreStyle.css");
            loanText[iCount].getStyleClass().add("ScrollListItem");
            loanText[iCount].setMinHeight(100);
            loanText[iCount].setPrefWidth(300);
            loanText[iCount].setAlignment(Pos.CENTER);
        }

        //Wrap all labels in a vBox
        VBox vBox = new VBox();
        vBox.getChildren().addAll(loanText);

        //Make listing center.
        HBox hBox = new HBox(vBox);
        hBox.setAlignment(Pos.CENTER);

        //Set the content of the scroll pane to the vBox
        scrollPane.setContent(hBox);

    }
}
