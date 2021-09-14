package JavaFX;

import Core.Copy;
import Core.Dvd;
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
 * Handles adding data of a DVD into the database.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class AddDVDController extends ResourceController implements Initializable {

    /**
     * file path for the thumbnail of the DVD.
     */
    private String path;
    /**
     * ChoiceBox to select the loan duration of the DVD.
     */
    @FXML
    private ChoiceBox loanDuration;
    /**
     * TextField for the title of the DVD.
     */
    @FXML
    private TextField title;
    /**
     * TextField for the director of the DVD.
     */
    @FXML
    private TextField director;
    /**
     * TextField for the release year of the DVD.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the language of the DVD.
     */
    @FXML
    private TextField language;
    /**
     * TextField for the subtitle language of the DVD.
     */
    @FXML
    private TextField subtitle;
    /**
     * TextField for the runtime of the DVD.
     */
    @FXML
    private TextField runtime;
    /**
     * TextField for the number of copies of the DVD.
     */
    @FXML
    private TextField numOfCopies;
    /**
     * thumbnail image displayed on the select thumbnail button.
     */
    @FXML
    private ImageView thumbImage;

    /**
     * creates a new DVD resource.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleCreateResourceButtonAction(ActionEvent event) {
        if (!title.getText().isEmpty() && !year.getText().isEmpty() && !director.getText().isEmpty()
                && !runtime.getText().isEmpty()) {
            try {
                String[] subLang = subtitle.getText().replaceAll(" ", "").split(",");
                getResourceManager().addResource(new Dvd(0, title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), SceneController.getCurrentDateTime(), director.getText(),
                        Integer.parseInt(runtime.getText()), language.getText(), subLang));

                int copies = Integer.parseInt(numOfCopies.getText());
                getResourceManager().addBulkCopies(new Copy(0, getResourceManager().getLastAddedID(),
                        (int) loanDuration.getValue(), "", 0, 0), copies);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Resource Created Successfully!\nResource ID = "
                        + +getResourceManager().getLastAddedID());
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
            loadSubscene(getResourceInterface());
        }
        loadSubscene(getResourceInterface());
    }

    /**
     * Assigns the thumbnail selected to the specific DVD.
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

        try {
            //if image doesnt exist then add image url to database
            if (!getResourceManager().isImageExist(path)) {

                getResourceManager().addResourceImage(path);

            }
            Image image = new Image(path);
            thumbImage.setImage(image);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error in Database!");
            alert.showAndWait();
        }
    }

    /**
     * initialises the loan duration of the book to 14.
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
