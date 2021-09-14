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
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class for adding events.
 *
 * @author Martin Trifonov
 * @version 1.0
 */
public class AddEventController extends SceneController implements Initializable {

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
     * Handles cancel button. Returns user to event management interface.
     */
    @FXML
    private void handleCancelButtonAction() {
        loadSubscene(SceneController.getEventManagementInterface());
    }

    /**
     * Handles save button. Tries to save event.
     */
    @FXML
    private void handleSaveButtonAction() {

        EventManager em = getResourceFlowManager().getEventManager();
        Event create = new Event();

        create.setEventID(0);
        create.setTitle(title.getText());
        create.setDescription(description.getText());

        if (Integer.valueOf(size.getText()) >= 1) {

            create.setSize(Integer.valueOf(size.getText()));
            create.setTime(time.getText());

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            LocalDate localDate = date.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date newDate = Date.from(instant);
            if (!DateManager.hasDatePassed(newDate)) {
                create.setDate(df.format(newDate));
                em.addEvent(create);
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
    }

}
