package JavaFX;

import Core.VideoGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles editing data of an existing VideoGame in the database.
 *
 * @author Rory Richards
 * @version 1.0
 */
public class EditVideoGameController extends ResourceController implements Initializable {

    /**
     * VideoGame to be edited.
     */
    private final VideoGame videoGame = (VideoGame) getRequestResource();
    /**
     * file path for the thumbnail image of the VideoGame.
     */
    private String path;

    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField title;
    /**
     * TextField for the year of release of the VideoGame.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField publisher;
    /**
     * TextField for the year of release of the VideoGame.
     */
    @FXML
    private TextField genre;
    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField certRating;
    /**
     * TextField for the year of release of the VideoGame.
     */
    @FXML
    private TextField multiplayer;
    /**
     * TextField for the number of copies of the VideoGame.
     */
    @FXML
    private TextField numOfCopies;

    /**
     * thumbnail image displayed on the select thumbnail button.
     */
    @FXML
    private ImageView thumbnailImg;

    /**
     * handles the general retrieval and editing of VideoGame description.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!videoGame.getTitle().isEmpty()) {
            title.setText(videoGame.getTitle());
        }
        if (videoGame.getYear() != 0) {
            year.setText(String.valueOf(videoGame.getYear()));
        }
        if (!videoGame.getPublisher().isEmpty()) {
            publisher.setText(videoGame.getPublisher());
        }
        if (!videoGame.getGenre().isEmpty()) {
            genre.setText(videoGame.getGenre());
        }
        if (!videoGame.getCertRating().isEmpty()) {
            certRating.setText(videoGame.getCertRating());
        }
        if (!videoGame.getMultiplayer().isEmpty()) {
            multiplayer.setText(videoGame.getMultiplayer());
        }
        if (getResourceManager().getCopies(videoGame.getResourceID()).length != 0) {
            numOfCopies.setText(String.valueOf(getResourceManager().getCopies(videoGame.getResourceID()).length));
        } else {
            numOfCopies.setText("0");
        }
        if (videoGame.getThumbImage() != 0) {
            try {
                thumbnailImg.setImage(new Image(getResourceManager().getImageURL(videoGame.getThumbImage())));
                path = getResourceManager().getImageURL(videoGame.getThumbImage());
            } catch (SQLException e) {
                System.out.println("Couldn't find image.");
            }
        }
    }

    /**
     * Saves all details set in text fields to respective variables, to change
     * the values in the database.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleSaveButtonAction(ActionEvent event) {
        if (!title.getText().isEmpty() && !year.getText().isEmpty() && !thumbnailImg.equals(null)
                && !publisher.getText().isEmpty() && !genre.getText().isEmpty() && !certRating.getText().isEmpty()
                && !multiplayer.getText().isEmpty()) {
            try {
                getResourceManager().editResource(new VideoGame(videoGame.getResourceID(), title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), videoGame.getdateCreated(), publisher.getText(),
                        genre.getText(), certRating.getText(), multiplayer.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Changes Saved Successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Couldn't load an image.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Not enough information.");
            alert.showAndWait();
        }
    }

    /**
     * Cancels all changes.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        cancel();
    }

    /**
     * Assigns the thumbnail selected to the specific VideoGame.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleSetThumbnailButtonAction(ActionEvent event) {
        try {
            path = setThumbnailImage(event);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        thumbnailImg.setImage(new Image(path));
    }
}
