package JavaFX;

import Core.Review;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles the displaying of reviews
 *
 * @author Matt Ashman
 * @version 1.0
 */
public class ResourceReviewController extends SceneController implements Initializable {

    /**
     * Thumbnail image displayed on the button for the requested resource.
     */
    @FXML
    private ImageView resourceImg;

    /**
     * Label to display the title of the resource.
     */
    @FXML
    private Label resourceTitle;

    /**
     * GridPane to display the details of the requested resource.
     */
    @FXML
    private GridPane descriptionPane;

    /**
     * Initialises the details and thumbnail image for the requested resource.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            resourceImg.setImage(new Image(getResourceManager().getImageURL(getRequestResource().getThumbImage())));
        } catch (SQLException e) {
            System.out.println("Couldn't find image");
        }
        resourceTitle.setText(getRequestResource().getTitle());
        resourceTitle.wrapTextProperty().setValue(Boolean.TRUE);

        displayReviews();
    }

    /**
     * Handles display of the details for the review.
     */
    public void displayReviews() {
        int rowCounter = 0;
        int resourceId = getRequestResource().getResourceID();

        try {
            //Gets all reviews on resource
            Review reviews[] = getResourceFlowManager().getReviews(resourceId);

            if (reviews.length != 0) {
                //Iterates through each review
                for (Review reviewIterator : reviews) {

                    String name[] = getResourceFlowManager().getUserName(reviewIterator.getUserID());
                    String username = name[0];

                    //Sets all the info for the review
                    Label rating = new Label();
                    switch (reviewIterator.getRating()) {
                        case 5:
                            rating.setText("* * * * * ");
                            break;
                        case 4:
                            rating.setText("* * * * ");
                            break;
                        case 3:
                            rating.setText("* * * ");
                            break;
                        case 2:
                            rating.setText("* * ");
                            break;
                        default:
                            rating.setText("* ");
                            break;
                    }
                    descriptionPane.addRow(rowCounter, rating);
                    rowCounter++;

                    if (reviewIterator.getReview() != null) {
                        String reviewStr = reviewIterator.getReview();
                        int length = reviewStr.length();
                        int index = 0;
                        while (index < length) {
                            Label review = new Label(reviewStr.substring(index, Math.min(index + 64, reviewStr.length())));
                            descriptionPane.addRow(rowCounter, review);
                            rowCounter++;
                            index += 64;
                        }

                    }

                    Label nameLabel = new Label(username);
                    descriptionPane.addRow(rowCounter, nameLabel);
                    rowCounter++;

                    Label space = new Label(" ");
                    descriptionPane.addRow(rowCounter, space);
                    rowCounter++;
                }
            } else {
                Label nameLabel = new Label("This resource does not have any ratings or reviews.");
                descriptionPane.addRow(rowCounter, nameLabel);
            }

        } catch (SQLException e) {

            System.out.println("Error: " + e);
        }

        descriptionPane.setVgap(5);
        descriptionPane.getRowConstraints().setAll(new RowConstraints(20));
    }
}
