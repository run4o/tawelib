package JavaFX;

import Core.DateManager;
import Core.Event;
import Core.EventManager;
import static JavaFX.SceneController.USER_ID;
import static JavaFX.SceneController.getResourceFlowManager;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class for viewing past event reservations.
 *
 * @author Martin Trifonov
 * @version 1.0
 */
public class ViewPastEventReservationsController implements Initializable {

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
        Event[] events = em.getUserReservations(USER_ID);
        ObservableList<Event> contents
                = FXCollections.observableArrayList();
        for (Event event : events) {
            if (DateManager.hasDatePassed(new Date(event.getDate()))) {
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

}
