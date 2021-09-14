package JavaFX;

import Core.Book;
import Core.Computer;
import Core.Dvd;
import Core.VideoGame;
import Core.Resource;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Handles the retrieval of resources for users.
 *
 * @author Noah Lenagan
 * @author Rory Richards (V2.0)
 * @version 2.0
 */
public class RequestResourceByUserController extends SceneController implements Initializable {

    /**
     * Button for showing trailer.
     */
    @FXML
    private Button trailerButton;

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
     * Handles the requesting of a resource for the button.
     */
    @FXML
    public void handleRequestResourceButtonAction() {
        try {
            getResourceFlowManager().requestResource(getRequestResource().getResourceID(), SceneController.USER_ID);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Requested successfully!");
            alert.showAndWait();
        } catch (SQLException | IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * When the user presses the review button.
     */
    @FXML
    public void goToReviews() {
        loadSubscene(getReviewsInterface());
    }

    /**
     * When the user presses the leave review button.
     */
    @FXML
    public void goToLeaveReview() {

        Resource res = getRequestResource();

        //If user has not borrowed/is currently borrowing book, don't allow
        //them to leave a review.
        try {
            boolean hasUserBorrowed = getResourceFlowManager().hasUserBorrowed(SceneController.USER_ID, res.getResourceID());

            if (hasUserBorrowed) {
                loadSubscene(getLeaveReviewInterface());
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Cannot leave review as you have not borrowed/are currently borrowing said resource.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

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
        setResourceDesc();
    }

    /**
     * Handles display of the details for the requested resource.
     */
    public void setResourceDesc() {
        int rowCounter = 0;
        if (getRequestResource() instanceof Book) {
            trailerButton.setVisible(false);
            Book book = (Book) getRequestResource();
            if (!book.getAuthor().isEmpty()) {
                Label author = new Label("Author:\t " + book.getAuthor());
                descriptionPane.addRow(rowCounter, author);
                rowCounter++;
            }
            if (!book.getGenre().isEmpty()) {
                Label genre = new Label("Genre:\t " + book.getGenre());
                descriptionPane.addRow(rowCounter, genre);
                rowCounter++;
            }
            if (!book.getPublisher().isEmpty()) {
                Label publisher = new Label("Publisher:\t " + book.getPublisher());
                descriptionPane.addRow(rowCounter, publisher);
                rowCounter++;
            }
            if (book.getYear() != 0) {
                Label year = new Label("Year:\t\t " + book.getYear());
                descriptionPane.addRow(rowCounter, year);
                rowCounter++;
            }
            if (!book.getIsbn().isEmpty()) {
                Label isbn = new Label("ISBN:\t " + book.getIsbn());
                descriptionPane.addRow(rowCounter, isbn);
                rowCounter++;
            }
            if (!book.getLang().isEmpty()) {
                Label language = new Label("Language: " + book.getLang());
                descriptionPane.addRow(rowCounter, language);
                rowCounter++;
            }
        }

        if (getRequestResource() instanceof Dvd) {
            Dvd dvd = (Dvd) getRequestResource();
            if (!dvd.getDirector().isEmpty()) {
                Label director = new Label("Director: " + dvd.getDirector());
                descriptionPane.addRow(rowCounter, director);
                rowCounter++;
            }
            if (dvd.getYear() != 0) {
                Label year = new Label("Year:\t\t " + dvd.getYear());
                descriptionPane.addRow(rowCounter, year);
                rowCounter++;
            }
            if (!dvd.getLanguage().isEmpty()) {
                Label language = new Label("Language: " + dvd.getLanguage());
                descriptionPane.addRow(rowCounter, language);
                rowCounter++;
            }
            if (dvd.getRunTime() != 0) {
                Label runtime = new Label("Year:\t\t " + dvd.getRunTime());
                descriptionPane.addRow(rowCounter, runtime);
                rowCounter++;
            }
            if (dvd.getSubLang().length != 0) {
                String subLangText = "Subtitle:\t";
                for (String element : dvd.getSubLang()) {
                    subLangText += element + "\n\t\t";
                }
                Label subLang = new Label(subLangText);
                descriptionPane.addRow(rowCounter, subLang);
                rowCounter++;
            }
        }

        if (getRequestResource() instanceof Computer) {
            trailerButton.setVisible(false);
            Computer computer = (Computer) getRequestResource();
            if (!computer.getModel().isEmpty()) {
                Label model = new Label("Model:\t " + computer.getModel());
                descriptionPane.addRow(rowCounter, model);
                rowCounter++;
            }
            if (computer.getYear() != 0) {
                Label year = new Label("Year:\t\t " + computer.getYear());
                descriptionPane.addRow(rowCounter, year);
                rowCounter++;
            }
            if (!computer.getManufacturer().isEmpty()) {
                Label manufacturer = new Label("Manufacturer:\t " + computer.getManufacturer());
                descriptionPane.addRow(rowCounter, manufacturer);
                rowCounter++;
            }
            if (!computer.getOs().isEmpty()) {
                Label os = new Label("System:\t " + computer.getOs());
                descriptionPane.addRow(rowCounter, os);
                rowCounter++;
            }
        }

        if (getRequestResource() instanceof VideoGame) {
            VideoGame videoGame = (VideoGame) getRequestResource();
            if (!videoGame.getPublisher().isEmpty()) {
                Label model = new Label("Publisher:\t " + videoGame.getPublisher());
                descriptionPane.addRow(rowCounter, model);
                rowCounter++;
            }
            if (!videoGame.getGenre().isEmpty()) {
                Label year = new Label("Genre:\t\t " + videoGame.getGenre());
                descriptionPane.addRow(rowCounter, year);
                rowCounter++;
            }
            if (!videoGame.getCertRating().isEmpty()) {
                Label manufacturer = new Label("Certificate Rating:\t " + videoGame.getCertRating());
                descriptionPane.addRow(rowCounter, manufacturer);
                rowCounter++;
            }
            if (!videoGame.getMultiplayer().isEmpty()) {
                Label os = new Label("Multiplayer:\t " + videoGame.getMultiplayer());
                descriptionPane.addRow(rowCounter, os);
                rowCounter++;
            }
        }

        descriptionPane.setVgap(5);
        descriptionPane.getRowConstraints().setAll(new RowConstraints(20));
    }

    /**
     * Creates a pop-up window with the a trailer for the resource.
     */
    @FXML
    private void openTrailer() {
        Stage trailer = new Stage();
        trailer.initModality(Modality.APPLICATION_MODAL);
        trailer.initOwner(resourceImg.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane pane = fxmlLoader.load(getClass().getResource(SceneController.getTrailers()).openStream());
            trailer.setScene(new Scene(pane));
        } catch (IOException e) {
            System.out.println("Couldn't set scene.");
        }
        trailer.show();
        trailer.setOnCloseRequest((WindowEvent e) -> {
            TrailerPageController controller = fxmlLoader.getController();
            controller.exit();
        });
    }
}
