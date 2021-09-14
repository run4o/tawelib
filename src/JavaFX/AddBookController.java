package JavaFX;

import Core.Book;
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
 * Handles adding data of a book into the database.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class AddBookController extends ResourceController implements Initializable {

    /**
     * file path for the thumbnail of the book.
     */
    private String path;
    /**
     * ChoiceBox to select the loan duration for the book
     */
    @FXML
    private ChoiceBox<Integer> loanDuration;
    /**
     * TextField for the title of the book.
     */
    @FXML
    private TextField title;
    /**
     * TextField for the author of the book.
     */
    @FXML
    private TextField author;
    /**
     * TextField for the genre of the book.
     */
    @FXML
    private TextField genre;
    /**
     * TextField for the publisher of the book.
     */
    @FXML
    private TextField publisher;
    /**
     * TextField for the year of the book.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the ISBN of the book.
     */
    @FXML
    private TextField isbn;
    /**
     * TextField for the language of the book.
     */
    @FXML
    private TextField language;
    /**
     * TextField for the number of copies of the book.
     */
    @FXML
    private TextField numOfCopies;
    /**
     * thumbnail image displayed on the select thumbnail button.
     */
    @FXML
    private ImageView thumbnail;

    /**
     * creates a new book resource.
     *
     * @param event the event triggered by clicking the button.
     */
    @FXML
    public void handleCreateResourceButtonAction(ActionEvent event) {
        if (!title.getText().isEmpty() && !year.getText().isEmpty() && !author.getText().isEmpty()
                && !publisher.getText().isEmpty()) {
            try {
                Book book = new Book(0, title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), SceneController.getCurrentDateTime(), author.getText(),
                        publisher.getText(), genre.getText(), isbn.getText(), language.getText());
                getResourceManager().addResource(book);

                int copies = Integer.parseInt(numOfCopies.getText());
                getResourceManager().addBulkCopies(new Copy(0, getResourceManager().getLastAddedID(),
                        loanDuration.getValue(), "", 0, 0), copies);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Resource Created Successfully!\nResource ID = \"\n"
                        + +getResourceManager().getLastAddedID());
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Couldn't load an image");
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
     * sets the thumbnail image on the set thumbnail button.
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
            Image image = new Image(getResourceManager().getImageURL(getResourceManager().getImageID(path)));
            thumbnail.setImage(image);

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
