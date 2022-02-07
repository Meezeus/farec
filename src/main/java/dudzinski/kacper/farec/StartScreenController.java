package dudzinski.kacper.farec;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StartScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    public Label changelogChanges;

    // Reads the changelog file and sets the label in the start_screen.
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get and read the changelog file.
            URL changelogURL = App.class.getResource("changelog.txt");
            List<String> changelogLines = Files.readAllLines(Paths.get(changelogURL.toURI()));

            // Get the latest entry in the changelog and set it to the label.
            String changelogEntry = "";
            boolean readingLatestChanges = false;
            for (String line : changelogLines) {
                if (!readingLatestChanges && line.matches("##\\s.*")) {
                    readingLatestChanges = true;
                }
                else if (readingLatestChanges && line.matches("##\\s.*")) {
                      break;
                }
                changelogEntry += line + "\n";
            }
            changelogChanges.setText(changelogEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCreateFAWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("create_FA_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) changelogChanges.getScene().getWindow();
        stage.setScene(scene);
    }

    public void openCreateREWindow() throws IOException{
        fxmlLoader = new FXMLLoader(App.class.getResource("create_RE_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) changelogChanges.getScene().getWindow();
        stage.setScene(scene);
    }

}
