package dudzinski.kacper.farec;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuBarController {

    private FXMLLoader fxmlLoader;
    public MenuBar menuBar;

    public void openCreateFAWindow(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("create_FA_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
    }

    public void openCreateREWindow(ActionEvent event) throws IOException{
        fxmlLoader = new FXMLLoader(App.class.getResource("create_RE_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
    }

}
