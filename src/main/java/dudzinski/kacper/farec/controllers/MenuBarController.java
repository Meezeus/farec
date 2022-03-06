package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller for the menu bar.
 */
public class MenuBarController {

    private FXMLLoader fxmlLoader;
    public MenuBar menuBar;

    /**
     * This method is called when the Convert finite automata menu item is clicked. It opens the window for creating
     * finite automata.
     */
    public void openCreateFAWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("create_FA_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), menuBar.getScene().getWidth(), menuBar.getScene().getHeight());
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * This method is called when the Convert regular expression menu item is clicked. It opens the window for creating
     * regular expressions.
     */
    public void openCreateREWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("create_RE_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), menuBar.getScene().getWidth(), menuBar.getScene().getHeight());
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
    }

}
