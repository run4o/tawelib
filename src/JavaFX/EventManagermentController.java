package JavaFX;

import Core.DateManager;
import Core.Event;
import Core.EventManager;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class for librarian event interface.
 *
 * @author Martin Trifinov.
 * @version 1.0
 */
public class EventManagermentController extends SceneController implements Initializable {

    /**
     * Table for displaying events.
     */
    @FXML
    private TableView<Event> table;

    /**
     * Error message Label.
     */
    @FXML
    private Label pastEvent;

    /**
     * Handles on-click action on create button. Loads scene for creating new
     * events.
     */
    @FXML
    private void create() {
        loadSubscene(SceneController.getEventAdd());
    }

    /**
     * Handles on-click action on delete button. Tries to delete selected event.
     */
    @FXML
    private void delete(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        pastEvent.setVisible(false);
        if (DateManager.hasDatePassed(new Date(selectedEvent.getDate()))) {
            pastEvent.setVisible(true);
        } else {
            EventManager em = getResourceFlowManager().getEventManager();
            em.removeEvent(selectedEvent);
            loadSubscene(SceneController.getEventManagementInterface());
        }
    }

    /**
     * Handles on-click action on edit button. Tries to edit selected event and
     * loads editing scene.
     */
    @FXML
    private void edit(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        pastEvent.setVisible(false);
        if (selectedEvent != null) {
            if (DateManager.hasDatePassed(new Date(selectedEvent.getDate()))) {
                pastEvent.setVisible(true);
            } else {
                setEvent(selectedEvent);
                loadSubscene(SceneController.getEventEdit());
            }
        }
    }

    /**
     * Handles on-click action on reservations button. Changes scene to
     * reservation list.
     */
    @FXML
    private void reservation(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            setEvent(selectedEvent);
            loadSubscene(SceneController.getEventAttendees());
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url The location used to resolve relative paths for the root
     * object
     * @param rb The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTable();
    }

    /**
     * Loads events into the table.
     */
    /**
     * Loads and setups all events into the table
     */
    private void loadTable() {
        EventManager em = getResourceFlowManager().getEventManager();
        Event[] events = em.getEventList();

        ObservableList<Event> contents
                = FXCollections.observableArrayList();

        contents.addAll(Arrays.asList(events));

        TableColumn title = new TableColumn("Title");
        TableColumn description = new TableColumn("Description");
        TableColumn time = new TableColumn("Time");
        TableColumn date = new TableColumn("Date");
        TableColumn size = new TableColumn("Size");

        table.getColumns()
                .addAll(title, description, time, date, size);

        title.setCellValueFactory(
                new PropertyValueFactory<Event, String>("title"));
        description.setCellValueFactory(
                new PropertyValueFactory<Event, String>("description"));
        time.setCellValueFactory(
                new PropertyValueFactory<Event, String>("time"));
        date.setCellValueFactory(
                new PropertyValueFactory<Event, String>("date"));
        size.setCellValueFactory(
                new PropertyValueFactory<Event, Integer>("size"));

        table.setItems(contents);
        table.setEditable(false);
    }
}
