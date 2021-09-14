package Core;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Responsible for performing operations on accounts.
 *
 * @author Noah Lenagan, Paris, Paris Kelly Skopelitis
 * @author Matt LLewellyn(V.2.0)
 * @version 2.0
 */
public class AccountManager {

    /**
     * An instance of DatabaseManager.
     */
    private final DatabaseManager dbManager;

    /**
     * Constructor.
     *
     * @param dbManager
     */
    public AccountManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Deletes an account.
     *
     * @param userID The user id of the account to delete.
     *
     * @throws IllegalArgumentException Thrown if the specified user does not
     * exist.
     * @throws SQLException Thrown if connection to the database failed or the
     * table doesn't exist.
     * @throws IllegalStateException Thrown if the user has fines to pay.
     */
    public void deleteAccount(int userID) throws IllegalArgumentException, SQLException, IllegalStateException {

        //check if account exists
        if (dbManager.checkIfExist("User", new String[]{
            "UID"
        }, new String[]{
            Integer.toString(userID)
        })) {

            //check if account balance is greater or equal to 0
            if (getAccountBalance(userID) >= 0.0F) {
                int resourceCap = getUserResourceCap(userID);
                if (resourceCap == 0) {
                    //check if user has borrowed any borrowed books
                    if (dbManager.getFirstTupleByQuery("SELECT count(*) FROM Copy WHERE UID = "
                            + userID)[0].equals("0")) {

                        //remove from request queue
                        dbManager.sqlQuery("DELETE FROM ResourceRequestQueue WHERE UID = " + userID);

                        //remove any reserved resources
                        String[][] copies = dbManager.getTupleListByQuery("SELECT CPID, RID FROM "
                                + "Copy WHERE UID = " + userID);

                        //if there are reserved copies, then add them to the availability queue.
                        if (copies.length > 0) {
                            //remove reserved copies from ReservedResource table.
                            dbManager.deleteTuple("ReservedResource", new String[]{
                                "UID"
                            },
                                    new String[]{
                                        Integer.toString(userID)
                                    });

                            //get queue tail
                            String tail = dbManager.getFirstTupleByQuery("SELECT TailOfAvailableQueue FROM "
                                    + "Resource WHERE RID = " + copies[0][1])[0];

                            //If the tail is null then set the head and tail, otherwise make the tail
                            //point to the first copy.
                            if (tail == null) {
                                //set head and tail
                                dbManager.editTuple("Resource",
                                        new String[]{
                                            "HeadOfAvailableQueue", "HeadOfAvailableQueue"
                                        },
                                        new String[]{
                                            copies[0][0], copies[0][0]
                                        }, "RID", copies[0][1]);
                            } else {
                                //Make the tail point to the first copy.
                                dbManager.editTuple("Copy", new String[]{
                                    "NextCopyInQueue"
                                },
                                        new String[]{
                                            copies[0][0]
                                        }, "CPID", tail);
                            }

                            //for every reserved copy make it available
                            for (int iCount = 1; iCount < copies.length; iCount++) {
                                //set the previous copy to point to this copy.
                                dbManager.editTuple("Copy", new String[]{
                                    "NextCopyInQueue"
                                },
                                        new String[]{
                                            copies[iCount][0]
                                        }, "CPID", tail);

                                //if last copy to make available, then must be the new tail
                                if (iCount == copies.length - 1) {
                                    dbManager.editTuple("Resource", new String[]{
                                        "TailOfAvailableQueue"
                                    },
                                            new String[]{
                                                copies[iCount][0]
                                            }, "RID", copies[iCount][1]);
                                }

                            }

                        }

                        //delete fine history
                        dbManager.sqlQuery("DELETE FROM FineHistory WHERE TranID IN (SELECT TranID FROM "
                                + "TransactionHistory WHERE UID = " + userID + ")");

                        //delete transaction history
                        dbManager.deleteTuple("TransactionHistory",
                                new String[]{
                                    "UID"
                                }, new String[]{
                                    Integer.toString(userID)
                                });

                        //delete resource request history
                        dbManager.deleteTuple("ResourceRequestHistory",
                                new String[]{
                                    "UID"
                                }, new String[]{
                                    Integer.toString(userID)
                                });

                        //delete borrow history
                        dbManager.deleteTuple("BorrowHistory",
                                new String[]{
                                    "UID"
                                }, new String[]{
                                    Integer.toString(userID)
                                });

                        //check if staff
                        boolean isStaff = dbManager.checkIfExist("User", new String[]{
                            "UID"
                        },
                                new String[]{
                                    Integer.toString(userID)
                                });

                        //if staff, then delete staff details
                        if (isStaff) {
                            //delete staff details
                            dbManager.deleteTuple("Staff",
                                    new String[]{
                                        "UID"
                                    }, new String[]{
                                        Integer.toString(userID)
                                    });
                        }

                        //delete account
                        dbManager.deleteTuple("User",
                                new String[]{
                                    "UID"
                                }, new String[]{
                                    Integer.toString(userID)
                                });

                    }

                } else {
                    throw new IllegalStateException("User must pay fines to delete account.");
                }
            } else {
                throw new IllegalStateException("User must not have borrowed resources to delete account");
            }

        } else {
            throw new IllegalArgumentException("Specified user does not exist");
        }

    }

    /**
     * Determines if the user account exists.
     *
     * @param userID The user id of the user.
     *
     * @return True if exists. False if does not exists.
     */
    public boolean isExist(int userID) {

        try {
            //returns whether user exists or not.
            return dbManager.checkIfExist("User", new String[]{
                "UID"
            },
                    new String[]{
                        Integer.toString(userID)
                    });
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Changes the balance of an account and records the transaction.
     *
     * @param userID The user id of the account.
     * @param money The change in balance.
     *
     * @throws IllegalArgumentException Thrown when the specified user does not
     * exist.
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     */
    public void changeBalance(int userID, float money) throws IllegalArgumentException, SQLException {

        //get the account balance, IllegalArgumentException is thrown if user does not exist.
        float accountBalance = getAccountBalance(userID);

        //transaction
        accountBalance += money;

        //change balance in user table.
        dbManager.editTuple("User", new String[]{
            "Current_Balance"
        },
                new String[]{
                    Float.toString(accountBalance)
                }, "UID", Integer.toString(userID));

        //create transaction/
        Transaction transaction = new Transaction(userID, DateManager.returnCurrentDate(),
                DateManager.returnTime(), money, -1);

        //add the transaction to the history.
        addTransaction(transaction);

        //Sends notification email
    }

    /**
     * Changes the balance of an account and records the fine.
     *
     * @param userID The user id of the account.
     * @param money The change in balance.
     * @param copyID The copy id of the copy that caused the fine.
     * @param days The amount of days the copy has been overdue.
     *
     * @throws IllegalArgumentException Thrown when the specified user does not
     * exist.
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     */
    public void addFine(int userID, float money, int copyID, int days) throws IllegalArgumentException, SQLException {

        //get the account balance, IllegalArgumentException is thrown if user does not exist.
        float accountBalance = getAccountBalance(userID);

        //transaction
        accountBalance += money;

        //change balance in user table.
        dbManager.editTuple("User", new String[]{
            "Current_Balance"
        },
                new String[]{
                    Float.toString(accountBalance)
                }, "UID", Integer.toString(userID));

        //create transaction/
        FineTransaction transaction = new FineTransaction(userID, DateManager.returnCurrentDate(),
                DateManager.returnTime(), money, -1, copyID, days);

        //add the transaction to the history.
        addTransaction(transaction);

    }

    /**
     * Adds a transaction to history.
     *
     * @param transaction The transaction object.
     *
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     */
    private void addTransaction(Transaction transaction) throws SQLException {

        dbManager.addTuple("TransactionHistory", new String[]{
            "null",
            Integer.toString(transaction.getUserID()), encase(transaction.getDate()),
            encase(transaction.getTime()), Float.toString(transaction.getChange())
        });

    }

    /**
     * Adds a fine transaction to history.
     *
     * @param transaction The fine transaction object.
     *
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     */
    private void addTransaction(FineTransaction transaction) throws SQLException {

        //add to the transaction history.
        addTransaction((Transaction) transaction);

        //get the transaction id.
        String tranID = dbManager.getFirstTupleByQuery("SELECT max(TranID) FROM TransactionHistory")[0];

        //add to the fine history
        dbManager.addTuple("FineHistory", new String[]{
            tranID, Integer.toString(transaction.getCopyID()),
            Integer.toString(transaction.getDays())
        });

    }

    /**
     * Gets the account balance of an account.
     *
     * @param userID The user id of the account.
     *
     * @return The account balance.
     *
     * @throws IllegalArgumentException Thrown if the specified user does not
     * exist.
     * @throws SQLException Thrown if connection to the database failed or
     * tables do not exist.
     */
    public float getAccountBalance(int userID) throws IllegalArgumentException, SQLException {

        //check if user exists
        if (dbManager.checkIfExist("User", new String[]{
            "UID"
        }, new String[]{
            Integer.toString(userID)
        })) {

            //get the balance
            return Float.parseFloat(dbManager.getFirstTupleByQuery("SELECT Current_Balance FROM User WHERE UID = "
                    + userID)[0]);

        } else {
            throw new IllegalArgumentException("User specified does not exist.");
        }

    }

    /**
     * Get all transactions from a specific account.
     *
     * @param userID The user id of the account.
     *
     * @return An array of transactions.
     *
     * @throws IllegalArgumentException Thrown if the specified user does not
     * exist.
     */
    public Transaction[] getTransactionHistory(int userID) throws IllegalArgumentException {

        try {
            //if user exists then get transactions
            if (dbManager.checkIfExist("User", new String[]{
                "UID"
            }, new String[]{
                Integer.toString(userID)
            })) {

                //get amount of transactions
                int amountOfTransactions = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT count(*) FROM "
                        + "TransactionHistory WHERE UID = " + userID)[0]);

                //create array
                Transaction[] transactions = new Transaction[amountOfTransactions];

                //get transactions
                String[][] transRows = dbManager.getTupleListByQuery("SELECT * FROM TransactionHistory WHERE UID = "
                        + userID);

                //for every transaction construct a transaction
                for (int iCount = 0; iCount < amountOfTransactions; iCount++) {

                    if (isTransactionFine(Integer.parseInt(transRows[iCount][0]))) {

                        String[] fineRow = dbManager.getFirstTupleByQuery("SELECT * FROM FineHistory WHERE TranID = "
                                + transRows[iCount][0]);

                        //construct fine transaction
                        transactions[iCount] = new FineTransaction(userID, transRows[iCount][2], transRows[iCount][3],
                                Float.parseFloat(transRows[iCount][4]), Integer.parseInt(transRows[iCount][0]),
                                Integer.parseInt(fineRow[1]), Integer.parseInt(fineRow[2]));
                    } else {
                        //construct transaction
                        transactions[iCount] = new Transaction(userID, transRows[iCount][2], transRows[iCount][3],
                                Float.parseFloat(transRows[iCount][4]), Integer.parseInt(transRows[iCount][0]));
                    }

                }

                return transactions;

            } else {
                throw new IllegalArgumentException("User specified does not exist");
            }
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * Determines if a transaction is a fine transaction.
     *
     * @param tranID The transaction id.
     *
     * @return True if a fine. False if not a fine.
     *
     * @throws SQLException Thrown if connection to the database fails or tables
     * do not exist.
     */
    private boolean isTransactionFine(int tranID) throws SQLException {
        return dbManager.checkIfExist("FineHistory", new String[]{
            "TranID"
        },
                new String[]{
                    Integer.toString(tranID)
                });
    }

    /**
     * Edits/replaces an existing user account with the new data.
     *
     * @param user The new user data to add to the account.
     *
     * @throws SQLException Thrown if connection to the database fails or tables
     * do not exist.
     */
    public void editAccount(User user) throws SQLException {

        //Edit user details
        dbManager.editTuple("User", new String[]{
            "First_Name", "Last_Name", "Phone_Number",
            "Address1", "Address2", "County", "Postcode", "City", "ImageID", "Email"
        },
                new String[]{
                    encase(user.getFirstName()),
                    encase(user.getLastName()), encase(user.getTelNum()), encase(user.getStreetNum()),
                    encase(user.getStreetName()), encase(user.getCounty()), encase(user.getPostCode()),
                    encase(user.getCity()), Integer.toString(user.getAvatarID()), encase(user.getEmail()),}, "UID",
                Integer.toString(user.getUserID()));

    }

    /**
     * Edits/replaces an existing staff account with the new data.
     *
     * @param staff The new staff data to add to the account.
     *
     * @throws SQLException Thrown if connection to the database fails or tables
     * do not exist.
     */
    public void editAccount(Staff staff) throws SQLException {

        //Edit basic user details first
        editAccount((User) staff);

        //Edit staff details
        dbManager.editTuple("Staff", new String[]{
            "Employment_Date"
        },
                new String[]{
                    encase(staff.getEmploymentDate())
                }, "SID", Integer.toString(staff.getStaffNum()));

    }

    /**
     * Adds a user account to the database and return its user id.
     *
     * @param newAccount The account to add.
     *
     * @return The user id of the newly added account.
     *
     * @throws SQLException Thrown if connection to the database failed or the
     * arguments were invalid.
     */
    public int addAccount(User newAccount) throws SQLException {

        //2018-01-01 00:00:00 -> default date for new users.
        // New users will have all resources as new additions 
        //add account to the database.
        dbManager.addTuple("User", new String[]{
            "null", encase(newAccount.getFirstName()),
            encase(newAccount.getLastName()), encase(newAccount.getTelNum()),
            encase(newAccount.getStreetNum()), encase(newAccount.getStreetName()),
            encase(newAccount.getCounty()), encase(newAccount.getPostCode()), encase(newAccount.getCity()),
            Integer.toString(newAccount.getAvatarID()), encase("0"), encase("2018-01-01 00:00:00"), encase("0"),
            encase("key"), encase("salt"), encase("Q"), encase("A"), encase(newAccount.getEmail())});

        //new user id
        int uid = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT max(UID) FROM User")[0]);
        //Sends Welcoming email.
        NotificationCenter nc = new NotificationCenter();
        nc.sendNewAccountEmail(uid, newAccount.getFirstName(), newAccount.getEmail());
        //get the user id, as the user id is an auto incrementing primary key. userID will be the max value.
        return uid;

    }

    /**
     * Adds a staff account to the database and returns its user id and staff
     * id.
     *
     * @param newAccount The staff account to add.
     *
     * @return An array containing the user and staff id.
     *
     * @throws SQLException Thrown if connection to the database failed or the
     * arguments were invalid.
     */
    public int[] addAccount(Staff newAccount) throws SQLException {

        //get the user id, and add basic user details
        int userID = addAccount((User) newAccount);

        //add staff details
        dbManager.addTuple("Staff", new String[]{
            "null", Integer.toString(userID),
            encase(newAccount.getEmploymentDate())
        });

        //int get staff id;
        int staffID = Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT max(SID) FROM Staff")[0]);

        return new int[]{
            userID, staffID
        };

    }

    /**
     * Constructs and returns a user from the database specified by the user id.
     *
     * @param userID The user id of the user to return.
     *
     * @return The constructed user.
     *
     * @throws IllegalArgumentException Thrown if the specified user does not
     * exist.
     */
    public User getAccount(int userID) throws IllegalArgumentException {

        try {
            //check if user exists
            if (dbManager.checkIfExist("User", new String[]{
                "UID"
            },
                    new String[]{
                        Integer.toString(userID)
                    })) {

                //get user details
                String[] userRow = dbManager.getFirstTupleByQuery("SELECT * FROM User WHERE UID = "
                        + userID);

                //check if user is a staff member
                boolean isStaff = dbManager.checkIfExist("Staff", new String[]{
                    "UID"
                },
                        new String[]{
                            Integer.toString(userID)
                        });

                User user;

                //get staff details and construct staff object if staff member, otherwise construct user object
                if (isStaff) {

                    String[] staffRow = dbManager.getFirstTupleByQuery("SELECT * FROM Staff WHERE UID = "
                            + userID);

                    /*
                     * userRow[12] may not be correct
                     */
                    //constructs a staff object from database table row.
                    user = new Staff(Integer.parseInt(userRow[0]), userRow[1], userRow[2], userRow[3], userRow[4],
                            userRow[5], userRow[6], userRow[8], userRow[7], staffRow[2],
                            Integer.parseInt(staffRow[0]), userRow[11], Integer.parseInt(userRow[9]), Integer.parseInt(userRow[12]), userRow[17]);

                } else {

                    //constructs a user object from database table row.
                    user = new User(Integer.parseInt(userRow[0]), userRow[1], userRow[2], userRow[3], userRow[4],
                            userRow[5], userRow[6], userRow[8], userRow[7], userRow[11], Integer.parseInt(userRow[9]), Integer.parseInt(userRow[12]), userRow[17]);
                }
                return user;

            } else {
                throw new IllegalArgumentException("Specified user does not exist");
            }
        } catch (SQLException e) {
            return null;
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
     * Gets the last login time for a user
     *
     * @param userID The users userID
     *
     * @return the date and time the user last logged in
     */
    public String getLastLogin(int userID) {
        User user = getAccount(userID);
        String LastLogin = user.getLastLogin();
        return LastLogin;
    }

    /**
     * Sets the last login time for a user.
     *
     * @param userID The users userID.
     * @param date The date to add as last login.
     */
    public void setLastLogin(int userID, String date) {
        //change lastlogin in user table.
        try {
            dbManager.editTuple("User", new String[]{"LastLogin"},
                    new String[]{encase(date)}, "UID", Integer.toString(userID));

        } catch (SQLException e) {
            //LastLogin couldnt be updated
        }
    }

    /**
     * Gets the resource cap of an account.
     *
     * @param userID The user id of the account.
     *
     * @return The account resource cap.
     *
     * @throws IllegalArgumentException Thrown if the specified user does not
     * exist.
     * @throws SQLException Thrown if connection to the database failed or
     * tables do not exist.
     */
    public int getUserResourceCap(int userID) throws IllegalArgumentException, SQLException {

        //check if user exists
        if (dbManager.checkIfExist("User", new String[]{"UID"},
                new String[]{Integer.toString(userID)})) {

            //get the Resource cap
            return Integer.parseInt(dbManager.getFirstTupleByQuery("SELECT Resource_Cap FROM User WHERE UID = "
                    + userID)[0]);

        } else {
            throw new IllegalArgumentException("User specified does not exist.");
        }

    }

    /**
     * Changes the resourceCap value of an account.
     *
     * @param userID The user id of the account.
     * @param newResourceValue The change in resource value.
     *
     * @throws IllegalArgumentException Thrown when the specified user does not
     * exist.
     * @throws SQLException Thrown if connection to database fails or table does
     * not exist.
     */
    public void changeUserResourceCap(int userID, int newResourceValue) throws IllegalArgumentException, SQLException {

        //get the account resourceCap value, IllegalArgumentException is thrown if user does not exist.
        int resourceCap = getUserResourceCap(userID);

        resourceCap += newResourceValue;

        //change Resource_Cap in user table.
        dbManager.editTuple("User", new String[]{
            "Resource_Cap"
        },
                new String[]{
                    Integer.toString(resourceCap)
                }, "UID", Integer.toString(userID));

    }

    /**
     * Converts an byte array to String for comparison.
     *
     * @param array array to be converted.
     * @returns String
     */
    private static String convertHash(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int padding = (array.length * 2) - hex.length();
        if (padding > 0) {
            return String.format("%0" + padding + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Converts the key to a hash.
     *
     * @param key key to be converted.
     * @return String, hashed.
     */
    private String encrypt(String key) throws Exception {
        try {
            int iterations = 1000;
            char[] chars = key.toCharArray();

            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + convertHash(salt) + ":" + convertHash(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new Exception("Encrypting failed");
    }

    /**
     * Saves the encrypted user input in the database.
     *
     * @param key user input.
     * @param userID user id.
     */
    public void savePassword(String key, int userID) {
        try {
            dbManager.editTuple("User", new String[]{"Key"},
                    new String[]{encase(encrypt(key))},
                    "UID", Integer.toString(userID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the secret question.
     *
     * @param question new question.
     * @param answer new answer.
     * @param userID user id.
     * @throws Exception saving failed.
     */
    public void updateSecret(String question, String answer, int userID) throws Exception {
        try {
            dbManager.editTuple("User", new String[]{"SecretA", "SecretQ"},
                    new String[]{encase(encrypt(answer)), encase(question)},
                    "UID", Integer.toString(userID));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Secret save failed");
        }
    }

    /**
     * Fetches user's secret question.
     *
     * @param userID user id.
     * @return String containing the question.
     */
    public String getSecretQuestion(int userID) {
        try {
            return dbManager.getTupleListByQuery("Select SecretQ From User Where UID=" + userID)[0][0];
        } catch (SQLException ex) {
            return "Failed to Load";
        }
    }

    /**
     * Checks if the secret answer is correct.
     *
     * @param answer inputed answer
     * @param userID user id.
     * @return boolean.
     */
    public boolean checkSecretAnswer(String answer, int userID) {
        String databaseAnswer = "";
        try {
            databaseAnswer = dbManager.getTupleListByQuery("Select SecretA From User Where UID=" + userID)[0][0];
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return AuthenticationManager.checkKey(answer, databaseAnswer);
    }
}
