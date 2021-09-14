package JavaFX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import Core.AccountManager;
import Core.EventNotificationThread;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Extends Application to launch the user interface. Also starts a second daemon
 * thread to notify users about upcoming events.
 *
 * @author Grzegorz Debicki, Marcos Pallikaras, Dominic Woodman
 * @author Martin Trifonov
 * @version 2.0
 */
public class Main extends Application {

    /**
     * Main method to launch the program.
     *
     * @param args Java command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the interface.
     *
     * @param primaryStage The primary window on which the interface is
     * displayed.
     */
    @Override
    public void start(Stage primaryStage) {
        //Tries to load the main interface screen and show it to the user
        try {
            Parent stage = FXMLLoader.load(getClass().getResource(SceneController.getMainInterface()));
            Scene scene = new Scene(stage);

            primaryStage.setScene(scene);
            primaryStage.setTitle("TaweLib");
            primaryStage.getIcons().add(new Image("/Resources/bookIcon.png"));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            TimerTask timerTask = new EventNotificationThread();
            //This should make the thread daemon, doesn't stop the JVM from exiting
            Timer timer = new Timer(true);

            //StartDate is set to 8am each day.
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.HOUR_OF_DAY, 8);
            startDate.set(Calendar.MINUTE, 0);
            startDate.set(Calendar.SECOND, 0);

            //Runs it after 60 seconds and then every 24 hours from now (easier to demonstrate it works)
            timer.scheduleAtFixedRate(timerTask, 0, 24 * 60 * 60 * 1000);

            //Runs it at 8am and every day at 8am
            // timer.scheduleAtFixedRate(timerTask, startDate.getTime(), 24 * 60 * 60 * 1000);
        }
    }

    /**
     * Saves current date as users last login.
     */
    @Override
    public void stop() {

        //get the account manager
        AccountManager am = SceneController.getAccountManager();

        //get the current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(new Date());

        //set the lastlogin to now
        am.setLastLogin(SceneController.USER_ID, now);
    }

}
