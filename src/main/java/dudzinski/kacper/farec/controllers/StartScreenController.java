package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This is the controller for the start screen view. The start screen is
 * displayed to the user when the application is first launched. It contains a
 * changelog as well as buttons to take the user to the screens for creating
 * finite automata and regular expressions.
 */
public class StartScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    public Label changelog;
    public Button convertFAButton;
    public Button convertREButton;

    /**
     * Reads the changelog file and sets the changelog label to the latest
     * entry.
     */
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get and read the changelog file.
            URL changelogURL = App.class.getResource("changelog.txt");
            if (changelogURL != null) {
                List<String> changelogLines =
                        Files.readAllLines(Paths.get(changelogURL.toURI()));

                // Get the latest entry in the changelog and set the label.
                boolean readingLatestChanges = false;
                StringBuilder changelogEntry = new StringBuilder();
                for (String line : changelogLines) {
                    // If the line is ## when not yet reading the latest
                    // changes, the first entry has been reached.
                    if (!readingLatestChanges && line.matches("##\\s.*")) {
                        readingLatestChanges = true;
                    }
                    // If the line is ## when reading the latest changes, the
                    // second entry has been reached.
                    else if (readingLatestChanges && line.matches("##\\s.*")) {
                        break;
                    }
                    // Add the line to the changelogEntry.
                    changelogEntry.append(line).append("\n");
                }
                // Set the label.
                changelog.setText(changelogEntry.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the view to the screen for creating finite automata. This method
     * is called when the convertFAButton button is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openCreateFAWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "create_fa_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                convertFAButton.getScene().getWidth(),
                                convertFAButton.getScene().getHeight());
        Stage stage = (Stage) convertFAButton.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Changes the view to the screen for creating regular expressions. This
     * method is called when the convertREButton button is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openCreateREWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "create_re_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                convertREButton.getScene().getWidth(),
                                convertREButton.getScene().getHeight());
        Stage stage = (Stage) convertREButton.getScene().getWindow();
        stage.setScene(scene);
    }

}
