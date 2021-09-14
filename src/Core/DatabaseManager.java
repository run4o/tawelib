package Core;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class allows the fetching and writing of data to the database.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class DatabaseManager {

    /**
     * The directory to the database file.
     */
    private String dbLocation;

    /**
     * Creates a DatabaseManager.
     *
     * @param dbLocation The directory to the database file.
     */
    public DatabaseManager(String dbLocation) {
        this.dbLocation = dbLocation;
    }

    /**
     * Creates a connection (session) with the database.
     *
     * @return The connection to the database.
     */
    private Connection createConnection() {

        try {
            Class.forName("org.sqlite.JDBC");

            //attempt to establish connection with database.
            try {

                //create connection
                return DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            } catch (SQLException e) {
                return null;
            }
        } catch (ClassNotFoundException e) {
            return null;
        }

    }

    /**
     * Checks if a row exists in the table where the values satisfies the keys.
     *
     * @param tableName The name of the table.
     * @param keys The keys to filter the row by.
     * @param values The values to check in the keys.
     *
     * @return If at least one row is found, true is returned. Otherwise, false
     * is returned.
     *
     * @throws SQLException Thrown if parameters are incorrect or connection
     * could not be established.
     */
    public boolean checkIfExist(String tableName, String[] keys, String[] values) throws SQLException {

        //Create a query to count the amount of rows that satisfies the search.
        String query = "SELECT count(*) FROM " + tableName + " WHERE ";

        //For every key, add the comparison between the key and value to the where condition.
        for (int iCount = 0; iCount < keys.length - 1; iCount++) {
            query += keys[iCount] + " = " + values[iCount] + " AND ";
        }
        query += keys[keys.length - 1] + " = " + values[keys.length - 1] + ";";

        //return true if count is not equal to 0. Otherwise false.
        return !getTupleListByQuery(query)[0][0].equals("0");
    }

    /**
     * Deletes a tuple/row from a table.
     *
     * @param tableName The name of the table.
     * @param keys The columns to search by.
     * @param values The values to filter the columns by.
     *
     * @throws SQLException Thrown if parameters are incorrect or connection
     * could not be established.
     */
    public void deleteTuple(String tableName, String[] keys, String[] values) throws SQLException {

        //Create a query delete row.
        String query = "DELETE FROM " + tableName + " WHERE ";

        //For every key, add comparisons between keys and values.
        for (int iCount = 0; iCount < keys.length - 1; iCount++) {
            query += keys[iCount] + " = " + values[iCount] + " AND ";
        }

        query += keys[keys.length - 1] + " = " + values[keys.length - 1] + ";";

        //execute query
        sqlQuery(query);

    }

    /**
     * Queries the database with a specified query
     *
     * @param query The query to ask the database.
     *
     * @throws SQLException Thrown if query is incorrect or connection could not
     * be established.
     */
    public void sqlQuery(String query) throws SQLException {
        // create connection
        Connection conn = createConnection();

        Statement statement = conn.createStatement();

        //query the database
        statement.execute(query);

        statement.close();
        conn.close();
    }

    /**
     * Adds a tuple/row to a database table.
     *
     * @param tableName The name of the database table.
     * @param columnData The tuple/row data to add. Each item in the array
     * represents a column. Text values must be encased in ''.
     *
     * @throws SQLException Thrown if passed data is incorrect or connection
     * could not be established.
     */
    public void addTuple(String tableName, String[] columnData) throws SQLException {

        String sqlQuery = "INSERT INTO " + tableName + " VALUES (";

        // for every column value in data except the last value, add to the sqlQuery
        for (int iColumn = 0; iColumn < columnData.length - 1; iColumn++) {
            sqlQuery += columnData[iColumn] + ", ";
        }
        // add the last column value to the sql query without a comma.
        sqlQuery += columnData[columnData.length - 1] + ")";

        sqlQuery(sqlQuery);

    }

    /**
     * Edits specified tuples/rows in a database table where the key column
     * satisfies the identifier value.
     *
     * @param tableName The name of the table.
     * @param columnNames The names of the columns.
     * @param columnData The data to be updated in the columns.
     * @param key The column name you want to filter by.
     * @param identifier The value you want the tuples/row to satisfy.
     *
     * @throws SQLException Thrown if passed data is incorrect or connection
     * could not be established.
     */
    public void editTuple(String tableName, String[] columnNames, String[] columnData, String key, String identifier)
            throws SQLException {

        //Create the query to execute on the database
        String query = "UPDATE " + tableName + " SET ";

        /*
		For every column specified, append the query so the corresponding column in the row is now equal to
		the new data.
         */
        for (int iColumn = 0; iColumn < columnNames.length - 1; iColumn++) {
            query += columnNames[iColumn] + " = " + columnData[iColumn] + ", ";
        }

        //Add the where condition to filter the rows/tuples
        query += columnNames[columnNames.length - 1] + " = " + columnData[columnNames.length - 1] + " WHERE " + key
                + " = " + identifier;

        //Execute the query
        sqlQuery(query);

    }

    /**
     * Gets all tuples/rows of a selected table.
     *
     * @param tableName The name of the table.
     *
     * @return The array of tuple/rows.
     *
     * @throws SQLException Thrown if tableName does not exist or connection
     * could not be established.
     */
    public String[][] getTupleList(String tableName) throws SQLException {
        // selects all rows from table
        return getTupleListByQuery("SELECT * FROM " + tableName + ";");
    }

    /**
     * Gets all tuples/rows of a select table that satisfies a search query on a
     * column.
     *
     * @param tableName The name of the table.
     * @param selectColumn The name of the column to query.
     * @param searchQuery The data to search in the table.
     *
     * @return The array of tuple/rows.
     *
     * @throws SQLException Thrown if passed data is incorrect or connection
     * could not be established.
     */
    public String[][] searchTuples(String tableName, String selectColumn, String searchQuery) throws SQLException {
        // selects all rows from table tableName where the selectColumn contains
        // searchQuery inside it
        return getTupleListByQuery(
                "SELECT * FROM " + tableName + " WHERE " + selectColumn + " LIKE " + "'%" + searchQuery + "%'");
    }

    /**
     * Gets all tuples/rows of a select table that satisfies the search queries
     * on the column(s).
     *
     * @param tableName The name of the table.
     * @param columns The name of the columns to query.
     * @param searchQueries The data to search on the columns.
     *
     * @return The array of tuple/rows.
     *
     * @throws SQLException Thrown if passed data is incorrect or connection
     * could not be established.
     */
    public String[][] searchTuples(String tableName, String[] columns, String[] searchQueries) throws SQLException {
        // selects all rows from table tableName where the columns is satisfied by the search queries
        String query = "SELECT * FROM " + tableName + " WHERE ";

        //for every column to search, add to the sql query.
        for (int iCount = 0; iCount < columns.length - 1; iCount++) {
            query += columns[iCount] + " LIKE " + "'%" + searchQueries[iCount] + "%' AND ";
        }
        query += columns[columns.length - 1] + " LIKE " + "'%" + searchQueries[columns.length - 1] + "%'";

        return getTupleListByQuery(query);
    }

    /**
     * Gets the tuples/rows from a specified query.
     *
     * @param query The query to execute on the database.
     *
     * @return The array of tuples/rows.
     *
     * @throws SQLException Thrown if query is incorrect or connection could not
     * be established.
     */
    public String[][] getTupleListByQuery(String query) throws SQLException {
        // create connection
        Connection conn = createConnection();

        Statement statement = conn.createStatement();

        // execute query which will list all rows
        ResultSet resultSet = statement.executeQuery(query);

        // an array list is initially used as the amount of rows are unknown
        ArrayList<String[]> tupleList = new ArrayList<>();

        // the amount of columns in a row
        int rowLength = resultSet.getMetaData().getColumnCount();

        // for every row, add the columns to an array of strings and then add to the
        // arraylist
        while (resultSet.next()) {

            // add an empty string array to the array list
            tupleList.add(new String[rowLength]);

            // for every column add the column data to the string array
            for (int iColumn = 1; iColumn <= rowLength; iColumn++) {
                tupleList.get(tupleList.size() - 1)[iColumn - 1] = resultSet.getString(iColumn);
            }

        }

        resultSet.close();
        statement.close();
        conn.close();

        // convert array list to an array and return
        return tupleList.toArray(new String[tupleList.size()][]);
    }

    /**
     * Gets the first tuple/row from a specified query.
     *
     * @param query The query to execute on the database.
     *
     * @return The first tuple found from a specified query.
     *
     * @throws SQLException Thrown if query is incorrect or connection could not
     * be established.
     */
    public String[] getFirstTupleByQuery(String query) throws SQLException {

        //Get the list of tuples/rows satisfied by the query.
        String[][] tupleList = getTupleListByQuery(query);
        //If the tupleList has atleast one row, then return the first row. Otherwise return null.
        if (tupleList.length > 0) {
            return tupleList[0];
        } else {
            return new String[0];
        }

    }

    /**
     * Gets the database directory.
     *
     * @return The database directory.
     */
    public String getDBLocation() {
        return dbLocation;
    }

    /**
     * Sets the database directory.
     *
     * @param dbLocation The database directory.
     */
    public void setDBlocation(String dbLocation) {
        this.dbLocation = dbLocation;
    }
}
