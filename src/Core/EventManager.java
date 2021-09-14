package Core;

import JavaFX.SceneController;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Manager for event based functionality.
 *
 * @author Martin Trifinov
 * @version 1.0
 */
public class EventManager {

    /**
     * An instance of DatabaseManager.
     */
    private final DatabaseManager dbManager;

    /**
     * Creates an instance of the manager.
     *
     * @param dbManager reference to database manager.
     */
    public EventManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Creates an Array of all Events.
     *
     * @return Array of Events.
     */
    public Event[] getEventList() {

        Event[] events;

        try {
            String[][] data = dbManager.getTupleList("Event");

            events = new Event[data.length];

            for (int i = 0; i < data.length; i++) {
                events[i] = new Event();
                events[i].setEventID(Integer.valueOf(data[i][0]));
                events[i].setSize(Integer.parseInt(data[i][1]));
                events[i].setTitle(data[i][2]);
                events[i].setDescription(data[i][3]);
                events[i].setDate(data[i][4]);
                events[i].setTime(data[i][5]);

            }

        } catch (SQLException ex) {
            System.out.print("SQL ERROR");
            events = new Event[0];
        }
        return events;
    }

    /**
     * Returns a specific event.
     *
     * @param n ID of the event
     * @return Event with the specified id.
     */
    public Event getEvent(int n) {
        try {
            String sql = "Select * From Event Where EventID=" + n;
            String[][] data = dbManager.getTupleListByQuery(sql);

            if (data.length > 0) {
                Event event = new Event();
                event.setEventID(Integer.valueOf(data[0][0]));
                event.setSize(Integer.parseInt(data[0][1]));
                event.setTitle(data[0][2]);
                event.setDescription(data[0][3]);
                event.setDate(data[0][4]);
                event.setTime(data[0][5]);
                return event;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Saves the specified event to the database.
     *
     * @param event event to be saved.
     */
    public void addEvent(Event event) {
        try {
            dbManager.addTuple("Event", new String[]{
                null,
                String.valueOf(event.getSize()),
                "\"" + event.getTitle() + "\"",
                "\"" + event.getDescription() + "\"",
                "\"" + event.getDate() + "\"",
                "\"" + event.getTime() + "\""
            });
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns how many spaces have been reserved for the specified event.
     *
     * @param event event to check.
     * @return number of reservations.
     */
    public int getReservedSlots(Event event) {

        String sql = "SELECT COUNT(*) From EventReservation Where EventID=" + event.getEventID();

        int taken = 0;
        try {
            dbManager.sqlQuery(sql);
            taken = Integer.valueOf(dbManager.getFirstTupleByQuery(sql)[0]);

        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return taken;
    }

    /**
     * Returns how many free slots the specified event has.
     *
     * @param event event to check.
     * @return number of free slots.
     */
    public int getFreeSlots(Event event) {
        String sql = "SELECT COUNT(*) From EventReservation Where EventID=" + event.getEventID();

        int taken = 0;
        try {
            dbManager.sqlQuery(sql);
            taken = event.getSize() - Integer.valueOf(dbManager.getFirstTupleByQuery(sql)[0]);

        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return taken;
    }

    /**
     * Returns true if even has free slots.
     *
     * @param event specified event.
     * @return true if event has free slots.
     */
    public boolean hasFreeSlots(Event event) {
        String sql = "SELECT COUNT(*) From EventReservation Where EventID=" + event.getEventID();

        int taken = 0;
        try {
            dbManager.sqlQuery(sql);
            taken = Integer.valueOf(dbManager.getFirstTupleByQuery(sql)[0]);

        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return event.getSize() - taken > 0;
    }

    /**
     * Checks if user has reserved a slot for the event.
     *
     * @param user user to check for.
     * @param event event to check in.
     * @return True if user has reservation.
     */
    public boolean checkIfAlreadyReserved(int user, Event event) {
        String sql = "SELECT * From EventReservation Where EventID="
                + event.getEventID() + " And UID=" + user;

        int result = 0;
        try {
            dbManager.sqlQuery(sql);
            result = dbManager.getFirstTupleByQuery(sql).length;

        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result != 0;

    }

    /**
     * Creates a reservations for the user in the specified event.
     *
     * @param user user to reserve for.
     * @param event event to reserve.
     * @throws Exception Event fully booked or user already reserved.
     */
    public void reserveSlot(int user, Event event) throws Exception {
        if (checkIfAlreadyReserved(user, event)) {
            throw new Exception("Already Reserved");
        } else if (!hasFreeSlots(event)) {
            throw new Exception("Fully Booked");
        } else {
            try {
                dbManager.addTuple("EventReservation", new String[]{
                    null,
                    String.valueOf(user),
                    String.valueOf(event.getEventID())
                });

                NotificationCenter nc = new NotificationCenter();
                String[][] data = dbManager.getTupleListByQuery("Select First_Name,Email From User Where UID=" + user);
                nc.sendEventReservation(event, data[0][0], data[0][1]);
            } catch (SQLException ex) {
                Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns Event a user is register for.
     *
     * @param user specified user.
     * @return list of events user is attending.
     */
    public Event[] getUserReservations(int user) {
        String sql = "SELECT EventID From EventReservation Where UID =" + user;
        String[][] eventIDs;
        Event[] events = null;
        try {
            dbManager.sqlQuery(sql);
            eventIDs = dbManager.getTupleListByQuery(sql);
            events = new Event[eventIDs.length];
            for (int i = 0; i < eventIDs.length; i++) {
                sql = "Select * From Event Where EventID = " + eventIDs[i][0];
                String[][] data = dbManager.getTupleListByQuery(sql);
                events[i] = new Event();
                events[i].setEventID(Integer.valueOf(data[0][0]));
                events[i].setSize(Integer.parseInt(data[0][1]));
                events[i].setTitle(data[0][2]);
                events[i].setDescription(data[0][3]);
                events[i].setDate(data[0][4]);
                events[i].setTime(data[0][5]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return events;

    }

    /**
     * Updates the information for the specified event.
     *
     * @param event updated event.
     */
    public void editEvent(Event event) {
        try {
            String sql = "UPDATE Event SET ";
            sql += "Size = " + event.getSize() + ", ";
            sql += "Title = \"" + event.getTitle() + "\", ";
            sql += "Description = \"" + event.getDescription() + "\", ";
            sql += "Date = \"" + event.getDate() + "\", ";
            sql += "Time = \"" + event.getTime() + "\" ";
            sql += "Where EventID =" + event.getEventID();

            dbManager.sqlQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Removes user reservation.
     *
     * @param user User to remove reservation.
     * @param event Event to remove reservation.
     */
    public void removeReservation(int user, Event event) {

        try {
            String sql = "Delete From EventReservation ";
            sql += "Where UID= " + user + " AND ";
            sql += "EventID= " + event.getEventID();
            dbManager.sqlQuery(sql);

            NotificationCenter nc = new NotificationCenter();
            String[][] data = dbManager.getTupleListByQuery("Select First_Name,Email From User Where UID=" + user);
            nc.sendEventReservationCanceled(event, data[0][0], data[0][1]);
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Removes event.
     *
     * @param event Event to be removed.
     */
    public void removeEvent(Event event) {
        try {
            NotificationCenter nc = new NotificationCenter();
            User[] users = this.getAttendees(event);
            for (User user : users) {
                nc.sendEventCancelation(event, user.getFirstName(), user.getEmail());
            }
            String sql = "Delete From Event ";
            sql += "Where EventID= " + event.getEventID();
            dbManager.sqlQuery(sql);
            removeEventReservations(event);
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes reservations made for the event.
     */
    private void removeEventReservations(Event event) {
        try {
            String sql = "Delete From EventReservation ";
            sql += "Where EventID = " + event.getEventID();
            dbManager.sqlQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns Users who have reserved for the event.
     *
     * @param event specified event.
     * @return Array of users.
     */
    public User[] getAttendees(Event event) {
        User[] users = null;
        try {

            String sql = "Select UID From EventReservation Where EventID ="
                    + event.getEventID();
            String[][] data = dbManager.getTupleListByQuery(sql);
            users = new User[data.length];
            for (int i = 0; i < data.length; i++) {
                int id = Integer.valueOf(data[i][0]);
                sql = "Select * From User Where UID =" + id;
                String[][] userData = dbManager.getTupleListByQuery(sql);
                users[i] = new User(id, userData[0][1], userData[0][2], "", "", "", "", "", "", null, 0, 0, userData[0][17]);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }
}
