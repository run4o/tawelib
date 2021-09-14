package Core;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Performs date operations.
 *
 * @author Alberto Martin
 * @author Martin Trifonov(v.2.0)
 * @version 2.0
 */
public class DateManager {

    /**
     * Constructor of DateManager that creates a file if it doesn't exist
     *
     * @throws IOException Thrown if file write fails.
     */
    public DateManager() throws IOException {
        //Get the current date
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        String filePathString = null;
        File dateFile = new File(filePathString);
        //if there exists no date file, then write a file which stores the date to able to forward the date.
        if (!dateFile.exists() && !dateFile.isDirectory()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("dateFile.txt"));
            writer.write(df.format(date));
            writer.close();
        }
    }

    /**
     * Method that returns the current date
     *
     * @return The current date. As yyyy/MM/dd
     */
    public static String returnCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return (df.format(date));
    }

    /**
     * Method that returns the time
     *
     * @return The current time as HH:mm:ss
     */
    public static String returnTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        return (df.format(date));
    }

    /**
     * Method to update the date by one and write it into a file
     *
     * @throws IOException Thrown if file writing fails.
     */
    public static void updateForward() throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, 1);

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter("dateFile.text"));
            writer.write(df.format(cal.getTime()));
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to calculate the due date.
     *
     * @param days The number days forward from the current date.
     *
     * @return The new date. As yyyy-MM-dd
     */
    public static String returnDueDate(int days) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, days);
        return (df.format(cal.getTime()));
    }

    /**
     * Method to calculate the due date from a specified date.
     *
     * @param currentDate The current date to calculate the dueDate from.
     * @param days The number days forward from the current date.
     *
     * @return The new date. As yyyy-MM-dd
     */
    public static String returnDueDate(String currentDate, int days) {

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(df.parse(currentDate));

            cal.add(Calendar.DAY_OF_MONTH, days);
            return (df.format(cal.getTime()));

        } catch (ParseException p) {
            //If returning due date failed. We return an empty string.
            return "";
        }

    }

    /**
     * Return true if the date has past.
     *
     * @param date Date to be checked.
     * @return True if date has passed, false if not.
     */
    public static boolean hasDatePassed(Date date) {
        Date curDate = new Date();
        return curDate.after(date);
    }
}
