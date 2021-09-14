package JavaFX;

import Core.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles adding the review to the database
 *
 * @author Matt Ashman
 * @version 1.0
 */
public class AddReviewController extends ResourceController implements Initializable {

    /**
     * ChoiceBox to select the rating
     */
    @FXML
    private ChoiceBox<Integer> rating;

    /**
     * TextField for the review.
     */
    @FXML
    private TextField review;

    /**
     * Creates a review
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleAddReview(ActionEvent event) {
        //Get the resource ID of what is being reviewed
        Resource res = getRequestResource();
        int resId = res.getResourceID();

        //Rating cannot be left blank if review is not empty
        if (review.getText().isEmpty() && rating.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Cannot leave rating and review blank.");
            alert.showAndWait();
        } else if (!review.getText().isEmpty() && rating.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Cannot leave a review without a rating.");
            alert.showAndWait();
        } else {
            String reviewText = review.getText();
            int ratingInt = rating.getValue();

            getResourceManager().addReview(resId, ratingInt, reviewText);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Review added successfully.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rating.getItems().addAll(5, 4, 3, 2, 1);
    }
}
