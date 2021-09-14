package JavaFX;

import Core.DateManager;
import Core.Event;
import Core.EventManager;
import static JavaFX.SceneController.getResourceFlowManager;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class for editing event.
 *
 * @author Martin Trifonov
 * @version 1.1
 */
public class EditEventController extends SceneController implements Initializable {

    /**
     * Error massage for negative event size.
     */
    @FXML
    private Label badSize;

    /**
     * Error massage for bad event date.
     */
    @FXML
    private Label badDate;

    /**
     * DatePicker.
     */
    @FXML
    private DatePicker date;

    /**
     * Event description text field.
     */
    @FXML
    private TextField description;

    /**
     * Event size text field.
     */
    @FXML
    private TextField size;

    /**
     * Event time text field.
     */
    @FXML
    private TextField time;

    /**
     * Event title text field.
     */
    @FXML
    private TextField title;

    /**
     * Handles cancel button. Returns to event interface.
     */
    @FXML
    private void handleCancelButtonAction() {
        loadSubscene(SceneController.getEventManagementInterface());
    }

    /**
     * Handles save button. Tries to create event.
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        EventManager em = getResourceFlowManager().getEventManager();
        Event edited = new Event();

        edited.setEventID(getEvent().getEventID());
        edited.setTitle(title.getText());
        edited.setDescription(description.getText());

        if (em.getReservedSlots(getEvent()) <= Integer.valueOf(size.getText())) {

            edited.setSize(Integer.valueOf(size.getText()));
            edited.setTime(time.getText());

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            LocalDate localDate = date.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date newDate = Date.from(instant);
            if (!DateManager.hasDatePassed(newDate)) {
                edited.setDate(df.format(newDate));
                em.editEvent(edited);
                loadSubscene(SceneController.getEventManagementInterface());
            } else {
                badDate.setVisible(true);
            }

        } else {
            badSize.setVisible(true);
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url The root location.
     * @param rb The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (getEvent() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate dt = LocalDate.parse(getEvent().getDate(), formatter);

            date.setValue(dt);
            description.setText(getEvent().getDescription());
            size.setText(String.valueOf(getEvent().getSize()));
            time.setText(getEvent().getTime());
            title.setText(getEvent().getTitle());
        }
    }
}
