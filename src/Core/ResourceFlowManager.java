package Core;

import JavaFX.SceneController;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Manages operations that relate resources and users.
 *
 * @author Noah Lenagan, Paris Kelly Skopelitis
 * @author Martin Trifonov(v.2.0)
 * @author Matt LLewellyn(V.2.0)
 * @version 2.0
 */
public class ResourceFlowManager {

    /**
     * An instance of database manager.
     */
    private final DatabaseManager dbManager;

    /**
     * An instance of account manager.
     */
    private final AccountManager acManager;

    /**
     * An instance of resource manager.
     */
    private final ResourceManager rmManager;

    /**
     * The user id of the account.
     */
    private int userID;

    /**
     * An instance of event manager.
     */
    private final EventManager eventManager;

    /**
     * Creates the ResourceFlowManager.
     *
     * @param dbManager The database manager instance.
     * @param acManager The account manager instance.
     * @param rmManager The resource manager instance.
     * @param em The Event Manager instance.
     * @param userID The ID of a user.
     */
    public ResourceFlowManager(DatabaseManager dbManager, AccountManager acManager,
            ResourceManager rmManager, EventManager em, int userID) {
        this.eventManager = em;
        this.dbManager = dbManager;
        this.acManager = acManager;
        this.rmManager = rmManager;
        this.userID = userID;

    }

    /**
     * Gets the borrow history of a user.
     *
     * @param userID The user id of the user.
     *
     * @return The array of loan events.
     */
    public LoanEvent[] getBorrowHistory(int userID) {

        try {
            //Get loan data.
            String[][] loanData = dbManager.getTupleListByQuery("SELECT * FROM BorrowHistory WHERE UID = "
                    + userID);

            //create array of LoanEvents
            return constructLoanEvents(loanData);
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the borrow history of a copy.
     *
     * @param copyID The copy id of the copy.
     *
     * @return The array of loan events.
     */
    public LoanEvent[] getBorrowHistoryByCopy(int copyID) {

        try {
            //Get loan data.
            String[][] loanData = dbManager.getTupleListByQuery("SELECT * FROM BorrowHistory WHERE CID = "
                    + copyID);

            //create array of LoanEvents
            return constructLoanEvents(loanData);
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the borrow history of a resource
     *
     * @param resourceID The resource id of the resource.
     *
     * @return The array of loan events.
     */
    public LoanEvent[] getBorrowHistoryByResource(int resourceID) {

        try {
            //Get loan data.
            String[][] loanData = dbManager.getTupleListByQuery("SELECT * FROM BorrowHistory WHERE BorrowHistory.CID "
                    + "in (SELECT CPID FROM Copy WHERE RID = " + resourceID + ")");

            //create array of LoanEvents
            return constructLoanEvents(loanData);
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the borrow history of a user.
     *
     * @return The array of loan events.
     */
    public LoanEvent[] getBorrowHistory() {

        try {
            //Get loan data.
            String[][] loanData = dbManager.getTupleListByQuery("SELECT * FROM BorrowHistory");

            //create array of LoanEvents
            return constructLoanEvents(loanData);
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Constructs an array of loan events with loan data.
     *
     * @param loanData The data from borrow history table.
     *
     * @return An array of LoanEvents.
     */
    private LoanEvent[] constructLoanEvents(String[][] loanData) {

        //create array of LoanEvents
        LoanEvent[] events = new LoanEvent[loanData.length];

        //for every loan history, create a loan event.
        for (int iCount = 0; iCount < loanData.length; iCount++) {
            events[iCount] = new LoanEvent(Integer.parseInt(loanData[iCount][0]),
                    Integer.parseInt(loanData[iCount][1]), loanData[iCount][2],
                    loanData[iCount][3], loanData[iCount][4], loanData[iCount][5]);
        }

        return events;
    }

    /**
     * Gets all overdue copies.
     *
     * @return An array of copies that are overdue.
     */
    public Copy[] showOverdueCopies() {

        //Get all copies
        Copy[] copies = rmManager.getCopies();

        return filterByOverdueCopies(copies);

    }

    /**
     * Gets all overdue copies by resource ID.
     *
     * @param resourceID The resource id of the resource.
     *
     * @return An array of copies that are overdue of a resource ID.
     */
    public Copy[] showOverdueCopies(int resourceID) {

        //Get all copies
        Copy[] copies = rmManager.getCopies(resourceID);

        return filterByOverdueCopies(copies);

    }

    /**
     * Filters copies by if they are overdue.
     *
     * @param copies An array of copies to filter.
     *
     * @return An array of copies that are only overdue.
     */
    private Copy[] filterByOverdueCopies(Copy[] copies) {

        //if null then become a empty array of copies.
        if (copies == null) {
            copies = new Copy[0];
        }

        ArrayList<Copy> overdueCopies = new ArrayList<>();

        //Get current date
        String currentDate = DateManager.returnCurrentDate();

        //For every copy, check if overdue. If so, then add overdueCopies.
        for (Copy currentCopy : copies) {
            //If overdue, then add overdueCopies.

            int daysOffset;
            try {
                daysOffset = currentCopy.calculateDaysOffset(currentDate);
            } catch (IllegalStateException e) {
                //copy has no due date set.
                daysOffset = 0;
            }

            if (daysOffset < -1) {
                overdueCopies.add(currentCopy);
            }
        }

        //return as array.
        return overdueCopies.toArray(new Copy[overdueCopies.size()]);

    }

    /**
     * Gets all borrowed copies of a user.
     *
     * @param userID The user id of the user.
     *
     * @return The array of borrowed copies.
     */
    public Copy[] getBorrowedCopies(int userID) {

        try {
            //Get array of borrowed copy ids.
            String[][] copyIDs = dbManager.getTupleListByQuery("SELECT CPID FROM Copy WHERE UID = "
                    + userID + " AND StateID = 1");

            //Create array of copies
            Copy[] copies = new Copy[copyIDs.length];

            //For every copy id, construct the copy and add to array.
            for (int iCount = 0; iCount < copyIDs.length; iCount++) {
                copies[iCount] = rmManager.getCopy(Integer.parseInt(copyIDs[iCount][0]));
            }

            return copies;
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets all requested resources by a user.
     *
     * @param userID The user id of the user.
     *
     * @return The array of requested resources.
     */
    public Resource[] getRequestedResources(int userID) {

        try {

            //Get resource ids
            String[][] resourceIDs = dbManager.getTupleListByQuery("SELECT Resource.RID From Resource, "
                    + "ResourceRequestQueue WHERE ResourceRequestQueue.RID = Resource.RID AND "
                    + "UID = " + userID);

            //Create array of resources.
            Resource[] resources = new Resource[resourceIDs.length];

            //For every resource id, construct a resource.
            for (int iCount = 0; iCount < resourceIDs.length; iCount++) {
                resources[iCount] = rmManager.getResourceList("SELECT * FROM Resource WHERE RID = "
                        + resourceIDs[iCount][0])[0];
            }

            return resources;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Calculates the fine of a copy.
     *
     * @param copy The copy to calculate the fine of.
     *
     * @return The calculated fine amount.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     * @throws IllegalStateException Thrown if the copy is not on loan.
     */
    private float calculateFine(Copy copy) throws SQLException, IllegalStateException {

        //calculate the days before or after the due date.
        int daysOffset;

        //may throw exception if due date is not set.
        try {
            daysOffset = copy.calculateDaysOffset(DateManager.returnCurrentDate());
        } catch (IllegalStateException e) {
            daysOffset = 0;
        }

        float fine = 0;

        //if the copy is days after the due date then calculate fine otherwise return 0
        if (daysOffset < 0) {

            //get resource type fine data
            String[] resourceFineStat = dbManager.getFirstTupleByQuery("SELECT * FROM Fine, (SELECT TID FROM Resource"
                    + " WHERE RID = " + copy.getResourceID() + ") as T2 WHERE Fine.TID = T2.TID");

            //convert data to floats
            float dailyFine = Float.parseFloat(resourceFineStat[1]);
            float maxFine = Float.parseFloat(resourceFineStat[2]);

            //calculate fine
            fine = dailyFine * daysOffset;

            //If the (-)fine is larger than the (-)maxFine then cap the fine.
            if (fine < -maxFine) {
                fine = -maxFine;
            }
        }

        return fine;

    }

    /**
     * Un-reserves a copy and puts it on loan for the user.
     *
     * @param copyID The copy id of the copy.
     * @param userID The user id of the copy.
     *
     * @throws SQLException When connection to database fails.
     * @throws IllegalStateException When copyID is not currently reserved.
     */
    public void borrowFromReserve(int copyID, int userID) throws SQLException, IllegalStateException {

        unreserveCopy(copyID, userID);
        borrowCopy(copyID, userID);

    }

    /**
     * Borrows a specified copy for a user, updates user's ResourceCap.
     *
     * @param copyID The copy id of a copy.
     * @param userID The user id of a copy.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     * @throws IllegalStateException Thrown if specified copy or user does not
     * exist, user balance is smaller than 0, or user has too many resources
     * borrowed.
     */
    public void borrowCopy(int copyID, int userID) throws SQLException, IllegalStateException {
        int resourceType = checkResourceType(copyID);
        int resourceCap = acManager.getUserResourceCap(userID);
        if (resourceCap < 5 && resourceType == 1 || resourceCap < 5 && resourceType == 2
                || resourceCap < 5 && resourceType == 4 || resourceCap < 3 && resourceType == 3) {

            //if user balance is greater or equal to 0, then continue to borrow. Otherwise throw exception.
            if (acManager.getAccountBalance(userID) >= 0) {

                //check if copy is available, if so then borrow copy. Otherwise throw exception.
                if (getCopyState(copyID) == 0) {

                    //Get the resource id.
                    int resourceID = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT RID FROM Copy WHERE CPID = "
                            + copyID)[0]);

                    //If the user exists then borrow the copy.
                    if (dbManager.checkIfExist("User", new String[]{
                        "UID"
                    },
                            new String[]{
                                Integer.toString(userID)
                            })) {

                        //remove copy from availability queue.
                        removeAvailable(copyID, resourceID);
                        //enqueue in borrowed queue.
                        enqueueBorrowed(copyID, resourceID, userID);

                        //adds to borrow history
                        dbManager.addTuple("BorrowHistory",
                                new String[]{
                                    Integer.toString(userID), Integer.toString(copyID),
                                    encase(DateManager.returnCurrentDate()), "null",
                                    encase(DateManager.returnTime()), "null"
                                });

                        if (resourceType == 1 || (resourceType == 2) || (resourceType == 4)) {
                            acManager.changeUserResourceCap(userID, 1);
                        } else if (resourceType == 3) {
                            acManager.changeUserResourceCap(userID, 3);
                        }

                    } else {
                        throw new IllegalStateException("User does not exist");
                    }

                } else {
                    throw new IllegalStateException("Copy is not available");
                }
            } else {
                throw new IllegalStateException("Balance must be positive to borrow copies!");
            }
        } else {
            throw new IllegalStateException("User has too many resources to take out more!");
        }
    }

    /**
     * Returns a specified copy from a user updates user's resourceCap.
     *
     * @param copyID The copy id of a copy.
     * @param userID The user id of a copy.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     * @throws IllegalStateException Thrown if specified copy or user does not
     * exist. Or the wrong user is passed.
     */
    public void returnCopy(int copyID, int userID) throws SQLException, IllegalStateException {

        //check if copy is on loan, if so then return the copy. Otherwise throw exception.
        if (getCopyState(copyID) == 1) {

            //Construct copy to perform fine operations.
            Copy copy = rmManager.getCopy(copyID);

            //check if copy owner is the user id
            if (userID == copy.getCurrentBorrowerID()) {
                //Get the resource id.
                int resourceID = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT RID FROM Copy WHERE CPID = "
                        + copyID)[0]);

                //If the user exists then return the copy.
                if (dbManager.checkIfExist("User", new String[]{
                    "UID"}, new String[]{Integer.toString(userID)})) {

                    //set date returned.
                    dbManager.sqlQuery("UPDATE BorrowHistory SET Date_Returned = "
                            + encase(DateManager.returnCurrentDate())
                            + " , Time_Returned = " + encase(DateManager.returnTime())
                            + " WHERE UID = " + userID
                            + " AND CID = " + copyID + " AND Date_Returned IS NULL");

                    //Calculate fine.
                    float fine = calculateFine(copy);

                    //If there is a fine then take from balance.
                    if (fine < 0) {
                        acManager.addFine(userID, fine, copyID, copy.
                                calculateDaysOffset(DateManager.returnCurrentDate()));
                    }

                    //check request queue. If there is a request then reserve copy. Otherwise make copy available.
                    if (dbManager.checkIfExist("ResourceRequestQueue", new String[]{"RID"}, new String[]{Integer.toString(resourceID)})) {
                        //get the request data.
                        String[] data = dbManager.getFirstTupleByQuery("SELECT min(Position), UID FROM ( "
                                + "SELECT Position, UID FROM ResourceRequestQueue WHERE RID = "
                                + resourceID + " )");

                        int pos = Integer.parseInt(data[0]);
                        int firstUser = Integer.parseInt(data[1]);

                        //remove from borrowed queue.
                        removeBorrowedCopy(copyID, resourceID);

                        //reserve the copy for the request user.
                        reserveCopy(copyID, firstUser);

                        Resource resource = SceneController.getResourceFlowManager().rmManager.getResource(resourceID);
                        User user = SceneController.getResourceFlowManager().acManager.getAccount(firstUser);
                        NotificationCenter nc = new NotificationCenter();
                        System.out.println(fine);
                        nc.sendReturnedItemNotification(resource, fine, user.getFirstName(), user.getEmail());

                        //delete request
                        dbManager.deleteTuple("ResourceRequestQueue", new String[]{"Position"}, new String[]{Integer.toString(pos)});
                        int resourceType = checkResourceType(copyID);

                        if (resourceType == 1 || (resourceType == 2) || (resourceType == 4)) {
                            acManager.changeUserResourceCap(userID, -1);
                        } else if (resourceType == 3) {
                            acManager.changeUserResourceCap(userID, -3);
                        }

                    } else {

                        //remove from borrowed queue.
                        removeBorrowedCopy(copyID, resourceID);

                        //make copy available.
                        enqueueAvailable(copyID, resourceID);

                        int resourceType = checkResourceType(copyID);

                        if (resourceType == 1 || (resourceType == 2) || (resourceType == 4)) {
                            acManager.changeUserResourceCap(userID, -1);
                        } else if (resourceType == 3) {
                            acManager.changeUserResourceCap(userID, -3);
                        };

                        Resource resource = SceneController.getResourceFlowManager().rmManager.getResource(resourceID);
                        User user = SceneController.getResourceFlowManager().acManager.getAccount(userID);
                        NotificationCenter nc = new NotificationCenter();
                        nc.sendReturnedItemNotification(resource, fine, user.getFirstName(), user.getEmail());
                    }

                } else {
                    throw new IllegalStateException("User does not exist");
                }

            } else {
                throw new IllegalStateException("Specified user does not possess this copy");
            }

        } else {
            throw new IllegalStateException("Copy is not on loan");
        }
    }

    /**
     * Reserves a copy for a user.
     *
     * @param copyID The copy id of a copy.
     * @param userID The user id of a user.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    private void reserveCopy(int copyID, int userID) throws SQLException, IllegalStateException {
        //If the copy state is undefined then reserve the copy.
        if (getCopyState(copyID) == -1) {
            dbManager.addTuple("ReservedResource",
                    new String[]{
                        Integer.toString(copyID), Integer.toString(userID)
                    });
            setCopyState(copyID, 2);
        } else {
            throw new IllegalStateException("Copy state must be undefined");
        }
        NotificationCenter nc = new NotificationCenter();
        User user = SceneController.getResourceFlowManager().acManager.getAccount(userID);
        int rid = Integer.valueOf(SceneController.getDatabase().getTupleListByQuery("Select RID From Copy Where CPID=" + copyID)[0][0]);
        Resource resource = SceneController.getResourceFlowManager().rmManager.getResource(rid);
        nc.sendReservedItemNotification(resource, user.getFirstName(), user.getEmail());
    }

    /**
     * Un-reserves a copy for a user.
     *
     * @param copyID The copy id of a copy.
     * @param userID The user id of a user.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public void unreserveCopy(int copyID, int userID) throws SQLException, IllegalStateException {
        if (getCopyState(copyID) == 2) {
            dbManager.deleteTuple("ReservedResource",
                    new String[]{
                        "CID", "UID"
                    },
                    new String[]{
                        Integer.toString(copyID), Integer.toString(userID)
                    });

            setCopyState(copyID, -1);

            //get resource id of copy.
            int resourceID = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT RID FROM Copy WHERE CPID = "
                    + copyID)[0]);

            enqueueAvailable(copyID, resourceID);
        } else {
            throw new IllegalStateException("Copy must be reserved");
        }
    }

    /**
     * Gets all reserved copies of the user.
     *
     * @param userID The user id of the user.
     *
     * @return An array of reserved copies.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public Copy[] getReservedCopies(int userID) throws SQLException {

        //An array of copy ids that have been reserved by the user.
        String[][] copyIDs = dbManager.getTupleListByQuery("SELECT CID FROM ReservedResource WHERE UID = "
                + userID);

        //An array of the reserved copies.
        Copy[] copies = new Copy[copyIDs.length];

        //For every copy id, construct the copy.
        for (int iCount = 0; iCount < copyIDs.length; iCount++) {
            copies[iCount] = rmManager.getCopy(Integer.parseInt(copyIDs[iCount][0]));
        }

        //Return the copies.
        return copies;

    }

    /**
     * Requests a select resource for the logged user. If a copy is already
     * available, it will be reserved.
     *
     * @param resourceID The resource id of the resource.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public void requestResource(int resourceID) throws SQLException {
        requestResource(resourceID, this.userID);
    }

    /**
     * Requests a select resource for a user. If a copy is already available, it
     * will be reserved.
     *
     * @param resourceID The resource id of the resource.
     * @param userID The user id of the user.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     * @throws IllegalStateException Thrown if there are no copies that are
     * available or borrowed.
     */
    public void requestResource(int resourceID, int userID) throws SQLException, IllegalStateException {

        //add request to history
        dbManager.addTuple("ResourceRequestHistory",
                new String[]{
                    Integer.toString(resourceID), Integer.toString(userID),
                    encase(DateManager.returnCurrentDate())
                });

        //check if there is a copy available to reserve, else check if any borrowed
        if (dbManager.checkIfExist("Copy", new String[]{"RID", "StateID"}, new String[]{Integer.toString(resourceID), "0"})) {
            //there exists a copy that is available
            //get the copy id
            int copyID = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT CPID FROM Copy WHERE RID = "
                    + resourceID + " AND StateID = 0")[0]);

            //removes copy from availability queue.
            removeAvailable(copyID, resourceID);

            //reserves copy.
            reserveCopy(copyID, userID);

        } else if (dbManager.checkIfExist("Copy", new String[]{"RID", "StateID"}, new String[]{Integer.toString(resourceID), "1"})) {
            //otherwise add user to request queue and set due date on a borrowed copy.
            dbManager.addTuple("ResourceRequestQueue",
                    new String[]{"null", Integer.toString(resourceID), Integer.toString(userID)});

            //traverse borrowed queue until copy without date set is found.
            //get the head of the queue.
            int currentCopy = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT HeadOfBorrowedQueue FROM "
                    + "Resource WHERE RID = " + resourceID)[0]);
            boolean isFound = false;
            boolean isNext = true;

            //traverse borrowed queue until end is reached or first copy has been found without due date
            do {
                //if the copy has no due date, then set the due date and exit the loop.
                if (dbManager.getFirstTupleByQuery("SELECT Due_Date FROM Copy WHERE CPID = "
                        + currentCopy)[0] == null) {

                    //get the loan duration.
                    int loanDuration = Integer.parseInt(
                            dbManager.getFirstTupleByQuery("SELECT Loan_Duration FROM Copy "
                                    + "WHERE CPID = " + currentCopy)[0]);

                    //Get the amount of days relative to the due date.
                    Copy copy = rmManager.getCopy(currentCopy);
                    LoanEvent[] loanEvents = getBorrowHistoryByCopy(currentCopy);

                    //find the loan date for the resource.
                    /*String borrowDate = dbManager.getFirstTupleByQuery("SELECT Date_Out FROM BorrowHistory" +
        " WHERE CID = " + Integer.toString(currentCopy) + " AND Date_Returned IS NULL")[0];*/
                    String borrowDate = dbManager.getFirstTupleByQuery("SELECT Date_Out FROM BorrowHistory"
                            + " WHERE CID = " + currentCopy + " AND Date_Returned IS NULL")[0];

                    String estimateDue = "";

                    //add loan duration to borrow date
                    estimateDue = DateManager.returnDueDate(borrowDate, loanDuration);

                    //get the offset
                    int offset = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.parse(DateManager.returnCurrentDate()), LocalDate.parse(estimateDue)));

                    //get the due date
                    String dueDate;

                    //if user has borrowed for longer than loan duration, then set due date for next day
                    //otherwise set due date for the loan duration + borrow date.
                    if (offset <= 0) {
                        dueDate = DateManager.returnDueDate(1);
                    } else {
                        dueDate = DateManager.returnDueDate(offset);
                    }

                    //set due date
                    dbManager.editTuple("Copy", new String[]{
                        "Due_Date"}, new String[]{encase(dueDate)},
                            "CPID", Integer.toString(currentCopy));

                    String[][] data = dbManager.getTupleListByQuery("Select RID, UID From Copy Where CPID =" + currentCopy);
                    Resource resource = SceneController.getResourceFlowManager().rmManager.getResource(Integer.valueOf(data[0][0]));
                    User user = SceneController.getResourceFlowManager().acManager.getAccount(Integer.valueOf(data[0][1]));
                    NotificationCenter nc = new NotificationCenter();
                    nc.sendDueItemNotification(resource, dueDate, user.getFirstName(), user.getEmail());

                    user = SceneController.getResourceFlowManager().acManager.getAccount(userID);
                    nc.sendRequestedItemNotification(resource, user.getFirstName(), user.getEmail());

                    isFound = true;
                }

                //if there is a next copy then set the next copy. Otherwise isNext becomes false.
                if (isNext(currentCopy)) {
                    currentCopy = getNext(currentCopy);
                } else {
                    isNext = false;
                }
            } while (isNext && !isFound);

        } else {
            throw new IllegalStateException("No copies can be requested");
        }

    }

    /**
     * Deletes a request for a resource.
     *
     * @param resourceID The resource id of the resource.
     * @param userID The user id of the resource.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public void deleteRequest(int resourceID, int userID) throws SQLException {

        dbManager.deleteTuple("ResourceRequestQueue", new String[]{
            "RID", "UID"}, new String[]{Integer.toString(resourceID),
            Integer.toString(userID)
        });

    }

    /**
     * Deletes all requests from a user.
     *
     * @param userID The user id of a user.
     *
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public void deleteAllRequests(int userID) throws SQLException {

        dbManager.deleteTuple("ResourceRequestQueue", new String[]{
            "UID"
        },
                new String[]{
                    Integer.toString(userID)
                });

    }

    /**
     * Makes the copy borrowed. The copy must not already be available, on loan
     * or reserved.
     *
     * @param copyID The copy id of the copy.
     * @param resourceID The resource id of the resource.
     * @param userID The user id of the user.
     *
     * @throws IllegalStateException Thrown if copy is available, on loan or
     * reserved.
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    private void enqueueBorrowed(int copyID, int resourceID, int userID) throws IllegalStateException, SQLException {

        //enqueue if not borrowed, reserved or available, otherwise throw a illegal state exception.
        //state of -1 represents an undefined state of the copy.
        if (getCopyState(copyID) == -1) {

            String stResourceID = Integer.toString(resourceID);
            String stCopyID = Integer.toString(copyID);

            //get queue tail
            String tail = dbManager.getFirstTupleByQuery("SELECT TailOfBorrowedQueue FROM "
                    + "Resource WHERE RID = " + resourceID)[0];

            //If the tail is null then set the head and tail, otherwise make the tail point to the copy.
            if (tail == null) {
                //set head and tail
                dbManager.editTuple("Resource",
                        new String[]{
                            "HeadOfBorrowedQueue"
                        },
                        new String[]{
                            stCopyID
                        }, "RID", stResourceID);
            } else {
                //Make the tail point to the first copy.
                setNextCopy(Integer.parseInt(tail), copyID);

                //Make the copy point to the tail.
                setPrevCopy(copyID, tail);
            }

            //make copy the new tail.
            dbManager.editTuple("Resource", new String[]{
                "TailOfBorrowedQueue"
            },
                    new String[]{
                        stCopyID
                    }, "RID", stResourceID);

            //set copy state id and due date.
            dbManager.editTuple("Copy", new String[]{
                "StateID", "Due_Date", "UID"
            },
                    new String[]{
                        "1", "null", Integer.toString(userID)
                    }, "CPID", stCopyID);

        } else {
            throw new IllegalStateException("Copy must not be reserved, on loan or available.");
        }

    }

    /**
     * Makes a copy unavailable. The copy must not already be available, on loan
     * or reserved.
     *
     * @param copyID The copy id of the copy.
     * @param resourceID The resource id of the resource.
     *
     * @throws IllegalStateException Thrown if copy is available, on loan or
     * reserved.
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    private void removeBorrowedCopy(int copyID, int resourceID) throws IllegalStateException, SQLException {

        //dequeue if only currently available, otherwise throw a illegal state exception.
        //state of -1 represents an undefined state of the copy.
        if (getCopyState(copyID) == 1) {

            String stResourceID = Integer.toString(resourceID);

            //get queue head
            String head = dbManager.getFirstTupleByQuery("SELECT HeadOfBorrowedQueue FROM "
                    + "Resource WHERE RID = " + resourceID)[0];

            //If head is null then throw illegal state exception, otherwise remove copy
            if (head == null) {
                throw new IllegalStateException("Queue is empty");
            } else {

                //Get previous id and next id.
                int prev = -1;
                int next = -1;

                //Get previous copy id if exists.
                if (isPrev(copyID)) {
                    prev = getPrev(copyID);
                }

                //Get next copy id if exists.
                if (isNext(copyID)) {
                    next = getNext(copyID);
                }

                if (prev != -1 && next != -1) {
                    //If prev and next does equal null.
                    setNextCopy(prev, next);
                    setPrevCopy(next, prev);
                } else if (prev == -1 && next != -1) {
                    //If the copy is the head but isn't the tail.
                    //set the head
                    dbManager.editTuple("Resource",
                            new String[]{
                                "HeadOfBorrowedQueue"
                            },
                            new String[]{
                                Integer.toString(next)
                            },
                            "RID", stResourceID);
                    setPrevCopy(next, "null");
                } else if (prev != -1 && next == -1) {
                    //If the copy is the tail but isn't the head.
                    //set the tail
                    dbManager.editTuple("Resource",
                            new String[]{
                                "TailOfBorrowedQueue"
                            },
                            new String[]{
                                Integer.toString(prev)
                            },
                            "RID", stResourceID);
                    setNextCopy(prev, "null");
                } else {
                    //If the copy is both the head and tail.
                    //set both the head and tail to null as queue is empty
                    dbManager.editTuple("Resource",
                            new String[]{
                                "HeadOfBorrowedQueue"
                            },
                            new String[]{
                                "null"
                            },
                            "RID", stResourceID);
                    dbManager.editTuple("Resource",
                            new String[]{
                                "TailOfBorrowedQueue"
                            },
                            new String[]{
                                "null"
                            },
                            "RID", stResourceID);
                }

                //Set the copys next and prev to null.
                setNextCopy(copyID, "null");
                setPrevCopy(copyID, "null");

                //Set the state
                setCopyState(copyID, -1);

            }

        } else {
            throw new IllegalStateException("Copy must not be reserved, on loan or available.");
        }

    }

    /**
     * Makes a copy available. The copy must not already be available, on loan
     * or reserved.
     *
     * @param copyID The copy id of the copy.
     * @param resourceID The resource id of the resource.
     *
     * @throws IllegalStateException Thrown if copy is available, on loan or
     * reserved.
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    private void enqueueAvailable(int copyID, int resourceID) throws IllegalStateException, SQLException {

        //enqueue if not borrowed, reserved or available, otherwise throw a illegal state exception.
        //state of -1 represents an undefined state of the copy.
        if (getCopyState(copyID) == -1) {

            String stResourceID = Integer.toString(resourceID);
            String stCopyID = Integer.toString(copyID);

            //get queue tail
            String tail = dbManager.getFirstTupleByQuery("SELECT TailOfAvailableQueue FROM "
                    + "Resource WHERE RID = " + resourceID)[0];

            //If the tail is null then set the head and tail, otherwise make the tail point to the copy.
            if (tail == null) {
                //set head and tail
                dbManager.editTuple("Resource",
                        new String[]{
                            "HeadOfAvailableQueue"
                        },
                        new String[]{
                            stCopyID
                        }, "RID", stResourceID);
            } else {
                //Make the tail point to the copy.
                setNextCopy(Integer.parseInt(tail), copyID);

                //Make the copy point to the tail.
                setPrevCopy(copyID, tail);
            }

            //make copy the new tail.
            dbManager.editTuple("Resource", new String[]{
                "TailOfAvailableQueue"
            },
                    new String[]{
                        stCopyID
                    }, "RID", stResourceID);

            //set copy state id and due date.
            dbManager.editTuple("Copy", new String[]{
                "StateID", "Due_Date"
            },
                    new String[]{
                        "0", "null"
                    }, "CPID", stCopyID);

        } else {
            throw new IllegalStateException("Copy must not be reserved, on loan or available.");
        }

    }

    /**
     * Makes a copy unavailable. The copy must not already be available, on loan
     * or reserved.
     *
     * @param copyID The copy id of the copy.
     * @param resourceID The resource id of the resource.
     *
     * @throws IllegalStateException Thrown if copy is available, on loan or
     * reserved.
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    private void removeAvailable(int copyID, int resourceID) throws IllegalStateException, SQLException {

        //dequeue if only currently available, otherwise throw a illegal state exception.
        //state of -1 represents an undefined state of the copy.
        if (getCopyState(copyID) == 0) {

            String stResourceID = Integer.toString(resourceID);

            //get queue head
            String head = dbManager.getFirstTupleByQuery("SELECT HeadOfAvailableQueue FROM "
                    + "Resource WHERE RID = " + resourceID)[0];

            //If head is null then throw illegal state exception, otherwise remove copy
            if (head == null) {
                throw new IllegalStateException("Queue is empty");
            } else {

                //Get previous id and next id.
                int prev = -1;
                int next = -1;

                //Get previous copy id if exists.
                if (isPrev(copyID)) {
                    prev = getPrev(copyID);
                }

                //Get next copy id if exists.
                if (isNext(copyID)) {
                    next = getNext(copyID);
                }

                if (prev != -1 && next != -1) {
                    //If prev and next does equal null.
                    setNextCopy(prev, next);
                    setPrevCopy(next, prev);
                } else if (prev == -1 && next != -1) {
                    //If the copy is the head but isn't the tail.
                    //set the head
                    dbManager.editTuple("Resource",
                            new String[]{
                                "HeadOfAvailableQueue"
                            },
                            new String[]{
                                Integer.toString(next)
                            },
                            "RID", stResourceID);
                    setPrevCopy(next, "null");
                } else if (prev != -1 && next == -1) {
                    //If the copy is the tail but isn't the head.
                    //set the tail
                    dbManager.editTuple("Resource",
                            new String[]{
                                "TailOfAvailableQueue"
                            },
                            new String[]{
                                Integer.toString(prev)
                            },
                            "RID", stResourceID);
                    setNextCopy(prev, "null");
                } else {
                    //If the copy is both the head and tail.
                    //set both the head and tail to null as queue is empty
                    dbManager.editTuple("Resource",
                            new String[]{
                                "HeadOfAvailableQueue"
                            },
                            new String[]{
                                "null"
                            },
                            "RID", stResourceID);
                    dbManager.editTuple("Resource",
                            new String[]{
                                "TailOfAvailableQueue"
                            },
                            new String[]{
                                "null"
                            },
                            "RID", stResourceID);
                }

                //Set the copys next and prev to null.
                setNextCopy(copyID, "null");
                setPrevCopy(copyID, "null");

                //Set the state
                setCopyState(copyID, -1);

            }

        } else {
            throw new IllegalStateException("Copy must not be reserved, on loan or available.");
        }

    }

    /**
     * Sets the next copy of the copy.
     *
     * @param copyID The copy id of the Copy.
     * @param nextID The copy id of the next Copy.
     *
     * @throws IllegalStateException Thrown if the copy does not exist.
     * @throws SQLException Thrown if table does not exist or connection to
     * database failed.
     */
    private void setNextCopy(int copyID, String nextID) throws IllegalStateException, SQLException {
        //If the copy exists then check if the next copy exists. Otherwise thrown exception.
        if (rmManager.doesCopyExist(copyID)) {
            //Set the next copy of the copy.
            dbManager.editTuple("Copy", new String[]{
                "NextCopyInQueue"
            },
                    new String[]{
                        nextID
                    }, "CPID", Integer.toString(copyID));

        } else {
            //Thrown if copy doesn't exist.
            throw new IllegalStateException("The copy ID specified does not exist");
        }
    }

    /**
     * Sets the next copy of the copy.
     *
     * @param copyID The copy id of the Copy.
     * @param nextID The copy id of the next Copy.
     *
     * @throws IllegalStateException Thrown if the copy does not exist.
     * @throws SQLException Thrown if table does not exist or connection to
     * database failed.
     */
    private void setNextCopy(int copyID, int nextID) throws IllegalStateException, SQLException {
        setNextCopy(copyID, Integer.toString(nextID));
    }

    /**
     * Sets the previous copy of the copy.
     *
     * @param copyID The copy id of the Copy.
     * @param prevID The copy id of the prev Copy.
     *
     * @throws IllegalStateException Thrown if the copy does not exist.
     * @throws SQLException Thrown if table does not exist or connection to
     * database failed.
     */
    private void setPrevCopy(int copyID, String prevID) throws IllegalStateException, SQLException {
        //If the copy exists then check if the next copy exists. Otherwise thrown exception.
        if (rmManager.doesCopyExist(copyID)) {

            //Set the previous copy of the copy.
            dbManager.editTuple("Copy", new String[]{
                "PreviousCopyInQueue"
            },
                    new String[]{
                        prevID
                    }, "CPID", Integer.toString(copyID));

        } else {
            //Thrown if copy doesn't exist.
            throw new IllegalStateException("The copy ID specified does not exist");
        }
    }

    /**
     * Sets the previous copy of the copy.
     *
     * @param copyID The copy id of the Copy.
     * @param prevID The copy id of the prev Copy.
     *
     * @throws IllegalStateException Thrown if the copy does not exist.
     * @throws SQLException Thrown if table does not exist or connection to
     * database failed.
     */
    private void setPrevCopy(int copyID, int prevID) throws IllegalStateException, SQLException {
        setPrevCopy(copyID, Integer.toString(prevID));
    }

    /**
     * Gets the previous copy id of the copy.
     *
     * @param copyID The copy id of the copy.
     *
     * @return The copy id of the previous copy.
     *
     * @throws SQLException Thrown if connection to database failed or table
     * does not exist.
     * @throws IllegalStateException Thrown if the copy or previous copy does
     * not exist.
     */
    private int getPrev(int copyID) throws SQLException, IllegalStateException {
        //If the copy exists, get the previous copy.
        if (rmManager.doesCopyExist(copyID)) {

            String prev = dbManager.getFirstTupleByQuery("SELECT PreviousCopyInQueue FROM Copy WHERE"
                    + " CPID = " + copyID)[0];

            if (prev == null) {
                throw new IllegalStateException("Previous copy does not exist");
            } else {
                return Integer.parseInt(prev);
            }

        } else {
            throw new IllegalStateException("Copy does not exist");
        }
    }

    /**
     * Gets the next copy id of the copy.
     *
     * @param copyID The copy id of the copy.
     *
     * @return The copy id of the next copy.
     *
     * @throws SQLException Thrown if connection to database failed or table
     * does not exist.
     * @throws IllegalStateException Thrown if the copy or next copy does not
     * exist.
     */
    private int getNext(int copyID) throws SQLException, IllegalStateException {
        //If the copy exists, get the next copy.
        if (rmManager.doesCopyExist(copyID)) {

            String next = dbManager.getFirstTupleByQuery("SELECT NextCopyInQueue FROM Copy WHERE"
                    + " CPID = " + copyID)[0];

            if (next.equals("null")) {
                throw new IllegalStateException("Next copy does not exist");
            } else {
                return Integer.parseInt(next);
            }

        } else {
            throw new IllegalStateException("Copy does not exist");
        }
    }

    /**
     * Determines if there is a previous copy in the queue.
     *
     * @param copyID The copy id of the copy.
     *
     * @return Returns true, if previous copy exists. False is does not exist.
     *
     * @throws SQLException Thrown if connection to database failed or table
     * does not exist.
     * @throws IllegalStateException Thrown if the copy specified does not
     * exist.
     */
    private boolean isPrev(int copyID) throws SQLException, IllegalStateException {
        //If the copy exists, get the previous copy.
        if (rmManager.doesCopyExist(copyID)) {

            String prev = dbManager.getFirstTupleByQuery("SELECT PreviousCopyInQueue FROM Copy WHERE"
                    + " CPID = " + copyID)[0];

            //if previous is null then return false, otherwise true.
            return prev != null;

        } else {
            throw new IllegalStateException("Copy does not exist");
        }
    }

    /**
     * Determines if there is a next copy in the queue.
     *
     * @param copyID The copy id of the copy.
     *
     * @return Returns true, if next copy exists. False is does not exist.
     *
     * @throws SQLException Thrown if connection to database failed or table
     * does not exist.
     * @throws IllegalStateException Thrown if the copy specified does not
     * exist.
     */
    private boolean isNext(int copyID) throws SQLException, IllegalStateException {
        //If the copy exists, get the next copy.
        if (rmManager.doesCopyExist(copyID)) {

            String next = dbManager.getFirstTupleByQuery("SELECT NextCopyInQueue FROM Copy WHERE"
                    + " CPID = " + copyID)[0];

            //if next is null then return false, otherwise true.
            return next != null;

        } else {
            throw new IllegalStateException("Copy does not exist");
        }
    }

    /**
     * Sets the state of the Copy.
     *
     * @param copyID The copy id of the copy.
     * @param state The state of the copy where -1 is undefined, 0 is available,
     * 1 is on loan and 2 is reserved.
     *
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     * @throws IllegalArgumentException Thrown if state argument is invalid.
     */
    private void setCopyState(int copyID, int state) throws SQLException, IllegalArgumentException {
        //If the state specified is valid then change the state.
        if (state >= -1 && state <= 2) {
            dbManager.editTuple("Copy", new String[]{
                "StateID"
            },
                    new String[]{
                        Integer.toString(state)
                    }, "CPID",
                    Integer.toString(copyID));
        } else {
            throw new IllegalArgumentException("State specified is invalid");
        }
    }

    /**
     * Gets the state of the Copy.
     *
     * @param copyID The copy id of the copy.
     *
     * @return The state of the Copy.
     *
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     * @throws IllegalArgumentException Thrown if specified copy does not exist.
     */
    private int getCopyState(int copyID) throws SQLException, IllegalArgumentException {
        //If copy exists, return it's state. Otherwise return
        if (rmManager.doesCopyExist(copyID)) {
            return Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT StateID FROM Copy WHERE CPID = "
                    + copyID)[0]);
        } else {
            throw new IllegalArgumentException("Specified copy does not exist");
        }
    }

    /**
     * Encases a string in apostrophe marks.
     *
     * @param str The string to encase.
     *
     * @return The encased string.
     */
    private String encase(String str) {
        return "'" + str + "'";
    }

    /**
     * Gets the currently logged in user id.
     *
     * @return User id of the user.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user id of the logged user.
     *
     * @param userID The user id of the user.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Returns an instance of the Event Manager.
     *
     * @return Event Manager instance.
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Gets all reviews on a resource
     *
     * @param resourceId The resource ID of the resource
     * @return An array of reviews.
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public Review[] getReviews(int resourceId) throws SQLException {

        //An array of the reviews
        String[][] reviewsRaw = dbManager.getTupleListByQuery("SELECT * FROM ResourceReview WHERE ResourceID = "
                + resourceId);

        //An array of review objects
        Review[] reviews = new Review[reviewsRaw.length];

        //For every review, add it to the review array
        for (int iCount = 0; iCount < reviewsRaw.length; iCount++) {
            reviews[iCount] = rmManager.getReview(Integer.parseInt(reviewsRaw[iCount][0]));
        }

        //Return the reviews.
        return reviews;

    }

    /**
     * Returns the users first name based off their UID
     *
     * @param userId the userId
     * @return the users first name
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public String[] getUserName(int userId) throws SQLException {

        String[] userName = dbManager.getFirstTupleByQuery("SELECT First_Name FROM User WHERE UID = "
                + userId);

        return userName;

    }

    /**
     * Checks if the user has borrowed a certain resource
     *
     * @param userId the userId
     * @param resId the resource Id
     * @return true/false depending on if the user has borrowed
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public boolean hasUserBorrowed(int userId, int resId) throws SQLException {

        String[] keys = {"RID", "UID"};
        String[] values = {Integer.toString(resId), Integer.toString(userId)};
        return dbManager.checkIfExist("Copy", keys, values);

    }

    /**
     * Checks what type a resource is
     *
     * @param copyID - the copy ID
     * @return int 1-4 depending on resource type (Book,DVD,Computer,Game)
     * @throws SQLException Thrown if connection to database failed or tables do
     * not exist.
     */
    public int checkResourceType(int copyID) throws SQLException {
        int resourceID = Integer.parseInt(dbManager.getTupleListByQuery(
                "SELECT RID FROM Copy WHERE CPID = " + copyID)[0][0]);

        int resourceType = Integer.parseInt(dbManager.getTupleListByQuery(
                "SELECT TID FROM Resource WHERE RID = " + resourceID)[0][0]);
        return resourceType;
    }

}
