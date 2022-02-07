package dudzinski.kacper.farec;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        String entry = "";
        BufferedReader reader = null;
        try {
            boolean readingLatestChanges = false;
            File changelog = new File(App.class.getResource("changelog.txt").toURI());
            reader = new BufferedReader(new FileReader(changelog));
            String line = reader.readLine();
            while (line != null) {
                entry += line + "\n";
                line = reader.readLine();
                if (line != null && !readingLatestChanges && line.matches("##\\s.*")) {
                    readingLatestChanges = true;
                }
                else if (line != null && readingLatestChanges && line.matches("##\\s.*")) {
                      break;
                }
            }
            changelogChanges.setText(entry);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null)  {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void openCreateFAWindow(ActionEvent event) throws IOException{
        fxmlLoader = new FXMLLoader(App.class.getResource("create_FA_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) changelogChanges.getScene().getWindow();
        stage.setScene(scene);
    }

    public void openCreateREWindow(ActionEvent event) throws IOException{
        fxmlLoader = new FXMLLoader(App.class.getResource("create_RE_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) changelogChanges.getScene().getWindow();
        stage.setScene(scene);
    }

}
