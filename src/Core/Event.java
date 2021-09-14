package Core;

/**
 * Represents an Event in the library. Holds all the information about the
 * event.
 *
 * @author Martin Trifinov
 * @version 1.0
 */
public class Event {

    /**
     * Unique event id.
     */
    private int eventID;

    /**
     * Size of the event.
     */
    private int size;

    /**
     * Date of the event.
     */
    private String date;

    /**
     * Time of the event.
     */
    private String time;

    /**
     * Title of the event.
     */
    private String title;

    /**
     * Brief description of the event.
     */
    private String description;

    /**
     * Construct an Event with the specified values.
     *
     * @param eventID Event Id.
     * @param size Event max size.
     * @param date Date of the event.
     * @param time Time of the event.
     * @param title Title of the event.
     * @param description description of the event.
     */
    public Event(int eventID, int size, String date,
            String time, String title, String description) {
        this.eventID = eventID;
        this.size = size;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }

    /**
     * Construct an Event with the specified values.
     *
     * @param size Event max size.
     * @param date Date of the event.
     * @param time Time of the event.
     * @param title Title of the event.
     * @param description description of the event.
     */
    public Event(int size, String date,
            String time, String title, String description) {
        this.eventID = 0;
        this.size = size;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }

    /**
     * Construct an empty Event.
     *
     */
    public Event() {
        eventID = 0;
    }

    /**
     * Return the event id.
     *
     * @return integer as event id.
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Sets the event id.
     *
     * @param eventID New Event Id.
     */
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    /**
     * Returns the Event description.
     *
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets new Event Description.
     *
     * @param description new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the maximum attendees the event can have.
     *
     * @return the maximum size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets new event size.
     *
     * @param atendees new event size.
     */
    public void setSize(int atendees) {
        this.size = atendees;
    }

    /**
     * Returns the date of the event.
     *
     * @return date of the event.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets new date for the event.
     *
     * @param date new date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the title of the event.
     *
     * @return event title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets new event title.
     *
     * @param title new event title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the time of the event.
     *
     * @return time of the event.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets new time for the event.
     *
     * @param time the new time.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Converts Event object to string.
     *
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return "Event{" + "eventID="
                + eventID + ", size="
                + size + ", date="
                + date + ", time="
                + time + ", title="
                + title + ", description="
                + description + '}';
    }
}
