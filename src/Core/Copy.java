package Core;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a copy of a resource.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class Copy {

    /**
     * Uniquely identifies the copy.
     */
    private final int copyID;

    /**
     * Uniquely identifies the resource the copy is of.
     */
    private final int resourceID;

    /**
     * The amount of days the copy may have on loan, set when taken out.
     */
    private int loanDuration;

    /**
     * The date the copy must be returned (with YYYY-MM-DD format).
     */
    private String dueDate;

    /**
     * The current state of the copy. 0 - available, 1 - on loan, 2 - reserved.
     */
    private int stateID;

    /**
     * The current borrowers of the copy.
     */
    private int currentBorrowerID;

    /**
     * Creates a copy
     *
     * @param copyID Uniquely identifies the copy.
     * @param resourceID Uniquely identifies the resource.
     * @param loanDuration The duration of the loan in days.
     * @param dueDate The date the copy must be returned by.
     * @param stateID The current state of the copy.
     * @param currentBorrowerID The id of the user borrowing the copy.
     */
    public Copy(int copyID, int resourceID, int loanDuration, String dueDate,
            int stateID, int currentBorrowerID) {
        this.copyID = copyID;
        this.resourceID = resourceID;
        this.loanDuration = loanDuration;
        this.dueDate = dueDate;
        this.stateID = stateID;
        this.currentBorrowerID = currentBorrowerID;
    }

    /**
     * Gets the copy id.
     *
     * @return The id of the copy.
     */
    public int getCopyID() {
        return copyID;
    }

    /**
     * Gets the resource id of the copy.
     *
     * @return The resource id of the copy.
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * Gets the loan duration of the copy in days.
     *
     * @return The loan duration.
     */
    public int getLoanDuration() {
        return loanDuration;
    }

    /**
     * Set the loan duration of the copy.
     *
     * @param loanDuration The loan duration of a copy in days.
     */
    public void setLoanDuration(int loanDuration) {
        this.loanDuration = loanDuration;
    }

    /**
     * Gets the due date of the copy.
     *
     * @return The due date of the copy (with YYYY-MM-DD format).
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of the copy.
     *
     * @param dueDate The due date of a copy (with YYYY-MM-DD format).
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the state id of the copy.
     *
     * @return The state id of the copy (where 0 - available, 1 - on loan, 2 -
     * reserved).
     */
    public int getStateID() {
        return stateID;
    }

    /**
     * Sets the state id of the copy.
     *
     * @param stateID The state id of the copy (where 0 - available, 1 - on
     * loan, 2 - reserved).
     */
    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    /**
     * Gets the id of the user borrowing the copy.
     *
     * @return The borrowers user id.
     */
    public int getCurrentBorrowerID() {
        return currentBorrowerID;
    }

    /**
     * Sets the user id of the current borrower.
     *
     * @param currentBorrowerID The user id of the current borrower.
     */
    public void setCurrentBorrowerID(int currentBorrowerID) {
        this.currentBorrowerID = currentBorrowerID;
    }

    /**
     * Returns the difference in days between current date and due date of the
     * copy.
     *
     * @param currentDate The current date (with YYYY-MM-DD format).
     *
     * @return The difference in days between current date and due date of the
     * copy.
     *
     * @throws IllegalStateException Thrown if due date is not set.
     */
    public int calculateDaysOffset(String currentDate) throws IllegalStateException {
        //Checks if due date is set. If set, then calculation can proceed.
        if (!(dueDate == null)) {
            return Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.parse(currentDate), LocalDate.parse(dueDate)));
        } else {
            throw new IllegalStateException("No due date set.");
        }
    }

    /**
     * Creates a summary of information for the Copy.
     *
     * @return Returns a summary of the Copy.
     */
    @Override
    public String toString() {
        //create summary
        String out = "\nCopy ID - " + copyID
                + "\nResource ID - " + resourceID
                + "\nLoan Duration - " + loanDuration;
        if (stateID == 0) {
            out += "\nCopy is available.";
        } else if (stateID == 1) {
            out += "\nCopy is currently on loan";

            if (dueDate != null) {
                out += "\nIt is due on " + LocalDate.parse(dueDate).getDayOfMonth() + "/"
                        + LocalDate.parse(dueDate).getMonthValue() + "/"
                        + LocalDate.parse(dueDate).getYear();
            } else {
                out += "\nNo due date has been set";
            }
        } else {
            out += "\nCopy is currently reserved";
        }

        return out;
    }
}
