package JavaFX;

import Core.Computer;
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
 * Handles editing data of an existing laptop in the database.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class EditLaptopController extends ResourceController implements Initializable {

    /**
     * laptop to be edited.
     */
    private final Computer computer = (Computer) getRequestResource();
    /**
     * file path for the thumbnail image of the laptop.
     */
    private String path;
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
     * TextField for the year of release of the laptop.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the model of the laptop.
     */
    @FXML
    private TextField model;
    /**
     * TextField for the installed OS of the laptop.
     */
    @FXML
    private TextField os;
    /**
     * TextField for the number of copies of the laptop.
     */
    @FXML
    private TextField numOfCopies;
    /**
     * thumbnail image displayed on the select thumbnail button.
     */
    @FXML
    private ImageView thumbnailImg;

    /**
     * handles the general retrieval and editing of laptop description.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!computer.getTitle().isEmpty()) {
            title.setText(computer.getTitle());
        }
        if (!computer.getManufacturer().isEmpty()) {
            manufacturer.setText(computer.getManufacturer());
        }
        if (computer.getYear() != 0) {
            year.setText(String.valueOf(computer.getYear()));
        }
        if (!computer.getModel().isEmpty()) {
            model.setText(computer.getModel());
        }
        if (!computer.getOs().isEmpty()) {
            os.setText(computer.getOs());
        }
        if (getResourceManager().getCopies(computer.getResourceID()).length != 0) {
            numOfCopies.setText(String.valueOf(getResourceManager().getCopies(computer.getResourceID()).length));
        } else {
            numOfCopies.setText("0");
        }
        if (computer.getThumbImage() != 0) {
            try {
                thumbnailImg.setImage(new Image(getResourceManager().getImageURL(computer.getThumbImage())));
                path = getResourceManager().getImageURL(computer.getThumbImage());
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
                && !model.getText().isEmpty() && !manufacturer.getText().isEmpty() && !os.getText().isEmpty()) {
            try {
                getResourceManager().editResource(new Computer(computer.getResourceID(), title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), computer.getdateCreated(), manufacturer.getText(),
                        model.getText(), os.getText()));
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
     * Assigns the thumbnail selected to the specific laptop.
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
