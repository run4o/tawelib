package JavaFX;

import Core.Book;
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
public class EditBookController extends ResourceController implements Initializable {

    /**
     * book to be edited.
     */
    private final Book book = (Book) getRequestResource();
    /**
     * file path for the thumbnail image of the book.
     */
    private String path;
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
     * TextField for the year of release of the book.
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
        if (!book.getTitle().isEmpty()) {
            title.setText(book.getTitle());
        }
        if (!book.getAuthor().isEmpty()) {
            author.setText(book.getAuthor());
        }
        if (!book.getGenre().isEmpty()) {
            genre.setText(book.getGenre());
        }
        if (!book.getPublisher().isEmpty()) {
            publisher.setText(book.getPublisher());
        }
        if (book.getYear() != 0) {
            year.setText(String.valueOf(book.getYear()));
        }
        if (!book.getIsbn().isEmpty()) {
            isbn.setText(book.getIsbn());
        }
        if (!book.getLang().isEmpty()) {
            language.setText(book.getLang());
        }
        if (getResourceManager().getCopies(book.getResourceID()).length != 0) {
            numOfCopies.setText(String.valueOf(getResourceManager().getCopies(book.getResourceID()).length));
        } else {
            numOfCopies.setText("0");
        }
        if (book.getThumbImage() != 0) {
            try {
                thumbImage.setImage(new Image(getResourceManager().getImageURL(book.getThumbImage())));
                path = getResourceManager().getImageURL(book.getThumbImage());
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
                && !author.getText().isEmpty() && !publisher.getText().isEmpty()) {
            try {
                getResourceManager().editResource(new Book(book.getResourceID(), title.getText(),
                        Integer.parseInt(year.getText()), getResourceManager().getImageID(path), book.getdateCreated(), author.getText(),
                        publisher.getText(), genre.getText(), isbn.getText(), language.getText()));
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
     * Assigns the thumbnail selected to the specific book.
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
