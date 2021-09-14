package Core;

/**
 * Contains the data regarding the borrow and return of a specific copy by a
 * user.
 *
 * @author Noah Lenagam.
 * @version 1.0
 */
public class LoanEvent {

    /**
     * The user id of a user.
     */
    private final int userID;

    /**
     * The copy id of a copy.
     */
    private final int copyID;

    /**
     * The date of the borrow of the copy.
     */
    private final String dateOut;

    /**
     * The time of the borrow of the copy.
     */
    private final String timeOut;

    /**
     * The date of the return of the copy.
     */
    private final String dateIn;

    /**
     * The time of the return of the copy.
     */
    private final String timeIn;

    /**
     * Constructs the BorrowEvent.
     *
     * @param userID The user id of a user.
     * @param copyID The copy id of a copy.
     * @param dateOut The date of the borrow of the copy.
     * @param dateIn The date of the return of the copy.
     * @param timeIn The time of the borrow of the copy.
     * @param timeOut The time of the return of the copy.
     */
    public LoanEvent(int userID, int copyID, String dateOut, String dateIn, String timeOut, String timeIn) {

        this.userID = userID;
        this.copyID = copyID;
        this.dateOut = dateOut;
        this.dateIn = dateIn;
        this.timeOut = timeOut;
        this.timeIn = timeIn;

    }

    /**
     * Gets the user id of a user.
     *
     * @return The user id of a user.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Gets the copy id of a copy.
     *
     * @return The copy id of a copy.
     */
    public int getCopyID() {
        return copyID;
    }

    /**
     * Gets the date of borrowing the copy.
     *
     * @return The date of the borrowing.
     */
    public String getDateOut() {
        return dateOut;
    }

    /**
     * Gets the date of returning the copy.
     *
     * @return The date of return.
     */
    public String getDateIn() {
        return dateIn;
    }

    /**
     * Gets the time of the borrow of the copy.
     *
     * @return The time of the borrow of the copy.
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     * Gets the time of the return of the copy.
     *
     * @return The time of the return of the copy.
     */
    public String getTimeIn() {
        return timeIn;
    }

    /**
     * Summarizes the loan event.
     *
     * @return The summary of the loan event.
     */
    @Override
    public String toString() {
        String out = "Copy ID: " + copyID
                + "\nBorrow date: " + dateOut + " " + timeOut;

        if (dateIn == null) {
            out += "\nReturn date: Not set";
        } else {
            out += "\nReturn date: " + dateIn + " " + timeIn;
        }
        return out;
    }
}
