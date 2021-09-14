package JavaFX;

import Core.Dvd;
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
 * Handles editing data of an existing book in the database.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class EditDVDController extends ResourceController implements Initializable {

    /**
     * DVD to be edited.
     */
    private final Dvd dvd = (Dvd) getRequestResource();
    /**
     * file path for the thumbnail image of the DVD.
     */
    private String path;
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
     * TextField for the year of release of the DVD.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the runtime of the DVD.
     */
    @FXML
    private TextField runtime;
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
     * retrieves the needed resource and handles the editing.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!dvd.getTitle().isEmpty()) {
            title.setText(dvd.getTitle());
        }
        if (!dvd.getDirector().isEmpty()) {
            director.setText(dvd.getDirector());
        }
        if (dvd.getSubLang().length != 0) {
            String subtitleLang = "";
            for (int i = 0; i < dvd.getSubLang().length - 1; i++) {
                subtitleLang += dvd.getSubLang()[i] + ", ";
            }
            subtitleLang += dvd.getSubLang()[dvd.getSubLang().length - 1];
            subtitle.setText(subtitleLang);
        }
        if (dvd.getRunTime() != 0) {
            runtime.setText(String.valueOf(dvd.getRunTime()));
        }
        if (dvd.getYear() != 0) {
            year.setText(String.valueOf(dvd.getYear()));
        }
        if (!dvd.getLanguage().isEmpty()) {
            language.setText(dvd.getLanguage());
        }
        if (getResourceManager().getCopies(dvd.getResourceID()).length != 0) {
            numOfCopies.setText(String.valueOf(getResourceManager().getCopies(dvd.getResourceID()).length));
        } else {
            numOfCopies.setText("0");
        }
        if (dvd.getThumbImage() != 0) {
            try {
                thumbImage.setImage(new Image(getResourceManager().getImageURL(dvd.getThumbImage())));
                path = getResourceManager().getImageURL(dvd.getThumbImage());
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
        if (!title.getText().isEmpty() && !year.getText().isEmpty() && !thumbImage.equals(null)
                && !director.getText().isEmpty() && !runtime.getText().isEmpty()) {
            try {
                String[] subLang = subtitle.getText().replaceAll(" ", "").split(",");
                getResourceManager().editResource(new Dvd(dvd.getResourceID(), title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), dvd.getdateCreated(), director.getText(),
                        Integer.parseInt(runtime.getText()), language.getText(), subLang));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Changes Saved Successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Couldn't load an image.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        thumbImage.setImage(new Image(path));
    }
}
