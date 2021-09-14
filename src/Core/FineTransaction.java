package Core;

/**
 * Represents a transaction which is a fine.
 */
public class FineTransaction extends Transaction {

    /**
     * The copy id of the copy that caused the fine.
     */
    private final int copyID;

    /**
     * The days that the copy was overdue.
     */
    private final int days;

    /**
     * Creates a transaction.
     *
     * @param userID The account ID of the user with whom the transaction is
     * created.
     * @param date The date of the transaction.
     * @param time The time of the transaction.
     * @param change The change in money from the transaction.
     * @param tranID The ID of the transaction.
     * @param copyID The copy ID of the resource being returned.
     * @param days The amount of days by which copy is overdue.
     */
    public FineTransaction(int userID, String date, String time, float change, int tranID, int copyID, int days) {
        super(userID, date, time, change, tranID);

        this.copyID = copyID;
        this.days = days;
    }

    /**
     * Gets the copy id of the copy that caused the fine.
     *
     * @return The copy id of the copy that caused the fine.
     */
    public int getCopyID() {
        return copyID;
    }

    /**
     * Gets the days that the copy was overdue.
     *
     * @return The days that the copy was overdue.
     */
    public int getDays() {
        return days;
    }

    /**
     * Summarizes the fine transaction.
     *
     * @return The summary of the transaction.
     */
    @Override
    public String toString() {
        String out = super.toString();
        out += "\nCopy ID: " + copyID
                + "\nDays overdue: " + days;

        return out;
    }
}
