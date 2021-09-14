package JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * Interface controller for the VideoGame search menu.
 *
 * @author Rory Richards
 * @version 1.0
 */
public class VideoGameSearchMenuController extends SceneController {

    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField title;
    /**
     * TextField for the release year of the VideoGame to be searched for.
     */
    @FXML
    private TextField year;
    /**
     * TextField for the title of the VideoGame.
     */
    @FXML
    private TextField publisher;

    /**
     * TextField for the director of the VideoGame.
     */
    @FXML
    private TextField genre;

    /**
     * TextField for the year of release of the VideoGame.
     */
    @FXML
    private TextField certRating;

    /**
     * TextField for the year of release of the VideoGame.
     */
    @FXML
    private TextField multiplayer;

    /**
     * Executes a search from information specified.
     *
     * @param event Represents the data of the button pressed.
     */
    public void handleVideoGameSearchButton(ActionEvent event) {
        getInput();
        loadSubscene(getVideoGameListInterface());
    }

    /**
     * Reads the input for the query from text fields.
     */
    private void getInput() {
        ArrayList<String> column = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        if (!title.getText().isEmpty()) {
            column.add("Title");
            input.add("'%" + title.getText() + "%'");
        }
        if (!year.getText().isEmpty()) {
            column.add("RYear");
            input.add(year.getText());
        }
        if (!publisher.getText().isEmpty()) {
            column.add("Publisher");
            input.add("'%" + publisher.getText() + "%'");
        }
        if (!genre.getText().isEmpty()) {
            column.add("Genre");
            input.add(genre.getText());
        }
        if (!certRating.getText().isEmpty()) {
            column.add("CertRating");
            input.add(certRating.getText());
        }
        if (!multiplayer.getText().isEmpty()) {
            column.add("Multiplayer");
            input.add(multiplayer.getText());
        }

        if (!input.isEmpty()) {
            setSqlQuery(getResourceManager().createQuery(column.toArray(new String[0]),
                    input.toArray(new String[0]), "VideoGame"));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid input.");
            alert.showAndWait();
        }
    }
}
