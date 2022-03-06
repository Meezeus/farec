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
 * This class is the controller for the start screen view.
 */
public class StartScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    public Label changelogChanges;
    public Button convertFAButton;
    public Button convertREButton;

    /**
     * Reads the changelog file and sets the label in the start_screen.
     */
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get and read the changelog file.
            URL changelogURL = App.class.getResource("changelog.txt");
            assert changelogURL != null;
            List<String> changelogLines = Files.readAllLines(Paths.get(changelogURL.toURI()));

            // Get the latest entry in the changelog and set it to the label.
            StringBuilder changelogEntry = new StringBuilder();
            boolean readingLatestChanges = false;
            for (String line : changelogLines) {
                if (!readingLatestChanges && line.matches("##\\s.*")) {
                    readingLatestChanges = true;
                }
                else if (readingLatestChanges && line.matches("##\\s.*")) {
                    break;
                }
                changelogEntry.append(line).append("\n");
            }
            changelogChanges.setText(changelogEntry.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when the Convert Finite Automata button is pressed. It opens the window for creating
     * finite automata.
     */
    public void openCreateFAWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("create_FA_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), convertFAButton.getScene().getWidth(), convertFAButton.getScene().getHeight());
        Stage stage = (Stage) convertFAButton.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * This method is called when the Convert Regular Expression button is pressed. It opens the window for creating
     * regular expressions.
     */
    public void openCreateREWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("create_RE_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), convertREButton.getScene().getWidth(), convertREButton.getScene().getHeight());
        Stage stage = (Stage) convertREButton.getScene().getWindow();
        stage.setScene(scene);
    }

}
