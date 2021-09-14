package JavaFX;

import Core.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Displays the transaction history of the user.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class TransactionHistoryController extends SceneController implements Initializable {

    /**
     * ScrollPane to display the transaction history.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * Initialises the ScrollPane to display the transaction history.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        //get all transactions of a user
        Transaction[] transactions = getAccountManager().
                getTransactionHistory(SceneController.USER_ID);

        //create transactions
        Label[] transactionText = new Label[transactions.length];

        //for every transaction, add transaction to a label.
        for (int iCount = 0; iCount < transactions.length; iCount++) {
            transactionText[iCount] = new Label(transactions[iCount].toString());

            //Styling of label.
            transactionText[iCount].getStylesheets().add("/Resources/CoreStyle.css");
            transactionText[iCount].getStyleClass().add("ScrollListItem");
            transactionText[iCount].setMinHeight(200);
            transactionText[iCount].setPrefWidth(400);
            transactionText[iCount].setAlignment(Pos.CENTER);

            //if negative change in transaction, change colour to red. Otherwise change to green.
            if (transactions[iCount].getChange() < 0) {
                transactionText[iCount].setStyle("-fx-background-color: #ff5b5f; -fx-text-fill: WHITE;");
            } else {
                transactionText[iCount].setStyle("-fx-background-color: #2acb5a; -fx-text-fill: WHITE;");
            }
        }

        VBox vBox = new VBox();
        vBox.getChildren().addAll(transactionText);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        //center align listing
        HBox hBox = new HBox(vBox);
        hBox.setAlignment(Pos.CENTER);

        scrollPane.setContent(hBox);
    }
}
