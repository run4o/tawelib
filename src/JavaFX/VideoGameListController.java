package JavaFX;

import Core.Resource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Creates a list of VideoGames from a query.
 *
 * @author Rory Richards
 * @version 1.0
 */
public class VideoGameListController extends SceneController implements Initializable {

    /**
     * Paginated viewer of VideoGames.
     */
    @FXML
    private Pagination videoGameView;

    /**
     * List of resources.
     */
    private Resource[] resourceList = null;

    /**
     * Initialises VideoGame List.
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            try {
                resourceList = getResourceManager().getResourceList(getSqlQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        videoGameView.setPageFactory(this::createPage);
    }

    /**
     * Creates pages using pagination to fill the list with VideoGames.
     *
     * @param pageIndex Index of the page
     * @return box An HBox containing pagination with the list of VideoGames.
     */
    private HBox createPage(int pageIndex) {
        double elementsPerPage = 3;
        HBox box = new HBox(elementsPerPage);
        videoGameView.setPageCount((int) (Math.ceil((double) resourceList.length / elementsPerPage)));
        int page = pageIndex * (int) elementsPerPage;
        for (int i = page; i < page + elementsPerPage; i++) {
            if (i < resourceList.length) {

                VBox element = new VBox(elementsPerPage);
                element.setId(String.valueOf(i));
                ImageView image = new ImageView();
                try {
                    image.setImage(new Image(getResourceManager().
                            getImageURL(resourceList[i].getThumbImage())));
                } catch (SQLException e) {
                    image.setImage(new Image("/Resources/videoGameIcon.png"));
                }

                image.setFitWidth(100);
                image.setPreserveRatio(true);
                image.setSmooth(true);
                image.setCache(true);

                Label text = new Label(resourceList[i].getTitle());
                text.wrapTextProperty().setValue(true);
                Label availability = new Label(getAvailableNumberOfCopies(resourceList[i]));
                element.getChildren().addAll(availability, image, text);
                element.setAlignment(Pos.TOP_CENTER);
                element.setSpacing(10);
                element.setPrefWidth(200);
                element.setPadding(new Insets(100, 0, 0, 0));
                box.getChildren().add(element);
                getOnMouseClicked(resourceList, element);
            }
        }
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
