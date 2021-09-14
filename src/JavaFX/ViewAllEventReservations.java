package JavaFX;

import Core.EventManager;
import Core.User;
import static JavaFX.SceneController.getResourceFlowManager;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class for displaying all event reservations.
 *
 * @author Martin Trifinov
 * @version 1.0
 */
public class ViewAllEventReservations extends SceneController implements Initializable {

    /**
     * Table to display reservations.
     */
    @FXML
    TableView<User> table;

    /**
     * Initializes the controller class.
     *
     * @param url The location used to resolve relative paths for the root
     * object
     * @param rb The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int i = 0;
        EventManager em = getResourceFlowManager().getEventManager();
        User[] users = em.getAttendees(getEvent());

        ObservableList<User> contents
                = FXCollections.observableArrayList();

        contents.addAll(Arrays.asList(users));

        TableColumn userID = new TableColumn("ID");
        TableColumn firstName = new TableColumn("Name");
        TableColumn lastName = new TableColumn("Surname");

        table.getColumns()
                .addAll(userID, firstName, lastName);

        userID.setCellValueFactory(
                new PropertyValueFactory<User, Integer>("userID"));
        firstName.setCellValueFactory(
                new PropertyValueFactory<User, String>("firstName"));
        lastName.setCellValueFactory(
                new PropertyValueFactory<User, String>("lastName"));

        table.setItems(contents);
        table.setEditable(false);
    }

}
