package JavaFX;

import Core.Resource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
 * @author Kaleb Tuck 963257
 * @version 1.0
 *
 * This controller displays resources created since the user last logged in, It
 * is an adapted version of the ListAll controller that uses resources created
 * after a date instead of all resources
 *
 *
 */
public class NewAdditionsInterfaceController extends SceneController implements Initializable {

    /**
     * paginated viewer of resources.
     */
    @FXML
    private Pagination resourceView;
    /**
     * list of resources.
     */
    private Resource[] resourceList = getResourceManager().getNewAdditions(SceneController.USER_ID, SceneController.getAccountManager());

    /**
     * Initialises the paginated list of resources.
     *
     * @param location The location used to resolve relative paths for the root
     * object.
     * @param resources The resources used to localize the root object.
     */
    public void initialize(URL location, ResourceBundle resources) {
        resourceView.setPageFactory(this::createPage);
    }

    /**
     * Generates a list of pages as a horizontal box in the interface.
     *
     * @param pageIndex the page number displayed beneath the box.
     *
     * @return box the generated horizontal box, containing the pages of the
     * list.
     */
    private HBox createPage(int pageIndex) {
        double elementsPerPage = 3;
        HBox box = new HBox(elementsPerPage);
        resourceView.setPageCount((int) (Math.ceil((double) resourceList.length / elementsPerPage)));
        int page = pageIndex * (int) elementsPerPage;
        for (int i = page; i < page + elementsPerPage; i++) {
            if (i < resourceList.length) {
                VBox element = new VBox(elementsPerPage);
                element.setId(String.valueOf(i));
                ImageView image = new ImageView();

                if (resourceList[i].toString().contains("Type - Book")) {
                    try {
                        image.setImage(new Image(getResourceManager().
                                getImageURL(resourceList[i].getThumbImage())));
                    } catch (SQLException e) {
                        image.setImage(new Image("/Resources/bookIcon.png"));
                    }
                } else if (resourceList[i].toString().contains("Type - Dvd")) {
                    try {
                        image.setImage(new Image(getResourceManager().
                                getImageURL(resourceList[i].getThumbImage())));
                    } catch (SQLException e) {
                        image.setImage(new Image("/Resources/dvdIcon.png"));
                    }
                } else if (resourceList[i].toString().contains("Type - Computer")) {
                    try {
                        image.setImage(new Image(getResourceManager().
                                getImageURL(resourceList[i].getThumbImage())));
                    } catch (SQLException e) {
                        image.setImage(new Image("/Resources/laptopIcon.png"));
                    }
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
                element.setPadding(new Insets(100, 0, 0, 0));
                element.setPrefWidth(200);
                box.getChildren().add(element);
                getOnMouseClicked(resourceList, element);
            }
        }
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * sorts the list to only show books.
     */
    @FXML
    public void handleSortByBookButtonAction() {
        try {
            resourceList = getResourceManager().getNewAdditionsResType(SceneController.USER_ID, SceneController.getAccountManager(), 1, SceneController.getDatabase());
            resourceView.setPageFactory(this::createPage);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Couldn't set type as book.");
            alert.showAndWait();
        }
    }

    /**
     * sorts the list to only show DVDs.
     */
    @FXML
    public void handleSortByDVDButtonAction() {
        try {
            resourceList = getResourceManager().getNewAdditionsResType(SceneController.USER_ID, SceneController.getAccountManager(), 2, SceneController.getDatabase());
            resourceView.setPageFactory(this::createPage);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Couldn't set type as DVD.");
            alert.showAndWait();
        }
    }

    /**
     * sorts the list to only show laptops.
     */
    @FXML
    public void handleSortByComputerButtonAction() {
        try {
            resourceList = getResourceManager().getNewAdditionsResType(SceneController.USER_ID, SceneController.getAccountManager(), 3, SceneController.getDatabase());
            resourceView.setPageFactory(this::createPage);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Couldn't set type as Computer.");
            alert.showAndWait();
        }
    }

    /**
     * sorts the list to only show video games.
     */
    @FXML
    public void handleSortByVideoGameButtonAction() {
        try {
            resourceList = getResourceManager().getNewAdditionsResType(SceneController.USER_ID, SceneController.getAccountManager(), 4, SceneController.getDatabase());
            resourceView.setPageFactory(this::createPage);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Couldn't set type as Video Games.");
            alert.showAndWait();
        }
    }
}
