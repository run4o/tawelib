package JavaFX;

import Core.Copy;
import Core.VideoGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles adding data of a VideoGame into the database.
 *
 * @author Rory Richards
 * @version 1.0
 */
public class AddVideoGameController extends ResourceController implements Initializable {

    /**
     * file path for the thumbnail of the VideoGame.
     */
    private String path;

    /**
     * ChoiceBox to select the duration of the loan on the VideoGame.
     */
    @FXML
    private ChoiceBox loanDuration;

    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField title;
    /**
     * TextField for the release year of the VideoGame.
     */
    @FXML
    private TextField year;

    /**
     * TextField for the publisher of the VideoGame.
     */
    @FXML
    private TextField publisher;
    /**
     * TextField for the release year of the VideoGame.
     */
    @FXML
    private TextField genre;
    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField certRating;
    /**
     * TextField for the release year of the VideoGame.
     */
    @FXML
    private TextField multiplayer;

    /**
     * TextField for the number of copies of the VideoGame.
     */
    @FXML
    private TextField numOfCopies;

    /**
     * thumbnail image on the select thumbnail button.
     */
    @FXML
    private ImageView thumbImage;

    /**
     * creates a new VideoGame resource.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleCreateResourceButtonAction(ActionEvent event) {
        if (!title.getText().isEmpty() && !year.getText().isEmpty() && !publisher.getText().isEmpty()
                && !genre.getText().isEmpty() && !certRating.getText().isEmpty() && !multiplayer.getText().isEmpty()) {
            try {
                getResourceManager().addResource(new VideoGame(0, title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), SceneController.getCurrentDateTime(),
                        publisher.getText(), genre.getText(), certRating.getText(), multiplayer.getText()));

                int copies = Integer.parseInt(numOfCopies.getText());
                getResourceManager().addBulkCopies(new Copy(0, getResourceManager().getLastAddedID(),
                        (int) loanDuration.getValue(), "", 0, 0), copies);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Resource Created Successfully!\nResource ID = \"\n"
                        + getResourceManager().getLastAddedID());
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Couldn't load an image.");
                alert.showAndWait();
                loadSubscene(getResourceInterface());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Not enough information.");
            alert.showAndWait();
        }
        loadSubscene(getResourceInterface());

    }

    /**
     * Assigns the thumbnail selected to the specific VideoGame.
     *
     * @param event Represents the data of the button pressed.
     */
    @FXML
    public void handleSetThumbnailButtonAction(ActionEvent event) {
        try {
            path = setThumbnailImage(event);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            //if image doesnt exist then add image url to database
            if (!getResourceManager().isImageExist(path)) {

                getResourceManager().addResourceImage(path);

            }
            System.out.println(path);
            Image image = new Image(path);
            thumbImage.setImage(image);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error in Database!");
            alert.showAndWait();
        }
    }

    /**
     * initialises the loan duration of the VideoGame to 14.
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loanDuration.getItems().addAll(1, 7, 14, 28);
        loanDuration.setValue(14);
    }
}
