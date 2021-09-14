package JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.ArrayList;

/**
 * Interface controller for the laptop search menu.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @version 1.0
 */
public class ComputerSearchMenuController extends SceneController {

    /**
     * TextField for the title of the laptop.
     */
    @FXML
    private TextField computerTitle;
    /**
     * TextField for the release year of the laptop.
     */
    @FXML
    private TextField computerYear;
    /**
     * TextField for the model of the laptop.
     */
    @FXML
    private TextField computerModel;
    /**
     * TextField for the manufacturer of the laptop.
     */
    @FXML
    private TextField computerManufacturer;
    /**
     * TextField for the installed OS of the laptop.
     */
    @FXML
    private TextField computerOS;

    /**
     * Executes a search from information specified.
     *
     * @param event the event triggered by clicking the button.
     */
    public void handleSearchComputerButtonAction(ActionEvent event) {
        getInput();
        loadSubscene(getComputerListInterface());
    }

    /**
     * Reads the input for the query from text fields.
     */
    private void getInput() {
        ArrayList<String> column = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        if (!computerTitle.getText().isEmpty()) {
            column.add("Title");
            input.add("'%" + computerTitle.getText() + "%'");
        }
        if (!computerYear.getText().isEmpty()) {
            column.add("RYear");
            input.add("'%" + computerYear.getText() + "%'");
        }
        if (!computerModel.getText().isEmpty()) {
            column.add("Model");
            input.add("'%" + computerModel.getText() + "%'");
        }
        if (!computerManufacturer.getText().isEmpty()) {
            column.add("Manufacturer");
            input.add("'%" + computerManufacturer.getText() + "%'");
        }
        if (!computerOS.getText().isEmpty()) {
            column.add("Installed_OS");
            input.add("'%" + computerOS.getText() + "%'");
        }

        if (!input.isEmpty()) {
            setSqlQuery(getResourceManager().createQuery(column.toArray(new String[0]),
                    input.toArray(new String[0]), "Computer"));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid input.");
            alert.showAndWait();
        }
    }
}
