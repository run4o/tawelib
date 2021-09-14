package JavaFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Displays statistics about resources and fines for staff members.
 *
 * @author Fraser Barrass
 * @version 1.0
 */
public class StaffStatsController extends SceneController implements Initializable {

    private String resource = "";
    private String time = "All Time";
    final int MAX_LISTED = 5;
    @FXML
    BarChart<String, Number> BC;

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
        BC.setTitle("Statistics");
    }

    /**
     * displays the most popular book statistics.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void bookButton(ActionEvent event) throws SQLException {
        resource = "Book";
        BC.setTitle(resource + " Statistics - " + time);

        BC.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);

        String[][] bookData = getResourceManager().getResourcePopularityData(resource, time);

        int maxBookListing = MAX_LISTED;

        if (bookData.length < maxBookListing) {
            maxBookListing = bookData.length;
        }

        Data<String, Number>[] books = new Data[maxBookListing];
        Series<String, Number> bookSeries = new XYChart.Series<>();
        for (int iCount = 0; iCount < maxBookListing; iCount++) {

            String title = bookData[iCount][0];
            int borrowCount = Integer.parseInt(bookData[iCount][1]);

            final int MAX_TITLE_LENGTH = 25;
            // If the title is longer than max length, the cut end and add "..."
            if (title.length() > MAX_TITLE_LENGTH) {
                title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
            }
            books[iCount] = new Data<String, Number>(title, borrowCount);
            bookSeries.getData().add(books[iCount]);
        }
        BC.getData().add(bookSeries);
    }

    /**
     * displays the most popular computer statistics.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void computerButton(ActionEvent event) throws SQLException {
        resource = "Computer";
        BC.setTitle(resource + " Statistics - " + time);

        BC.setVisible(true);
        BC.setLegendVisible(false);
        BC.getData().clear();

        String[][] computerData = getResourceManager().getResourcePopularityData(resource, time);

        int maxComputerListing = MAX_LISTED;

        if (computerData.length < maxComputerListing) {
            maxComputerListing = computerData.length;
        }

        Data<String, Number>[] computers = new Data[maxComputerListing];
        Series<String, Number> computerSeries = new XYChart.Series<>();
        for (int iCount = 0; iCount < maxComputerListing; iCount++) {

            String title = computerData[iCount][0];
            int borrowCount = Integer.parseInt(computerData[iCount][1]);

            final int MAX_TITLE_LENGTH = 25;
            // If the title is longer than max length, the cut end and add "..."
            if (title.length() > MAX_TITLE_LENGTH) {
                title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
            }
            computers[iCount] = new Data<String, Number>(title, borrowCount);
            computerSeries.getData().add(computers[iCount]);
        }
        BC.getData().add(computerSeries);
    }

    /**
     * displays the most popular dvd statistics.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void dvdButton(ActionEvent event) throws SQLException {
        resource = "DVD";
        BC.setTitle(resource + " Statistics - " + time);

        BC.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);

        String[][] dvdData = getResourceManager().getResourcePopularityData(resource, time);

        int maxDVDListing = MAX_LISTED;
        if (dvdData.length < maxDVDListing) {
            maxDVDListing = dvdData.length;
        }
        Data<String, Number>[] dvds = new Data[maxDVDListing];

        Series<String, Number> dvdSeries = new XYChart.Series<>();

        for (int iCount = 0; iCount < maxDVDListing; iCount++) {

            String title = dvdData[iCount][0];
            int borrowCount = Integer.parseInt(dvdData[iCount][1]);

            final int MAX_TITLE_LENGTH = 25;
            // If the title is longer than max length, the cut end and add "..."
            if (title.length() > MAX_TITLE_LENGTH) {
                title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
            }
            dvds[iCount] = new Data<String, Number>(title, borrowCount);
            dvdSeries.getData().add(dvds[iCount]);
        }

        BC.getData().add(dvdSeries);
    }

    /**
     * displays the most popular game statistics.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void gameButton(ActionEvent event) throws SQLException {
        resource = "VideoGame";
        BC.setTitle(resource + " Statistics - " + time);

        BC.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);

        String[][] gameData = getResourceManager().getResourcePopularityData(resource, time);

        int maxGameListing = MAX_LISTED;

        if (gameData.length < maxGameListing) {
            maxGameListing = gameData.length;
        }
        Data<String, Number>[] games = new Data[maxGameListing];

        Series<String, Number> gameSeries = new XYChart.Series<>();

        for (int iCount = 0; iCount < maxGameListing; iCount++) {

            String title = gameData[iCount][0];
            int borrowCount = Integer.parseInt(gameData[iCount][1]);

            final int MAX_TITLE_LENGTH = 25;
            // If the title is longer than max length, the cut end and add "..."
            if (title.length() > MAX_TITLE_LENGTH) {
                title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
            }
            games[iCount] = new Data<String, Number>(title, borrowCount);
            gameSeries.getData().add(games[iCount]);
        }

        BC.getData().add(gameSeries);
    }

    /**
     * displays the data for the last day, for the currently selected resource.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void dayButton(ActionEvent event) throws SQLException {
        time = "Day";
        BC.setTitle(resource + " Statistics - " + time);

        BC.getData().clear();
        BC.setVisible(true);
        BC.setLegendVisible(false);

        if (resource.equalsIgnoreCase("Fine")) {
            String[][] fineData = getResourceManager().getFineData(time);

            int maxBookListing = MAX_LISTED;

            if (fineData.length < maxBookListing) {
                maxBookListing = fineData.length;
            }

            Data<String, Number>[] fines = new Data[maxBookListing];
            Series<String, Number> fineSeries = new XYChart.Series<>();
            for (int iCount = 0; iCount < maxBookListing; iCount++) {

                String inputDate = fineData[iCount][0];
                double fineAmount = Double.parseDouble(fineData[iCount][1]);

                fines[iCount] = new Data<String, Number>(inputDate, fineAmount);
                fineSeries.getData().add(fines[iCount]);
            }
            BC.getData().add(fineSeries);

        } else {

            String[][] in = getResourceManager().getResourcePopularityData(resource, time);

            int maxListing = MAX_LISTED;

            if (in.length < maxListing) {
                maxListing = in.length;
            }

            Data<String, Number>[] data = new Data[maxListing];
            Series<String, Number> series = new XYChart.Series<>();
            for (int iCount = 0; iCount < maxListing; iCount++) {

                String title = in[iCount][0];
                int borrowCount = Integer.parseInt(in[iCount][1]);

                final int MAX_TITLE_LENGTH = 25;
                // If the title is longer than max length, the cut end and add "..."
                if (title.length() > MAX_TITLE_LENGTH) {
                    title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
                }
                data[iCount] = new Data<String, Number>(title, borrowCount);
                series.getData().add(data[iCount]);
            }
            BC.getData().add(series);
        }
    }

    /**
     * displays the data for the last week, for the currently selected resource.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void weekButton(ActionEvent event) throws SQLException {
        time = "Week";
        BC.setTitle(resource + " Statistics - " + time);

        BC.getData().clear();
        BC.setVisible(true);
        BC.setLegendVisible(false);

        if (resource.equalsIgnoreCase("Fine")) {
            String[][] fineData = getResourceManager().getFineData(time);

            int maxBookListing = MAX_LISTED;

            if (fineData.length < maxBookListing) {
                maxBookListing = fineData.length;
            }

            Data<String, Number>[] fines = new Data[maxBookListing];
            Series<String, Number> fineSeries = new XYChart.Series<>();
            for (int iCount = 0; iCount < maxBookListing; iCount++) {

                String inputDate = fineData[iCount][0];
                double fineAmount = Double.parseDouble(fineData[iCount][1]);

                fines[iCount] = new Data<String, Number>(inputDate, fineAmount);
                fineSeries.getData().add(fines[iCount]);
            }
            BC.getData().add(fineSeries);
        } else {

            String[][] in = getResourceManager().getResourcePopularityData(resource, time);

            int maxListing = MAX_LISTED;

            if (in.length < maxListing) {
                maxListing = in.length;
            }

            Data<String, Number>[] data = new Data[maxListing];
            Series<String, Number> series = new XYChart.Series<>();
            for (int iCount = 0; iCount < maxListing; iCount++) {

                String title = in[iCount][0];
                int borrowCount = Integer.parseInt(in[iCount][1]);

                final int MAX_TITLE_LENGTH = 25;
                // If the title is longer than max length, the cut end and add "..."
                if (title.length() > MAX_TITLE_LENGTH) {
                    title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
                }
                data[iCount] = new Data<String, Number>(title, borrowCount);
                series.getData().add(data[iCount]);
            }
            BC.getData().add(series);
        }
    }

    /**
     * displays the data for all time, for the currently selected resource.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void allTimeButton(ActionEvent event) throws SQLException {
        time = "All Time";
        BC.setTitle(resource + " Statistics - " + time);

        BC.getData().clear();
        if (resource.equalsIgnoreCase("Fine")) {
            String[][] fineData = getResourceManager().getFineData(time);

            int maxBookListing = MAX_LISTED;

            if (fineData.length < maxBookListing) {
                maxBookListing = fineData.length;
            }

            Data<String, Number>[] fines = new Data[maxBookListing];
            Series<String, Number> fineSeries = new XYChart.Series<>();
            for (int iCount = 0; iCount < maxBookListing; iCount++) {

                String inputDate = fineData[iCount][0];
                double fineAmount = Double.parseDouble(fineData[iCount][1]);

                fines[iCount] = new Data<String, Number>(inputDate, fineAmount);
                fineSeries.getData().add(fines[iCount]);
            }
            BC.getData().add(fineSeries);
        } else {
            String[][] in = getResourceManager().getResourcePopularityData(resource, time);

            int maxListing = MAX_LISTED;

            if (in.length < maxListing) {
                maxListing = in.length;
            }

            Data<String, Number>[] data = new Data[maxListing];
            Series<String, Number> series = new XYChart.Series<>();
            for (int iCount = 0; iCount < maxListing; iCount++) {

                String title = in[iCount][0];
                int borrowCount = Integer.parseInt(in[iCount][1]);

                final int MAX_TITLE_LENGTH = 25;
                // If the title is longer than max length, the cut end and add "..."
                if (title.length() > MAX_TITLE_LENGTH) {
                    title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
                }
                data[iCount] = new Data<String, Number>(title, borrowCount);
                series.getData().add(data[iCount]);
            }
            BC.getData().add(series);
        }
    }

    /**
     * displays Fine statistics to the user.
     *
     * @param event The user button being pressed
     * @throws SQLException
     */
    public void viewFinesButton(ActionEvent event) throws SQLException {
        resource = "Fine";
        BC.setTitle(resource + " Statistics - " + time);
        BC.setVisible(true);
        BC.getData().clear();
        BC.setLegendVisible(false);

        String[][] fineData = getResourceManager().getFineData(time);

        int maxBookListing = MAX_LISTED;

        if (fineData.length < maxBookListing) {
            maxBookListing = fineData.length;
        }

        Data<String, Number>[] fines = new Data[maxBookListing];
        Series<String, Number> fineSeries = new XYChart.Series<>();
        for (int iCount = 0; iCount < maxBookListing; iCount++) {

            String inputDate = fineData[iCount][0];
            double fineAmount = Double.parseDouble(fineData[iCount][1]);

            fines[iCount] = new Data<String, Number>(inputDate, fineAmount);
            fineSeries.getData().add(fines[iCount]);
        }
        BC.getData().add(fineSeries);
    }
}
