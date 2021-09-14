package JavaFX;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the page displaying the trailer
 *
 * @author Alistair Bates
 * @version 1.3
 */
public class TrailerPageController extends SceneController implements Initializable {

    //A WebView from the FXML for loading in the web pages
    @FXML
    private WebView webView;

    //The name of the resource of which the trailer is being searched
    private String searchTerm = "";

    //A listener to check whether the page has been loaded in the WebView's engine
    private ChangeListener stateListener;

    //The address of the video to check whether the page has been changed after being loaded
    private String videoAddress = "";

    /**
     * This method is ran when the page is first loaded. It searched for the
     * trailer for the resource and then displays it in the WebView as an
     * embedded youtube video.
     *
     * @param location The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or
     * null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        //Gets the search term as the name of the resource
        searchTerm = getRequestResource().getTitle();

        //Replaces each of the spaces in the search term with +s for the URL
        String searchTermTemp = searchTerm.replaceAll("\\s", "+");

        //Gets the engine of the WebView
        WebEngine engine = webView.getEngine();

        //Loads the WebView engine with a URL which loads the first result in a google search for the trailer
        engine.load("http://www.google.com/search?btnI=I%27m+Feeling+Lucky&ie=UTF-8&oe=UTF-8&q=youtube+" + searchTermTemp + "+trailer");

        //Creates a listener to check whether the page has been loaded in the WebView
        stateListener = (observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                //Gets the history of all pages loaded in the WebView
                ObservableList<WebHistory.Entry> entries = engine.getHistory().getEntries();
                try {
                    //If the embedded youtube video has not been loaded yet, load it and save the address
                    if (videoAddress.equals("")) {
                        String url = entries.get(0).getUrl();
                        String[] parts = url.split("=");
                        videoAddress = parts[1];
                        engine.load("http://www.youtube.com/embed/" + videoAddress);
                        //If then another page is loaded, throw an error
                    } else if (!entries.get(entries.size() - 1).getUrl().contains(videoAddress)) {
                        throw new ArrayIndexOutOfBoundsException();
                    }
                    //Catch either the error thrown in another page is loaded or if the first result is not a video and load an error message into the HTML
                } catch (ArrayIndexOutOfBoundsException e) {
                    engine.loadContent("<h>Error: trailer not found.</h>");
                    engine.getLoadWorker().stateProperty().removeListener(stateListener);
                }
            }
        };
        //Add the listener to the engine
        engine.getLoadWorker().stateProperty().addListener(stateListener);
    }

    //Exits the program and loads the engine with a blank HTML
    /**
     * Stops the video from playing.
     */
    public void exit() {
        webView.getEngine().load("");
    }
}
