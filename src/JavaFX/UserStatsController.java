package JavaFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ListView;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Displays resource statistics about what the user has borrowed.
 *
 * @author Fraser Barrass
 * @version 1.0
 */
public class UserStatsController extends SceneController implements Initializable {

    final int MAX_LISTED = 15;
    @FXML
    BarChart<String, Number> BC;
    @FXML
    ListView<String> borrowList;

    /**
     * retrieves the resources specified as popular.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BC.setVisible(false);
        BC.setAnimated(false);
        BC.getData().clear();
    }

    /**
     * displays how many items borrowed per day, for the currently logged in
     * user.
     *
     * @param event The user button being pressed
     * @throws ParseException Thrown when a parsing error occurs
     * @throws SQLException
     */
    public void perDayButton(ActionEvent event) throws ParseException, SQLException {
        BC.setTitle("Statistics per Day");
        BC.setVisible(true);
        borrowList.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);

        String[][] in = getResourceManager().getPopularityTimeData(SceneController.USER_ID);

        int maxListing = MAX_LISTED;

        if (in.length < maxListing) {
            maxListing = in.length;
        }

        Data<String, Number>[] data = new Data[maxListing];
        Series<String, Number> series = new XYChart.Series<>();
        ObservableList<String> dataText = FXCollections.observableArrayList();
        for (int iCount = 0; iCount < maxListing; iCount++) {

            String inputDate = in[iCount][0];
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(inputDate);

            int borrowCount = Integer.parseInt(in[iCount][1]);

            data[iCount] = new Data<String, Number>(inputDate, borrowCount);
            series.getData().add(data[iCount]);

            String out = inputDate + "			" + borrowCount + " Items Borrowed.";
            dataText.add(out);

        }
        BC.getData().add(series);
        borrowList.setItems(dataText);
    }

    /**
     * displays how many items borrowed per week, for the currently logged in
     * user.
     *
     * @param event The user button being pressed
     * @throws ParseException Thrown when a parsing error occurs
     * @throws SQLException
     */
    public void perWeekButton(ActionEvent event) throws ParseException, SQLException {
        BC.setTitle("Statistics per Week");
        BC.setVisible(true);
        borrowList.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);
        Calendar cal = new GregorianCalendar();

        int pastBorrowCount = 0;
        String pastWeek = "";

        ArrayList<Date> dateList = new ArrayList<Date>();
        for (int i = 0; i < 52; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
            dateList.add(cal.getTime());
        }

        String weekNum = "";

        String[][] in = getResourceManager().getPopularityTimeData(SceneController.USER_ID);

        int maxListing = MAX_LISTED;

        if (in.length < maxListing) {
            maxListing = in.length;
        }

        Data<String, Number>[] data = new Data[maxListing];
        Series<String, Number> series = new XYChart.Series<>();
        ObservableList<String> dataText = FXCollections.observableArrayList();
        for (int iCount = 0; iCount < maxListing; iCount++) {

            String inputDate = in[iCount][0];
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(inputDate);

            for (int i = 0; i < dateList.size(); i++) {
                if (date.after(dateList.get(0))) {
                    weekNum = "Last Week";
                } else if (date.after(dateList.get(1)) && date.before(dateList.get(0))) {
                    weekNum = "1 Week Ago";
                } else if (date.after(dateList.get(i)) && date.before(dateList.get(i - 1))) {
                    weekNum = i + " Weeks Ago";
                }
            }
            int borrowCount = Integer.parseInt(in[iCount][1]);

            // if the week before is the same week, then add them both together
            if (weekNum.equalsIgnoreCase(pastWeek)) {
                borrowCount += pastBorrowCount;
                dataText.set(iCount - 1, weekNum + "			" + borrowCount + " Items Borrowed.");
            }

            data[iCount] = new Data<String, Number>(weekNum, borrowCount);
            series.getData().add(data[iCount]);

            String out = weekNum + "			" + borrowCount + " Items Borrowed.";
            dataText.add(out);

            pastWeek = weekNum;
            pastBorrowCount = Integer.parseInt(in[iCount][1]);
        }
        removeDuplicates(dataText);
        BC.getData().add(series);
        borrowList.setItems(dataText);
    }

    /**
     * displays how many items borrowed per month, for the currently logged in
     * user.
     *
     * @param event The user button being pressed
     * @throws ParseException Thrown when a parsing error occurs
     * @throws SQLException
     */
    public void perMonthButton(ActionEvent event) throws ParseException, SQLException {
        BC.setTitle("Statistics per Month");
        BC.setVisible(true);
        borrowList.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);
        int pastBorrowCount = 0;
        String pastMonth = "";
        String[][] in = getResourceManager().getPopularityTimeData(SceneController.USER_ID);

        int maxListing = MAX_LISTED;

        if (in.length < maxListing) {
            maxListing = in.length;
        }

        Data<String, Number>[] data = new Data[maxListing];
        Series<String, Number> series = new XYChart.Series<>();
        ObservableList<String> dataText = FXCollections.observableArrayList();
        for (int iCount = 0; iCount < maxListing; iCount++) {

            String inputDate = in[iCount][0];
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(inputDate);
            int year = (date.getYear() + 1900);
            String month = month(date.getMonth());

            int borrowCount = Integer.parseInt(in[iCount][1]);

            // if the month before is the same month, then add them both together
            if (month.equalsIgnoreCase(pastMonth)) {
                borrowCount += pastBorrowCount;
                dataText.set(iCount - 1, month + " " + year + "			" + borrowCount + " Items Borrowed.");
            }

            data[iCount] = new Data<String, Number>(month + " " + year, borrowCount);
            series.getData().add(data[iCount]);

            pastMonth = month(date.getMonth());
            pastBorrowCount = Integer.parseInt(in[iCount][1]);

            String out = month + " " + year + "			" + borrowCount + " Items Borrowed.";
            dataText.add(out);
        }
        removeDuplicates(dataText);
        BC.getData().add(series);
        borrowList.setItems(dataText);
    }

    /**
     * removes duplicates from a list.
     *
     * @param dataText The list to have duplicates removed from
     * @return ObservableList The new list with no duplicates
     */
    public static ObservableList<String> removeDuplicates(ObservableList<String> dataText) {

        Set<String> set = new LinkedHashSet<>();
        for (int i = 0; i < dataText.size(); i++) {
            set.add(dataText.get(i));
        }

        dataText.clear();

        dataText.addAll(set);
        return dataText;
    }

    /**
     * method to turn the month number into the String name of month.
     *
     * @param monthNum The month number
     * @return String The month name
     */
    public String month(int monthNum) {
        if (monthNum == 0) {
            return "January";
        } else if (monthNum == 1) {
            return "February";
        } else if (monthNum == 2) {
            return "March";
        } else if (monthNum == 3) {
            return "April";
        } else if (monthNum == 4) {
            return "May";
        } else if (monthNum == 5) {
            return "June";
        } else if (monthNum == 6) {
            return "July";
        } else if (monthNum == 7) {
            return "August";
        } else if (monthNum == 8) {
            return "September";
        } else if (monthNum == 9) {
            return "October";
        } else if (monthNum == 10) {
            return "November";
        } else if (monthNum == 11) {
            return "December";
        }
        return null;
    }
}
