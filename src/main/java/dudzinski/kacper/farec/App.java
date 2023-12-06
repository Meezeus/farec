package dudzinski.kacper.farec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is the main class of the application. It sets everything off by
 * displaying the start screen.
 */
public final class App extends Application {

    /**
     * Launches the app.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Creates the stage and sets the view to the start screen window.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(App.class.getResource("start_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("FAREC");
        stage.setScene(scene);
        stage.show();
        InputStream stream = App.class.getResourceAsStream("farec.jpg");
        if (stream != null) {
            Image icon = new Image(stream);
            stage.getIcons().add(icon);
        }
    }

}