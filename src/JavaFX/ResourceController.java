package JavaFX;

import Core.Book;
import Core.Computer;
import Core.Dvd;
import Core.Resource;
import Core.VideoGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Interface for resource management.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @author Rory Richards (2.0)
 * @version 2.0
 */
public class ResourceController extends SceneController {

    /**
     * FileChooser used to select a resource image.
     */
    private final FileChooser imageChooser = new FileChooser();

    /**
     * TextField for the resource ID to be searched for.
     */
    @FXML
    private TextField searchID;

    /**
     * Loads subscene to add book.
     */
    @FXML
    public void handleAddBookButtonAction() {
        loadSubscene(getAddBookInterface());
    }

    /**
     * Loads subscene to add DVD.
     */
    @FXML
    public void handleAddDVDButtonAction() {
        loadSubscene(getAddDvdInterface());
    }

    /**
     * Loads subscene to add laptop.
     */
    @FXML
    public void handleAddComputerButtonAction() {
        loadSubscene(getAddLaptopInterface());
    }

    /**
     * Loads subscene to add video game.
     */
    @FXML
    public void handleAddVideoGameButtonAction() {
        loadSubscene(getAddVideoGameInterface());
    }

    /**
     * Loads subscene to edit a resource.
     */
    @FXML
    public void handleEditResourceButtonAction() {
        try {
            loadSubscene(getResourceScene("Edit"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error in database!");
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: please select a valid resource identifier");
            alert.showAndWait();
        }
    }

    /**
     * Loads subscene to display log for specified resource.
     */
    @FXML
    public void handleResourceLogButtonAction() {
        loadSubscene(getResourceLogInterface());
    }

    /**
     * Cancels all current changes and returns to resource manager scene.
     */
    @FXML
    public void cancel() {
        loadSubscene(getResourceInterface());
    }

    /**
     * Returns the path of the add resource subscene depending on the resource
     * type.
     *
     * @param action The Add action selected by the user.
     * @return Constructed FXML path for add resource subscene.
     * @throws SQLException When connection to database fails.
     */
    public String getResourceScene(String action) throws SQLException {

        Resource addResource;

        if (!searchID.getText().isEmpty()) {
            addResource = getResourceManager().getResource(Integer.parseInt(searchID.getText()));
            if (addResource instanceof Book) {
                setRequestResource(addResource);
                return "/View/" + action + "BookInterface.fxml";
            }
            if (addResource instanceof Dvd) {
                setRequestResource(addResource);
                return "/View/" + action + "DVDInterface.fxml";
            }
            if (addResource instanceof Computer) {
                setRequestResource(addResource);
                return "/View/" + action + "LaptopInterface.fxml";
            }
            if (addResource instanceof VideoGame) {
                setRequestResource(addResource);
                return "/View/" + action + "VideoGameInterface.fxml";
            }
        } else {
            System.out.println("Specify the Resource ID.");
        }
        return null;
    }

    /**
     * Sets the thumbnail image of a resource.
     *
     * @param event the event triggered by clicking the button.
     *
     * @return the file path of the thumbnail image.
     *
     * @throws MalformedURLException When method creates an invalid URL.
     */
    public String setThumbnailImage(ActionEvent event) throws MalformedURLException {
        imageChooser.setInitialDirectory(new File("src/ResourceImages"));
        Node node = (Node) event.getSource();
        File file = imageChooser.showOpenDialog(node.getScene().getWindow());

        //Call setAvatar on absolute path
        String path = file.toURI().toURL().toExternalForm();
        path = path.replace("\\", "/");
//		final int LENGTH_OF_SRC = 3;
//		path = path.substring(path.indexOf("src") + LENGTH_OF_SRC);
        return path;
    }

    /**
     * Sets the avatar image of a user.
     *
     * @param event the event triggered by clicking the button.
     *
     * @return the file path of the avatar image.
     */
    public String setAvatar(ActionEvent event) {
        imageChooser.setInitialDirectory(new File("src/DefaultAvatars"));
        Node node = (Node) event.getSource();
        File file = imageChooser.showOpenDialog(node.getScene().getWindow());
        Path selectedPath = Paths.get(file.getAbsolutePath());

        String path = selectedPath.toString();
        path = path.replace("\\", "/");
        final int LENGTH_OF_SRC = 3;
        path = path.substring(path.indexOf("src") + LENGTH_OF_SRC);
        return path;
    }
}
