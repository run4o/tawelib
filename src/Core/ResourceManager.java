package Core;

import JavaFX.SceneController;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Responsible for fetching, adding and editing resources from the database.
 *
 * @author Noah Lenagan
 * @author Martin Trifonov(V.2.0)
 * @author Rory Richards(V.2.0)
 * @author Kaleb Tuck(V.2.0)
 * @author Fraser Barrass(V.2.0)
 * @version 2.0
 */
public class ResourceManager {

    /**
     * The default amount of copies to add if no amount is specified.
     */
    private static final int DEFAULT_COPIES_PER_RESOURCE = 5;
    /**
     * An instance of DatabaseManager.
     */
    private final DatabaseManager dbManager;

    /**
     * Creates ResourceManager.
     *
     * @param dbManager An instance of DatabaseManager
     */
    public ResourceManager(DatabaseManager dbManager) {

        this.dbManager = dbManager;

    }

    /**
     * Creates a query for a user-specified input for specific type.
     *
     * @param column An array of columns used for search.
     * @param attribute An array of user input,
     * @param type Type of resource.
     *
     * @return Query string for user-specified search
     */
    public String createQuery(String[] column, String[] attribute, String type) {
        String sqlQuery = "SELECT * FROM Resource, " + type + " WHERE Resource.RID = " + type + ".RID AND ";
        // for every column value in data except the last value, add to the sqlQuery
        for (int iColumn = 0; iColumn < column.length - 1; iColumn++) {
            sqlQuery += column[iColumn] + " LIKE " + attribute[iColumn] + " AND ";

        }
        // add the last column value to the sql query without a comma.
        sqlQuery += column[column.length - 1] + " LIKE " + attribute[attribute.length - 1] + ";";

        return sqlQuery;
    }

    /**
     * Gets all resources found in the database.
     *
     * @return All resources found in the database as Resource object.
     */
    public Resource[] getResourceList() {

        String[][] table;

        try {

            table = dbManager.getTupleList("Resource");
            return constructResources(table);

        } catch (SQLException e) {
            /*
            Catch any sql errors and return null.
            Any errors will most likely be as a result of a connection failure to the database or missing tables.
             */
            return null;
        }

    }

    /**
     * Get specific resource by resource id.
     *
     * @param resourceID The resource id of the resource.
     *
     * @return The specific resource.
     *
     * @throws IllegalArgumentException Thrown if resource specified does not
     * exist.
     * @throws SQLException Thrown if column specified is incorrect or
     * connection to database couldn't be established.
     */
    public Resource getResource(int resourceID) throws IllegalArgumentException, SQLException {

        try {

            //Get resource by rid
            Resource resource = getResourceList("SELECT * FROM Resource WHERE RID = "
                    + resourceID)[0];

            //If no resource found by id, then throw illegal argument exception.
            if (resource == null) {
                throw new IllegalArgumentException("Resource specified does not exist");
            }

            return resource;

        } catch (ArrayIndexOutOfBoundsException a) {
            //Caused by no data being returned from database.
            return null;
        }

    }

    /**
     * Gets all resource objects based upon a sql query.
     *
     * @param SQLQuery The SQL query to execute upon the database.
     *
     * @return An array of resources.
     *
     * @throws SQLException Thrown when the sql query is invalid.
     */
    public Resource[] getResourceList(String SQLQuery) throws SQLException {

        String[][] table;

        table = dbManager.getTupleListByQuery(SQLQuery);
        return constructResources(table);

    }

    /**
     * Gets all resource objects based upon a search on a column.
     *
     * @param selectColumn The column to search upon.
     * @param searchQuery The query to search on the column.
     *
     * @return An array of resource objects.
     *
     * @throws SQLException Thrown if column specified is incorrect or
     * connection to database couldn't be established.
     */
    public Resource[] searchResources(String selectColumn, String searchQuery) throws SQLException {

        String[][] table;
        table = dbManager.searchTuples("Resource", selectColumn, searchQuery);
        return constructResources(table);

    }

    /**
     * Returns Fine data for specified time periods.
     *
     * @param time All Time / Week / Day
     * @return A 2D array of tuples.
     * @throws SQLException Thrown when the sql query is invalid.
     */
    public String[][] getFineData(String time) throws SQLException {
        if (time.equals("All Time")) {
            try {
                String query = "SELECT Date, sum(-Amount) "
                        + "FROM TransactionHistory, FineHistory "
                        + "WHERE TransactionHistory.TranID = FineHistory.TranID GROUP BY Date";

                return dbManager.getTupleListByQuery(query);

            } catch (SQLException e) {
                return null;
            }
        } else if (time.equalsIgnoreCase("Week")) {
            try {
                String query = "SELECT Date, sum(-Amount) "
                        + "FROM TransactionHistory, FineHistory "
                        + "WHERE TransactionHistory.TranID = FineHistory.TranID "
                        + "AND Date BETWEEN (SELECT DATE('now', 'weekday 0', '-7 days')) AND (SELECT DATE('now')) GROUP BY Date";

                return dbManager.getTupleListByQuery(query);

            } catch (SQLException e) {
                return null;
            }

        } else if (time.equalsIgnoreCase("Day")) {
            try {
                String query = "SELECT Date, sum(-Amount) "
                        + "FROM TransactionHistory, FineHistory "
                        + "WHERE TransactionHistory.TranID = FineHistory.TranID "
                        + "AND Date BETWEEN (SELECT DATE('now', '-1 day')) AND (SELECT DATE('now')) GROUP BY Date";

                return dbManager.getTupleListByQuery(query);

            } catch (SQLException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Returns the most popular resource data .
     *
     * @return A 2D array of tuples.
     * @throws SQLException Thrown when the sql query is invalid.
     */
    public String[][] getPopularityData() throws SQLException {
        try {
            // create query to order by amount of borrow
            String query = "SELECT Title, Count(BorrowHistory.CID) FROM Resource, BorrowHistory, Copy "
                    + "WHERE Resource.RID = Copy.RID AND BorrowHistory.CID = Copy.CPID GROUP BY Copy.RID";

            return dbManager.getTupleListByQuery(query);

        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Returns popular resource data for a particular User ID.
     *
     * @param uid A user ID
     * @return A 2D array of tuples.
     * @throws SQLException Thrown when the sql query is invalid.
     */
    public String[][] getPopularityTimeData(int uid) throws SQLException {
        try {
            // create query to order by amount of borrow
            String query = "SELECT BorrowHistory.Date_Out, Count(BorrowHistory.CID) FROM Resource, BorrowHistory, Copy "
                    + "WHERE Resource.RID = Copy.RID AND BorrowHistory.CID = Copy.CPID AND BorrowHistory.UID = " + uid + " GROUP BY BorrowHistory.Date_Out";

            return dbManager.getTupleListByQuery(query);

        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Returns specified resource data for specified time periods.
     *
     * @param resource Book / Computer / DVD / Game
     * @param time All Time / Week / Day
     * @return A 2D array of tuples.
     * @throws SQLException Thrown when the sql query is invalid.
     */
    public String[][] getResourcePopularityData(String resource, String time) throws SQLException {
        if (time.equals("All Time")) {
            try {
                String query = "SELECT Title, Count(BorrowHistory.CID) FROM Resource, BorrowHistory, Copy, " + resource
                        + " WHERE Resource.RID = Copy.RID AND BorrowHistory.CID = Copy.CPID AND Resource.RID = "
                        + resource + ".RID GROUP BY Copy.RID";

                return dbManager.getTupleListByQuery(query);

            } catch (SQLException e) {
                return null;
            }
        } else if (time.equalsIgnoreCase("Week")) {
            try {
                // create query to order by amount of borrow
                String query = "SELECT Title, Count(BorrowHistory.CID) FROM Resource, BorrowHistory, Copy, " + resource
                        + " WHERE Resource.RID = Copy.RID AND BorrowHistory.CID = Copy.CPID AND Resource.RID = "
                        + resource + ".RID"
                        + " AND BorrowHistory.Date_Out BETWEEN (SELECT DATE('now', 'weekday 0', '-7 days')) AND (SELECT DATE('now')) GROUP BY Copy.RID";

                return dbManager.getTupleListByQuery(query);

            } catch (SQLException e) {
                return null;
            }

        } else if (time.equalsIgnoreCase("Day")) {
            try {
                // create query to order by amount of borrow
                String query = "SELECT Title, Count(BorrowHistory.CID) FROM Resource, BorrowHistory, Copy, " + resource
                        + " WHERE Resource.RID = Copy.RID AND BorrowHistory.CID = Copy.CPID AND Resource.RID = "
                        + resource + ".RID"
                        + " AND BorrowHistory.Date_Out BETWEEN (SELECT DATE('now', '-1 day')) AND (SELECT DATE('now')) GROUP BY Copy.RID";

                return dbManager.getTupleListByQuery(query);

            } catch (SQLException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Gets all resource objects based upon searches on the column(s).
     *
     * @param selectColumns The columns to search upon.
     * @param searchQueries The queries to search on the column(s).
     *
     * @return An array of resource objects.
     *
     * @throws SQLException Thrown if column specified is incorrect or
     * connection to database couldn't be established.
     */
    public Resource[] searchResources(String[] selectColumns, String[] searchQueries) throws SQLException {

        String[][] table;
        table = dbManager.searchTuples("Resource", selectColumns, searchQueries);
        return constructResources(table);

    }

    /**
     * Determines whether a copy exists or not.
     *
     * @param copyID The copy id of the copy.
     *
     * @return True if the copy exists. False if does not exist.
     *
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     */
    public boolean doesCopyExist(int copyID) throws SQLException {
        return dbManager.checkIfExist("Copy", new String[]{
            "CPID"
        },
                new String[]{
                    Integer.toString(copyID)
                });
    }

    /**
     * Gets the copy which corresponds to the copy id.
     *
     * @param copyID The copy id of the copy.
     *
     * @return The Copy.
     */
    public Copy getCopy(int copyID) throws IllegalArgumentException {

        try {

            //Get all copy rows based upon a rid
            String[] copyRow = dbManager.getFirstTupleByQuery("SELECT * FROM Copy WHERE CPID = "
                    + copyID);

            //Create copy from row information.
            //Return the create Copy.
            return new Copy(Integer.parseInt(copyRow[0]), Integer.parseInt(copyRow[1]),
                    Integer.parseInt(copyRow[2]), copyRow[3], Integer.parseInt(copyRow[4]),
                    Integer.parseInt(copyRow[5]));

        } catch (SQLException e) {
            return null;
        } catch (ArrayIndexOutOfBoundsException a) {
            return null;
        }

    }

    /**
     * Gets all copies.
     *
     * @return An array of all copies.
     */
    public Copy[] getCopies() {

        try {
            //Get all copy rows based upon a rid
            String[][] copyRows = dbManager.getTupleList("Copy");

            Copy[] copies = new Copy[copyRows.length];

            //For every copy associated with the rid, will be constructed and added to the array of copies.
            for (int iCount = 0; iCount < copyRows.length; iCount++) {
                copies[iCount] = new Copy(Integer.parseInt(copyRows[iCount][0]),
                        Integer.parseInt(copyRows[iCount][1]), Integer.parseInt(copyRows[iCount][2]),
                        copyRows[iCount][3], Integer.parseInt(copyRows[iCount][4]),
                        Integer.parseInt(copyRows[iCount][5]));
            }

            return copies;

        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets all copies of a resource.
     *
     * @param resourceID The resource id of a copy.
     *
     * @return All copies of a resource.
     */
    public Copy[] getCopies(int resourceID) {

        try {

            //Get all copy rows based upon a rid
            String[][] copyRows = dbManager.getTupleListByQuery("SELECT * FROM Copy WHERE RID = "
                    + resourceID);

            Copy[] copies = new Copy[copyRows.length];

            //For every copy associated with the rid, will be constructed and added to the array of copies.
            for (int iCount = 0; iCount < copyRows.length; iCount++) {
                copies[iCount] = new Copy(Integer.parseInt(copyRows[iCount][0]),
                        Integer.parseInt(copyRows[iCount][1]), Integer.parseInt(copyRows[iCount][2]),
                        copyRows[iCount][3], Integer.parseInt(copyRows[iCount][4]),
                        Integer.parseInt(copyRows[iCount][5]));
            }

            return copies;

        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Adds multiple copies of a resource to the database.
     *
     * @param newCopy The copy to add.
     *
     * @return True if addition was successful.
     */
    public boolean addBulkCopies(Copy newCopy) {

        return addBulkCopies(newCopy, DEFAULT_COPIES_PER_RESOURCE);

    }

    /**
     * Adds multiple copies of a resource to the database.
     *
     * @param newCopy The copy to add.
     * @param amount The amount of copies to add.
     *
     * @return True if addition was successful.
     */
    public boolean addBulkCopies(Copy newCopy, int amount) {

        boolean isSuccess = true;

        //For every copy to add, add the copy to the database.
        for (int iCount = 0; iCount < amount; iCount++) {
            //if addCopy failed then isSuccess must equal false.
            if (!addCopy(newCopy)) {
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    /**
     * Adds a copy of a resource to the database.
     *
     * @param newCopy The copy to add.
     *
     * @return True if operation was a success. False if failure.
     */
    public boolean addCopy(Copy newCopy) {
        try {
            //Add the copy to the database.
            dbManager.addTuple("Copy", new String[]{
                "null", Integer.toString(newCopy.getResourceID()),
                Integer.toString(newCopy.getLoanDuration()), "null",
                Integer.toString(newCopy.getStateID()), Integer.toString(newCopy.getCurrentBorrowerID()),
                "null", "null"
            });

            //ENQUEUE THE COPY
            //Get copy id.
            String copyID = dbManager.getFirstTupleByQuery("SELECT max(CPID) FROM Copy")[0];
            String resourceID = Integer.toString(newCopy.getResourceID());

            //check if there is an a item in the available queue.
            String[] queryResult = dbManager.getFirstTupleByQuery("SELECT HeadOfAvailableQueue, TailOfAvailableQueue"
                    + " FROM Resource WHERE RID = " + newCopy.getResourceID());
            String head = queryResult[0];
            String tail = queryResult[1];

            if (head == null) {
                //add copy to head and tail, the row is edited as the auto increment primary key must be found before
                //adding the id to the queue.
                dbManager.editTuple("Resource", new String[]{
                    "HeadOfAvailableQueue", "TailOfAvailableQueue"
                },
                        new String[]{
                            copyID, copyID
                        }, "RID", resourceID);
            } else {
                //insert at the tail.

                //assign the next queue item for the tail item.
                dbManager.editTuple("Copy", new String[]{
                    "NextCopyInQueue"
                }, new String[]{
                    copyID
                },
                        "CPID", tail);

                //assign the previous queue item for the new item.
                dbManager.editTuple("Copy", new String[]{
                    "PreviousCopyInQueue"
                }, new String[]{
                    tail
                },
                        "CPID", copyID);

                //assign the new tail.
                dbManager.editTuple("Resource", new String[]{
                    "TailOfAvailableQueue"
                },
                        new String[]{
                            copyID
                        }, "RID", resourceID);
            }

            return true;

        } catch (SQLException e) {

            //If an exception, return false to indicate failure to add copy.
            return false;

        }
    }

    /**
     * Edits/replaces an existing resource with a new resource.
     *
     * @param newResource The new resource with information to overwrite.
     * @param type The type of resource.
     *
     * @return Returns true if the operation was a success. False if failed.
     */
    private boolean editResource(Resource newResource, int type) {

        try {
            //get old resource data
            String[] resourceRow = dbManager.searchTuples("Resource", "RID",
                    Integer.toString(newResource.getResourceID()))[0];

            //edit resource in resource table
            dbManager.editTuple("Resource", new String[]{
                "RID", "Title", "RYear",
                "ImageID", "TID", "HeadOfAvailableQueue", "TailOfAvailableQueue",
                "HeadOfBorrowedQueue", "TailOfBorrowedQueue"
            },
                    new String[]{
                        Integer.toString(newResource.resourceID), encase(newResource.getTitle()),
                        Integer.toString(newResource.getYear()), Integer.toString(newResource.getThumbImage()),
                        Integer.toString(type), resourceRow[5], resourceRow[6], resourceRow[7], resourceRow[8]
                    },
                    "RID", Integer.toString(newResource.resourceID));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Edits/replaces an existing book resource with the passed new book.
     *
     * @param newResource The new book with information to overwrite.
     */
    public void editResource(Book newResource) {

        try {

            //Attempt to edit the resource table.
            boolean isEditSuccess = editResource(newResource, 1);

            //If the edit of the resource table was a success then continue editing.
            if (isEditSuccess) {
                //get old book data
                String[] bookRow = dbManager.searchTuples("Book", "RID",
                        Integer.toString(newResource.getResourceID()))[0];

                //edit book in book table
                dbManager.editTuple("Book", new String[]{
                    "BID", "RID", "Author", "Publisher",
                    "Genre", "ISBN"
                }, new String[]{
                    bookRow[0], Integer.toString(newResource.resourceID),
                    encase(newResource.getAuthor()), encase(newResource.getPublisher()),
                    encase(newResource.getGenre()), encase(newResource.getIsbn())
                }, "RID",
                        Integer.toString(newResource.resourceID));

                dbManager.deleteTuple("ResourceLanguage", new String[]{
                    "RID"
                },
                        new String[]{
                            Integer.toString(newResource.resourceID)
                        });

                assignResourceLanguage(Integer.toString(newResource.resourceID), newResource.getLang());

            } else {
            }

        } catch (SQLException e) {
        }
    }

    /**
     * Edits/replaces an existing dvd resource with the passed new dvd.
     *
     * @param newResource The new dvd with information to overwrite.
     */
    public void editResource(Dvd newResource) {

        try {

            //Attempt to edit the resource table.
            boolean isEditSuccess = editResource(newResource, 2);

            //If the edit of the resource table was a success then continue editing.
            if (isEditSuccess) {

                //get old dvd data
                String[] dvdRow = dbManager.searchTuples("Dvd", "RID",
                        Integer.toString(newResource.getResourceID()))[0];

                //edit book in dvd table
                dbManager.editTuple("Dvd", new String[]{
                    "DID", "RID", "Director", "Runtime"
                },
                        new String[]{
                            dvdRow[0], Integer.toString(newResource.resourceID),
                            encase(newResource.getDirector()), Integer.toString(newResource.getRunTime())
                        },
                        "RID", Integer.toString(newResource.resourceID));

                //remove language
                dbManager.deleteTuple("ResourceLanguage", new String[]{
                    "RID"
                },
                        new String[]{
                            Integer.toString(newResource.resourceID)
                        });

                //add language
                assignResourceLanguage(Integer.toString(newResource.resourceID), newResource.getLanguage());

                //remove subtitles
                dbManager.deleteTuple("DvdSubtitleLanguage", new String[]{
                    "RID"
                },
                        new String[]{
                            Integer.toString(newResource.resourceID)
                        });

                //add subtitles
                assignSubtitleLanguages(Integer.toString(newResource.resourceID), newResource.getSubLang());

            } else {
            }

        } catch (SQLException e) {
        }
    }

    /**
     * Adds a resource image directory and returns the image id.
     *
     * @param imageURL The image directory of the image file.
     *
     * @throws SQLException Thrown if connection to database could not be
     * established.
     */
    public void addResourceImage(String imageURL) throws SQLException {
        dbManager.addTuple("Image", new String[]{
            "null", encase(relativeUrl(imageURL)), "0"});
        Integer.parseInt(dbManager.getFirstTupleByQuery("Select max(ImageID) FROM Image")[0]);
    }

    /**
     * Adds a avatar image directory and returns the image id.
     *
     * @param imageURL The image directory of the image file.
     *
     * @return The image id of the newly added image.
     *
     * @throws SQLException Thrown if connection to database could not be
     * established.
     */
    public int addAvatarImage(String imageURL) throws SQLException {
        dbManager.addTuple("Image", new String[]{
            "null", encase(relativeUrl(imageURL)), "1"
        });
        return Integer.parseInt(dbManager.getFirstTupleByQuery("Select max(ImageID) FROM Image")[0]);
    }

    /**
     * Gets the image directory url for the image associated with the image id.
     *
     * @param imageID The image id of the image.
     *
     * @return The image directory url.
     *
     * @throws SQLException Thrown if failed to connect to database.
     * @throws IllegalArgumentException Thrown if imageID does not exist.
     */
    public String getImageURL(int imageID) throws SQLException, IllegalArgumentException {
        //if image exists then return image dir url.
        if (isImageExist(imageID)) {
            return dbManager.getFirstTupleByQuery("SELECT Image_Address FROM Image WHERE ImageID = "
                    + imageID)[0];
        } else {
            throw new IllegalArgumentException("Specified image does not exist");
        }
    }

    /**
     * Gets the image id of the image which points to the url.
     *
     * @param url The url directory of the image.
     *
     * @return The image id of the image.
     *
     * @throws SQLException Thrown if failed to connect to database.
     * @throws IllegalArgumentException Thrown if imageID does not exist.
     */
    public int getImageID(String url) throws SQLException, IllegalArgumentException {
        //if image exists then return image id.
        url = relativeUrl(url);

        if (isImageExist(url)) {
            return Integer.parseInt(dbManager.getFirstTupleByQuery(
                    "SELECT ImageID FROM Image WHERE Image_Address = " + encase(url))[0]);
        } else {
            addResourceImage(url);
            return getImageID(url);
        }
    }

    /**
     * Returns relative to the project image location. If location is already
     * relative returns input.
     *
     * @param url Full url path of the image.
     *
     * @return relative location to the project.
     */
    public String relativeUrl(String url) {
        for (int i = 5; i < url.length(); i++) {
            if (url.substring(i - 5, i).equalsIgnoreCase("/src/")) {
                return url.substring(i - 1);
            }
        }

        //Relative url not found -> either url is relative already or file is missing
        return url;
    }

    /**
     * Determines whether an specified image exists or not by the image id.
     *
     * @param imageID The image id of the image.
     *
     * @return True if the image exists. False if not.
     *
     * @throws SQLException Thrown if connection to database could not be
     * established.
     */
    public boolean isImageExist(int imageID) throws SQLException {
        return dbManager.checkIfExist("Image", new String[]{
            "ImageID"
        },
                new String[]{
                    Integer.toString(imageID)
                });
    }

    /**
     * Determines whether an specified image exists or not by the image url dir.
     *
     * @param url The url directory for the image.
     *
     * @return True if the image exists. False if not.
     *
     * @throws SQLException Thrown if connection to database could not be
     * established.
     */
    public boolean isImageExist(String url) throws SQLException {
        return dbManager.checkIfExist("Image", new String[]{"Image_Address"}, new String[]{encase(url)});
    }

    /**
     * Edits/replaces an existing computer resource with the passed new
     * computer.
     *
     * @param newResource The new computer with information to overwrite.
     */
    public void editResource(Computer newResource) {

        try {

            //Attempt to edit the resource table.
            boolean isEditSuccess = editResource(newResource, 3);

            //If the edit of the resource table was a success then continue editing.
            if (isEditSuccess) {

                //get old computer data
                String[] computerRow = dbManager.searchTuples("Computer", "RID",
                        Integer.toString(newResource.getResourceID()))[0];

                //edit book in computer table
                dbManager.editTuple("Computer", new String[]{
                    "CID", "RID", "Manufacturer",
                    "Model", "Installed_OS"
                },
                        new String[]{
                            computerRow[0], Integer.toString(newResource.resourceID),
                            encase(newResource.getManufacturer()), encase(newResource.getModel()),
                            encase(newResource.getOs())
                        }, "RID", Integer.toString(newResource.resourceID));

            } else {
            }

        } catch (SQLException e) {
        }

    }

    /**
     * Edits/replaces an existing video game resource with the passed new video
     * game.
     *
     * @param newResource The new video game with information to overwrite.
     */
    public void editResource(VideoGame newResource) {

        try {

            //Attempt to edit the resource table.
            boolean isEditSuccess = editResource(newResource, 4);

            //If the edit of the resource table was a success then continue editing.
            if (isEditSuccess) {

                //get old computer data
                String[] videoGameRow = dbManager.searchTuples("VideoGame", "RID",
                        Integer.toString(newResource.getResourceID()))[0];

                //edit book in computer table
                dbManager.editTuple("VideoGame", new String[]{"CID", "RID", "Publisher",
                    "Genre", "CertRating", "Multiplayer"},
                        new String[]{videoGameRow[0], Integer.toString(newResource.resourceID),
                            encase(newResource.getPublisher()), encase(newResource.getGenre()),
                            encase(newResource.getCertRating()), encase(newResource.getMultiplayer())}, "RID", Integer.toString(newResource.resourceID));

            } else {
            }

        } catch (SQLException e) {
        }

    }

    /**
     * Adds a new book to the database.
     *
     * @param newBook The new book to add.
     *
     * @throws ResourceDuplicateException Thrown if a duplicate resource is
     * trying to be added.
     */
    public void addResource(Book newBook) throws ResourceDuplicateException {

        /*
        Stage indicates how far the progression of the operation has gone. If an exception were to be thrown,
        the database must be reverted.
         */
        int stage = 0;

        String resourceID = "0";

        //The id of the language, a string is used as will only be used in sqlQueries.
        String langID = "0";

        try {

            //if the resource doesnt exist then add to the database. Otherwise return false.
            if (!dbManager.checkIfExist("Resource", new String[]{"Title", "RYear"},
                    new String[]{encase(newBook.getTitle()), Integer.toString(newBook.getYear())})) {
                //The resource type is 1 corresponding to a book.
                int bookTypeID = 1;

                //get the resourceID of the resource by getting the largest primary key in Resource.
                resourceID = dbManager.getFirstTupleByQuery("SELECT max(RID) FROM Resource")[0];

                //Add the the resource to the resource table.
                dbManager.addTuple("Resource", new String[]{
                    null, encase(newBook.title),
                    Integer.toString(newBook.year), Integer.toString(newBook.thumbImageID),
                    Integer.toString(bookTypeID), "null", "null", "null", "null", encase(SceneController.getCurrentDateTime())
                });

                stage = 1;

                //add book to book table.
                dbManager.addTuple("Book", new String[]{
                    "null", resourceID,
                    encase(newBook.getAuthor()), encase(newBook.getPublisher()), encase(newBook.getGenre()),
                    encase(newBook.getIsbn())
                });

                stage = 2;

                //Assign the language to the resource, returning its new id.
                langID = assignResourceLanguage(resourceID, newBook.getLang());

                stage = 3;

            } else {
                //Book already exists.
                throw new ResourceDuplicateException();
            }
        } catch (SQLException e) {

            System.out.println("Error encounterd at stage " + stage);
            //Revert the database to the previous state.
            /*
            break is not used in cases as for every stage increased, the previous stages must be reverted
            from the database.
             */
            try {
                switch (stage) {
                    case 3: //del language entry in db.
                        dbManager.deleteTuple("ResourceLanguage", new String[]{"RID", "LangID"}, new String[]{resourceID, langID});
                    case 2: //del book entry in db.
                        dbManager.deleteTuple("Book", new String[]{"RID"}, new String[]{resourceID});
                    case 1: //del resource entry in db.
                        dbManager.deleteTuple("Resource", new String[]{"RID"}, new String[]{resourceID});
                    default:
                        break;
                }

            } catch (IllegalArgumentException f) {
                //Arguments are incorrect which suggests table doesn't exist or exception should have
                // been thrown after stage increase!
            } catch (SQLException f) {
                //Clearly the database tables don't exist or database file has moved!
            }
        }

    }

    /**
     * Gets the resource id of the last resource added.
     *
     * @return The resource id.
     *
     * @throws SQLException Thrown if connection to database could not be
     * established.
     * @throws IllegalStateException Thrown if there exist no resources.
     */
    public int getLastAddedID() throws SQLException, IllegalStateException {

        try {
            return Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT max(RID) FROM Resource")[0]);
        } catch (IndexOutOfBoundsException i) {
            throw new IllegalStateException("No resource data exists");
        }

    }

    /**
     * Adds a new dvd to the database.
     *
     * @param newDvd The new dvd to add.
     *
     * @throws ResourceDuplicateException Thrown if a duplicate resource is
     * trying to be added.
     */
    public void addResource(Dvd newDvd) throws ResourceDuplicateException {

        /*
        Stage indicates how far the progression of the operation has gone. If an exception
        were to be thrown, the database must be reverted.
         */
        int stage = 0;

        String resourceID = "";

        //The id of the language, a string is used as will only be used in sqlQueries.
        String langID = "";
        String[] subLangID = null;

        try {

            //if the resource doesnt exist then add to the database. Otherwise return false.
            if (!dbManager.checkIfExist("Resource", new String[]{
                "Title", "RYear"
            }, new String[]{
                encase(newDvd.getTitle()), Integer.toString(newDvd.getYear())
            })) {
                //The resource type is 1 corresponding to a Dvd.
                int dvdTypeID = 2;

                //Add the the resource to the resource table.
                dbManager.addTuple("Resource", new String[]{
                    "null", encase(newDvd.title),
                    Integer.toString(newDvd.year), Integer.toString(newDvd.thumbImageID),
                    Integer.toString(dvdTypeID), "null", "null", "null", "null", encase(SceneController.getCurrentDateTime())
                });

                //get the resourceID of the resource by getting the largest primary key in Resource.
                resourceID = dbManager.getFirstTupleByQuery("SELECT max(RID) FROM Resource")[0];

                stage = 1;

                //add Dvd to Dvd table.
                dbManager.addTuple("Dvd", new String[]{
                    "null", resourceID,
                    encase(newDvd.getDirector()), Integer.toString(newDvd.getRunTime())
                });

                stage = 2;

                //Assign the language to the resource, returning its new lang ID.
                langID = assignResourceLanguage(resourceID, newDvd.getLanguage());

                stage = 3;

                //Assign the subtitle languages to the resource, returning its new subLangIDs.
                subLangID = assignSubtitleLanguages(resourceID, newDvd.getSubLang());

                stage = 4;

            } else {
                //Dvd already exists.
                throw new ResourceDuplicateException();
            }
        } catch (SQLException e) {

            //Revert the database to the previous state.
            /*
            break is not used in cases as for every stage increased, the previous stages must be reverted
            from the database.
             */
            try {
                switch (stage) {
                    case 4: //del every sub languages entry in db.
                        for (int iCount = 0; iCount < subLangID.length; iCount++) {
                            dbManager.deleteTuple("DvdSubtitleLanguage", new String[]{
                                "RID", "SubID"
                            },
                                    new String[]{
                                        resourceID, subLangID[iCount]
                                    });
                        }
                    case 3: //del language entry in db.
                        dbManager.deleteTuple("ResourceLanguage", new String[]{
                            "RID", "LangID"
                        }, new String[]{
                            resourceID, langID
                        });
                    case 2: //del book entry in db.
                        dbManager.deleteTuple("Dvd", new String[]{
                            "RID"
                        }, new String[]{
                            resourceID
                        });
                    case 1: //del resource entry in db.
                        dbManager.deleteTuple("Resource", new String[]{
                            "RID"
                        }, new String[]{
                            resourceID
                        });
                    default:
                        break;
                }

            } catch (IllegalArgumentException f) {
                //Arguments are incorrect which suggests table doesn't exist or exception should have
                // been thrown after stage increase!
            } catch (SQLException f) {
                //Clearly the database tables don't exist or database file has moved!
            }
        }

    }

    /**
     * Adds a new computer to the database.
     *
     * @param newComputer The new computer to add.
     *
     * @throws ResourceDuplicateException Thrown if a duplicate resource is
     * trying to be added.
     */
    public void addResource(Computer newComputer) throws ResourceDuplicateException {

        /*
        Stage indicates how far the progression of the operation has gone. If an exception were to be thrown,
        the database must be reverted.
         */
        int stage = 0;

        String resourceID = "";

        //The id of the language, a string is used as will only be used in sqlQueries.
        String langID = "";
        String[] subLangID = null;

        try {

            //if the resource doesnt exist then add to the database. Otherwise return false.
            if (!dbManager.checkIfExist("Resource", new String[]{
                "Title", "RYear"
            }, new String[]{
                encase(newComputer.getTitle()), Integer.toString(newComputer.getYear())
            })) {
                //The resource type is 1 corresponding to a Computer.
                int computerTypeID = 3;

                //Add the the resource to the resource table.
                dbManager.addTuple("Resource", new String[]{
                    "null", encase(newComputer.title),
                    Integer.toString(newComputer.year), Integer.toString(newComputer.thumbImageID),
                    Integer.toString(computerTypeID), "null", "null", "null", "null", encase(SceneController.getCurrentDateTime())
                });

                //get the resourceID of the resource by getting the largest primary key in Resource.
                resourceID = dbManager.getFirstTupleByQuery("SELECT max(RID) FROM Resource")[0];

                stage = 1;

                //add Computer to Computer table.
                dbManager.addTuple("Computer", new String[]{
                    "null", resourceID,
                    encase(newComputer.getManufacturer()), encase(newComputer.getModel()),
                    encase(newComputer.getOs())
                });

                stage = 2;

            } else {
                //Computer already exists.
                throw new ResourceDuplicateException();
            }
        } catch (SQLException e) {

            //Revert the database to the previous state.
            /*
            break is not used in cases as for every stage increased, the previous stages must be reverted
            from the database.
             */
            try {
                switch (stage) {
                    case 2: //del book entry in db.
                        dbManager.deleteTuple("Computer", new String[]{
                            "RID"
                        }, new String[]{
                            resourceID
                        });
                    case 1: //del resource entry in db.
                        dbManager.deleteTuple("Resource", new String[]{
                            "RID"
                        }, new String[]{
                            resourceID
                        });
                        break;
                }

            } catch (IllegalArgumentException f) {
                //Arguments are incorrect which suggests table doesn't exist or exception should have
                // been thrown after stage increase!
            } catch (SQLException f) {
                //Clearly the database tables don't exist or database file has moved!
            }
        }

    }

    /**
     * Adds a new video game to the database.
     *
     * @param newVideoGame The new video game to add.
     * @throws ResourceDuplicateException Thrown if a duplicate resource is
     * trying to be added.
     */
    public void addResource(VideoGame newVideoGame) throws ResourceDuplicateException {
        /*
        Stage indicates how far the progression of the operation has gone. If an exception were to be thrown,
        the database must be reverted.
         */

        int stage = 0;

        String resourceID = "";

        try {

            //if the resource doesnt exist then add to the database. Otherwise return false.
            if (!dbManager.checkIfExist("Resource", new String[]{"Title", "RYear"}, new String[]{encase(newVideoGame.getTitle()), Integer.toString(newVideoGame.getYear())})) {
                //The resource type is 1 corresponding to a VideoGame.
                int videoGameTypeID = 4;

                //Add the the resource to the resource table.
                dbManager.addTuple("Resource", new String[]{"null", encase(newVideoGame.title),
                    Integer.toString(newVideoGame.year), Integer.toString(newVideoGame.thumbImageID),
                    Integer.toString(videoGameTypeID), "null", "null", "null", "null", encase(SceneController.getCurrentDateTime())});

                //get the resourceID of the resource by getting the largest primary key in Resource.
                resourceID = dbManager.getFirstTupleByQuery("SELECT max(RID) FROM Resource")[0];

                stage = 1;

                //add VideoGame to VideoGame table.
                dbManager.addTuple("VideoGame", new String[]{"null", resourceID,
                    encase(newVideoGame.getPublisher()), encase(newVideoGame.getGenre()),
                    encase(newVideoGame.getCertRating()), encase(newVideoGame.getMultiplayer())}); // THIS IS BROKE

                stage = 2;

            } else {
                //Computer already exists.
                throw new ResourceDuplicateException();
            }
        } catch (SQLException e) {

            //Revert the database to the previous state.
            /*
            break is not used in cases as for every stage increased, the previous stages must be reverted
            from the database.
             */
            try {
                switch (stage) {
                    case 2: //del VideoGame entry in db.
                        dbManager.deleteTuple("VideoGame", new String[]{"RID"}, new String[]{resourceID});
                    case 1: //del resource entry in db.
                        dbManager.deleteTuple("Resource", new String[]{"RID"}, new String[]{resourceID});
                        break;
                }

            } catch (IllegalArgumentException f) {
                //Arguments are incorrect which suggests table doesn't exist or exception should have
                // been thrown after stage increase!
            } catch (SQLException f) {
                //Clearly the database tables don't exist or database file has moved!
            }
        }

    }

    /**
     * Assigns a language to the resource in the database.
     *
     * @param rid Identifies which resource to assign the language to.
     * @param language The language to assign. If it does not already exist it
     * will be added to the database.
     *
     * @return Returns the unique identifier for language.
     *
     * @throws SQLException Thrown if the resource specified was not found or
     * connection to database could not be established.
     */
    private String assignResourceLanguage(String rid, String language) throws SQLException {

        //Uniquely identifies the language.
        String langID;

        //check if language already exists in db.
        String[][] result = dbManager.searchTuples("Language", "Language", language);

        //if length is greater than 0, then the language must exist. Otherwise add the language.
        if (result.length > 0) {
            //set the langID where langID is the first column in the row. The first row is taken.
            langID = result[0][0];
        } else {
            //Adding new language
            dbManager.addTuple("Language", new String[]{
                "null", encase(language)
            });
            //Set langID
            langID = dbManager.getFirstTupleByQuery("SELECT max(LangID) FROM Language")[0];
        }

        //Now make the association between the langID and RID
        dbManager.addTuple("ResourceLanguage", new String[]{
            rid,
            langID
        });

        return langID;

    }

    /**
     * Assigns multiple subtitle languages to the resource in the database.
     *
     * @param rid Identifies which resource to assign the subtitle languages to.
     * @param subtitleLang The subtitle languages to assign. If they do not
     * exist, they will be added to the database.
     *
     * @return Returns an array of unique identifiers for the subtitle
     * languages.
     *
     * @throws SQLException Thrown if the resource specified was not found or
     * connection to database could not be established.
     */
    private String[] assignSubtitleLanguages(String rid, String[] subtitleLang) throws SQLException {

        //Uniquely identifies the subtitle languages.
        String[] langID = new String[subtitleLang.length];

        //for every subtitle language, assign the language to the resource.
        for (int iCount = 0; iCount < subtitleLang.length; iCount++) {

            //check if language already exists in db.
            String[][] result = dbManager.searchTuples("SubtitleLanguage", "Subtitle_Language",
                    subtitleLang[iCount]);

            //if length is greater than 0, then the language must exist. Otherwise add the language.
            if (result.length > 0) {
                //set the langID where langID is the first column in the row. The first row is taken.
                langID[iCount] = result[0][0];
            } else {
                //Adding new subtitle language.
                dbManager.addTuple("SubtitleLanguage", new String[]{
                    "null", encase(subtitleLang[iCount])
                });
                //Set the langID.
                langID[iCount] = dbManager.getFirstTupleByQuery("SELECT max(SubID) FROM SubtitleLanguage")[0];
            }

            //Now make the association between the langID and RID
            dbManager.addTuple("DvdSubtitleLanguage", new String[]{
                rid,
                langID[iCount]
            });

        }

        return langID;

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
     * Constructs multiple resources to their respective type from a table of
     * resource data and then returns them.
     *
     * @param table The data in which to construct the resource objects from.
     * @return The array of resources.
     */
    private Resource[] constructResources(String[][] table) {

        //an ArrayList is used as some rows may not be constructed in to resources. Therefore the size is unknown.
        ArrayList<Resource> resources = new ArrayList<>();

        //The column position which identifies the type id of a resource.
        final int TID_ATTRIBUTE_POSITION = 4;

        //for every row in the table construct a resource.
        for (String[] row : table) {

            Resource newResource = null;

            //determine whether the resource is a book, dvd or computer.
            if (row[TID_ATTRIBUTE_POSITION].equals("1")) {
                newResource = constructBook(row);
            } else if (row[TID_ATTRIBUTE_POSITION].equals("2")) {
                newResource = constructDvd(row);
            } else if (row[TID_ATTRIBUTE_POSITION].equals("3")) {
                newResource = constructComputer(row);
            } else if (row[TID_ATTRIBUTE_POSITION].equals("4")) {
                newResource = constructVideoGame(row);
            }

            //if the newly constructed resource isn't null, add the ArrayList.
            if (newResource != null) {
                resources.add(newResource);
            }

        }

        //convert the ArrayList to an array.
        return resources.toArray(new Resource[resources.size()]);
    }

    /**
     * Creates a Book object from the database and passed data.
     *
     * @param row The row data from resources.
     *
     * @return The constructed Book object.
     */
    private Book constructBook(String[] row) {

        //dateCreated should be 9
        //The resource id.
        int rid = Integer.parseInt(row[0]);

        //Data from the book table.
        String[] bookExtraData = getBookData(rid);

        //If the passed row and the fetched book data not null then construct the book, otherwise return null.
        if (bookExtraData != null && row != null) {

            try {
                //Get the language corresponding to the resource.
                String language = getLanguage(rid);

                //Return the constructed book.
                return new Book(rid, row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[9],
                        bookExtraData[0], bookExtraData[1], bookExtraData[2], bookExtraData[3], language);

            } catch (ArrayIndexOutOfBoundsException e) {
                //returns null if data is invalid.
                return null;
            }

        } else {
            //returns null if the passed data is null or book data could not be retrieved.
            return null;
        }

    }

    /**
     * Creates a Dvd object from the database and passed data.
     *
     * @param row The row data from resources.
     *
     * @return The constructed Dvd object.
     */
    private Dvd constructDvd(String[] row) {

        //The resource id.
        int rid = Integer.parseInt(row[0]);

        //Data from the dvd table.
        String[] dvdExtraData = getDvdData(rid);

        //If the passed row and the fetched dvd data not null then construct the dvd, otherwise return null.
        if (dvdExtraData != null && row != null) {

            try {

                //Get the language corresponding to the resource.
                String language = getLanguage(rid);

                //Get the subtitle languages corresponding to the resource.
                String[] subtitleLanguages = getSubtitleLanguages(rid);

                //If the subtitle languages does not equal null
                if (subtitleLanguages != null) {

                    //Return the constructed dvd.
                    return new Dvd(rid, row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[9],
                            dvdExtraData[0], Integer.parseInt(dvdExtraData[1]), language, subtitleLanguages);

                } else {
                    //returns null if subtitle languages could not be found.
                    return null;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                //returns null if data is invalid.
                return null;
            }

        } else {
            //returns null if the passed data is null or dvd data could not be retrieved.
            return null;
        }

    }

    /**
     * Creates a Computer object from the database and passed data.
     *
     * @param row The row data from resources.
     *
     * @return The constructed Computer object.
     */
    private Computer constructComputer(String[] row) {

        //The resource id.
        int rid = Integer.parseInt(row[0]);

        //Data from the computer table.
        String[] computerExtraData = getComputerData(rid);

        //If the passed row and the fetched computer data not null then construct the computer, otherwise return null.
        if (computerExtraData != null && row != null) {

            try {
                return new Computer(rid, row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[9],
                        computerExtraData[0], computerExtraData[1], computerExtraData[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
                //returns null if data is invalid.
                return null;
            }

        } else {
            //returns null if the passed data is null or computer data could not be retrieved.
            return null;
        }

    }

    /**
     * Creates a VideoGame object from the database and passed data.
     *
     * @param row The row data from resources.
     * @return The constructed VideoGame object.
     */
    private VideoGame constructVideoGame(String[] row) {
        //The resource id.
        int rid = Integer.parseInt(row[0]);

        //Data from the computer table.
        String[] videoGameExtraData = getVideoGameData(rid);

        //If the passed row and the fetched computer data not null then construct the computer, otherwise return null.
        if (videoGameExtraData != null && row != null) {

            try {
                return new VideoGame(rid, row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[9],
                        videoGameExtraData[0], videoGameExtraData[1], videoGameExtraData[2], videoGameExtraData[3]);
            } catch (ArrayIndexOutOfBoundsException e) {
                //returns null if data is invalid.
                return null;
            }

        } else {
            //returns null if the passed data is null or computer data could not be retrieved.
            return null;
        }

    }

    /**
     * Gets the language of the resource.
     *
     * @param rid Uniquely identifies the resource.
     *
     * @return The language of the resource.
     */
    private String getLanguage(int rid) {

        try {

            //Get the language of the resource.
            String[] row = dbManager.getFirstTupleByQuery("SELECT Language FROM ResourceLanguage, Language WHERE "
                    + rid + " = RID AND ResourceLanguage.LangID = Language.LangID");

            //If the row found is not null then return the language, otherwise return an empty string.
            if (row != null) {
                return row[0];
            } else {
                return "";
            }
        } catch (SQLException e) {
            //Return an empty string if exception. Most likely will be caused by connection failure or missing table.
            return "";
        }

    }

    /**
     * Gets all subtitle languages of a resource.
     *
     * @param rid Uniquely identifies the resource.
     *
     * @return All subtitle languages of the resource.
     */
    private String[] getSubtitleLanguages(int rid) {

        try {

            //Get the table data for the subtitle languages.
            String[][] table = dbManager.getTupleListByQuery("SELECT Subtitle_Language FROM DvdSubtitleLanguage, "
                    + "SubtitleLanguage WHERE "
                    + rid + " = RID AND DvdSubtitleLanguage.SubID = SubtitleLanguage.SubID");

            String[] subLanguages = new String[table.length];

            //For every row, add the subtitle language from the first column to the array of strings.
            for (int iCount = 0; iCount < table.length; iCount++) {
                subLanguages[iCount] = table[iCount][0];
            }

            return subLanguages;

        } catch (SQLException e) {

            //Return null if an exception has occurred.
            return null;

        }

    }

    /**
     * Gets the book data using a resource id.
     *
     * @param rid Uniquely identifies the resource.
     *
     * @return The row of book data.
     */
    private String[] getBookData(int rid) {

        try {
            //Create the row
            return dbManager.getFirstTupleByQuery("SELECT Author, Publisher, Genre, ISBN "
                    + "FROM Book WHERE RID = " + rid + ";");
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the dvd data using a resource id.
     *
     * @param rid Uniquely identifies the resource.
     *
     * @return The row of dvd data.
     */
    private String[] getDvdData(int rid) {

        try {
            //Create the row
            return dbManager.getFirstTupleByQuery("SELECT Director, Runtime "
                    + "FROM Dvd WHERE RID = " + rid + ";");
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the computer data using a resource id.
     *
     * @param rid Uniquely identifies the resource.
     *
     * @return The row of computer data.
     */
    private String[] getComputerData(int rid) {

        try {
            //Create the row
            return dbManager.getFirstTupleByQuery("SELECT Manufacturer, Model, Installed_OS "
                    + "FROM Computer WHERE RID = " + rid + ";");
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the video game data using a resource id.
     *
     * @param rid Uniquely identifies the resource.
     * @return The row of video game data.
     */
    private String[] getVideoGameData(int rid) {

        try {
            //Create the row
            String query = "SELECT Publisher, Genre, CertRating, Multiplayer "
                    + "FROM VideoGame WHERE RID = " + rid + ";";

            return dbManager.getFirstTupleByQuery(query);
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Adds a new review to the database.
     *
     * @param resId the resource Id of the resource being reviewed
     * @param rating the rating of the resource
     * @param review the review of the resource
     *
     */
    public void addReview(int resId, int rating, String review) {
        try {
            //Add the review to the resource review table
            //If review is empty, add null to the table
            if (review.isEmpty()) {
                dbManager.addTuple("ResourceReview", new String[]{"null", Integer.toString(resId),
                    Integer.toString(SceneController.USER_ID), Integer.toString(rating), "null"});
            } else {
                dbManager.addTuple("ResourceReview", new String[]{"null", Integer.toString(resId),
                    Integer.toString(SceneController.USER_ID), Integer.toString(rating), encase(review)});
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * Gets the review corresponding to the review ID
     *
     * @param reviewId The review id of the review.
     * @return The Review.
     */
    public Review getReview(int reviewId) throws IllegalArgumentException {

        try {
            //Get review based of reviewId
            String[] reviewRow = dbManager.getFirstTupleByQuery("SELECT * FROM ResourceReview WHERE ReviewId = "
                    + reviewId);

            //Return the created Review.
            return new Review(Integer.parseInt(reviewRow[0]), Integer.parseInt(reviewRow[1]),
                    Integer.parseInt(reviewRow[2]), Integer.parseInt(reviewRow[3]), reviewRow[4]);

        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Gets the resources that were created after date.
     *
     * @param userID current user.
     * @param am Reference to account manager.
     * @return The list of resources created after date.
     */
    public Resource[] getNewAdditions(int userID, AccountManager am) {
        //get the last login for userID
        User user = null;
        if (am.isExist(userID)) {
            user = am.getAccount(userID);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date lastLogin = null;
        try {
            lastLogin = format.parse(user.getLastLogin());
        } catch (ParseException e) {
            //last Login couldn't be formatted
        }

        //get list of all resources
        Resource[] allResources = getResourceList();
        //arraylist to store the filtered resources
        ArrayList<Resource> newAdditions = new ArrayList<Resource>();

        //for every resource found
        for (Resource res : allResources) {
            //format the date created to Date
            Date dateCreated = null;

            try {
                dateCreated = format.parse(res.getdateCreated());
                //if the resource was created after the last login
                if (dateCreated.after(lastLogin) || dateCreated.equals(lastLogin)) {
                    //add to the filtered arrayList
                    newAdditions.add(res);
                }

            } catch (ParseException e) {
                //date created couldn't be formatted
            }

        }

        //convert arraylist to array
        Resource[] additions = new Resource[newAdditions.size()];

        for (int i = 0; i < newAdditions.size(); i++) {
            additions[i] = newAdditions.get(i);
        }

        //return list
        return additions;

    }

    /**
     * Gets the resources that were created after date. Filtered by resource
     * type.
     *
     * @param userID current user.
     * @param am Reference to account manager.
     * @param resType Resource type.
     * @param dm Reference to database manager.
     * @return The list of resources created after date.
     * @throws SQLException database error.
     */
    public Resource[] getNewAdditionsResType(int userID, AccountManager am, int resType, DatabaseManager dm) throws SQLException {
        Resource[] allResources = getNewAdditions(userID, am);
        ArrayList<Resource> temp = new ArrayList<Resource>();
        for (Resource res : allResources) {
            String tableName = "";

            switch (resType) {
                case 1:
                    //book
                    tableName = "Book";
                    break;
                case 2:
                    //dvd
                    tableName = "DVD";
                    break;
                case 3:
                    //laptop
                    tableName = "Laptop";
                    break;
                case 4:
                    //videogame
                    tableName = "VideoGame";
                    break;
                default:
                    System.out.println("invalid resource type");
            }

            //setup the checkIfExists parameters
            String[] keys = {"RID"};
            String[] values = {String.valueOf(res.getResourceID())};

            //check if its the correct type
            Boolean isCorrectType = dm.checkIfExist(tableName, keys, values);

            //if its the correct type add it to the temp arrayList
            if (isCorrectType) {
                temp.add(res);
            }
        }

        Resource[] resourceList = (Resource[]) temp.toArray();
        return resourceList;
    }

}
