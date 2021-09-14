/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import static JavaFX.SceneController.getResourceFlowManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread, when ran, sends out email to all attendees to events this day.
 *
 * @author Martin Trifonov
 */
public class EventNotificationThread extends TimerTask {

    /**
     * Runs a check of all events, and sends notifications to all attending
     * users.
     */
    @Override
    public void run() {
        EventManager em = getResourceFlowManager().getEventManager();
        Event[] events = em.getEventList();
        for (Event event : events) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                Date date = df.parse(event.getDate());
                if (df.format(date).equals(df.format(new Date()))) {
                    sendReminder(em, event);
                }
            } catch (ParseException ex) {
                Logger.getLogger(EventNotificationThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Sends message to all users attending the event.
     * @param em Eventmanager for getting the needed data.
     * @param event Event that is happening today.
     */
    private void sendReminder(EventManager em, Event event) {
        User[] users = em.getAttendees(event);
        for (User user : users) {
            NotificationCenter nc = new NotificationCenter();
            nc.sendEventNotification(event, user.getFirstName(), user.getEmail());
        }
    }

}
