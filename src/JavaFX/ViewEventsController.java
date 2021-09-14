package JavaFX;

import Core.DateManager;
import Core.Event;
import Core.EventManager;
import static JavaFX.SceneController.getResourceFlowManager;
import java.net.URL;
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
 * FXML Controller class for user interface of events.
 *
 * @author Martin Trifinov
 * @version 1.0
 */
public class ViewEventsController extends SceneController implements Initializable {

    /**
     * Error message for fully booked events.
     */
    @FXML
    private Label fullyBooked;

    /**
     * Error message for duplicate reservations.
     */
    @FXML
    private Label alreadyReserved;

    /**
     * Table to display reservations.
     */
    @FXML
    private TableView<Event> table;

    /**
     * Initializes the controller class.
     *
     * @param url The root location.
     * @param rb The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EventManager em = getResourceFlowManager().getEventManager();
        Event[] events = em.getEventList();
        ObservableList<Event> contents
                = FXCollections.observableArrayList();
        for (Event event : events) {
            if (!DateManager.hasDatePassed(new Date(event.getDate()))) {
                contents.add(event);
            }
        }

        // contents.addAll(Arrays.asList(events));
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

    /**
     * Handles on-click button action. Changes scene to upcoming events.
     */
    @FXML
    private void viewMyReservatinos() {
        loadSubscene(SceneController.getViewMyUpcomingReservations());
    }

    /**
     * Handles on-click button action. Changes scene to past events.
     */
    @FXML
    private void viewMyPastReservatinos() {
        loadSubscene(SceneController.getViewMyPastReservations());
    }

    /**
     * Handles on-click button action. Creates reservation for the selected
     * event.
     */
    @FXML
    private void reserve(ActionEvent event) {
        fullyBooked.setVisible(false);
        alreadyReserved.setVisible(false);
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                EventManager em = getResourceFlowManager().getEventManager();
                em.reserveSlot(USER_ID, selectedEvent);
                fullyBooked.setVisible(false);
                alreadyReserved.setVisible(false);
            } catch (Exception ex) {
                if (ex.getMessage().equals("Already Reserved")) {
                    alreadyReserved.setVisible(true);
                }
                if (ex.getMessage().equals("Fully Booked")) {
                    fullyBooked.setVisible(true);
                }
            }
        }
    }

}
