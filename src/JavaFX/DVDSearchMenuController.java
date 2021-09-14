package JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.ArrayList;

/**
 * Interface controller for the DVD search menu.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class DVDSearchMenuController extends SceneController {

    /**
     * TextField for the title of the DVD.
     */
    @FXML
    private TextField dvdTitle;
    /**
     * TextField for the director of the DVD.
     */
    @FXML
    private TextField dvdDirector;
    /**
     * TextField for the year of release of the DVD.
     */
    @FXML
    private TextField dvdYear;

    /**
     * Executes a search from information specified.
     *
     * @param event Represents the data of the button pressed.
     */
    public void handleDVDSearchButton(ActionEvent event) {
        getInput();
        loadSubscene(getComputerListInterface());
    }

    /**
     * Reads the input for the query from text fields.
     */
    private void getInput() {
        ArrayList<String> column = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        if (!dvdTitle.getText().isEmpty()) {
            column.add("Title");
            input.add("'%" + dvdTitle.getText() + "%'");
        }
        if (!dvdDirector.getText().isEmpty()) {
            column.add("Director");
            input.add("'%" + dvdDirector.getText() + "%'");
        }
        if (!dvdYear.getText().isEmpty()) {
            column.add("RYear");
            input.add(dvdYear.getText());
        }

        if (!input.isEmpty()) {
            setSqlQuery(getResourceManager().createQuery(column.toArray(new String[0]),
                    input.toArray(new String[0]), "Dvd"));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid input.");
            alert.showAndWait();
        }
    }
}
