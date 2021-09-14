package JavaFX;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Displays most popular resources currently.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class PopularListingController extends SceneController implements Initializable {

    /**
     * PieChart to display the most popular resources.
     */
    @FXML
    private PieChart popularPie;

    /**
     * retrieves the resources specified as popular.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //The max number of resources listed.
        final int MAX_LISTED = 5;

        //get the popularity data.
        String[][] popularityData = null;
        try {
            popularityData = getResourceManager().getPopularityData();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //The maximum number of resources listed. This becomes the data count if smaller.
        int maxListing = MAX_LISTED;

        //If the data count is smaller than max listing then set max listing to data count.
        if (popularityData.length < maxListing) {
            maxListing = popularityData.length;
        }

        Data[] data = new Data[maxListing];

        //for every resource in the popularity data, add to piechart with its borrow count.
        for (int iCount = 0; iCount < maxListing; iCount++) {

            String title = popularityData[iCount][0];
            int borrowCount = Integer.parseInt(popularityData[iCount][1]);

            final int MAX_TITLE_LENGTH = 25;
            //If the title is longer than max length, the cut end and add "..."
            if (title.length() > MAX_TITLE_LENGTH) {
                title = title.substring(0, MAX_TITLE_LENGTH - 1) + "...";
            }
            data[iCount] = new Data(title, borrowCount);
        }

        popularPie.setData(FXCollections.observableArrayList(data));
        popularPie.setLegendVisible(false);
    }

}
