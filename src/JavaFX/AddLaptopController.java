package JavaFX;

import Core.Computer;
import Core.Copy;
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
public class AddLaptopController extends ResourceController implements Initializable {

    /**
     * file path for the thumbnail of the laptop.
     */
    private String path;
    /**
     * ChoiceBox to select the duration of the loan on the laptop.
     */
    @FXML
    private ChoiceBox loanDuration;
    /**
     * TextField for the title of the laptop.
     */
    @FXML
    private TextField title;
    /**
     * TextField for the manufacturer of the laptop.
     */
    @FXML
    private TextField manufacturer;
    /**
     * TextField for the release year of the laptop.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the model of the laptop.
     */
    @FXML
    private TextField model;
    /**
     * TextField for the OS installed on the laptop.
     */
    @FXML
    private TextField installedOS;
    /**
     * TextField for the number of copies of the laptop.
     */
    @FXML
    private TextField numOfCopies;
    /**
     * thumbnail image on the select thumbnail button.
     */
    @FXML
    private ImageView thumbImage;

    /**
     * creates a new laptop resource.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleCreateResourceButtonAction(ActionEvent event) {
        if (!title.getText().isEmpty() && !year.getText().isEmpty() && !manufacturer.getText().isEmpty()
                && !model.getText().isEmpty() && !installedOS.getText().isEmpty()) {
            try {
                getResourceManager().addResource(new Computer(0, title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), SceneController.getCurrentDateTime(),
                        manufacturer.getText(), model.getText(), installedOS.getText()));

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
     * Assigns the thumbnail selected to the specific DVD.
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
