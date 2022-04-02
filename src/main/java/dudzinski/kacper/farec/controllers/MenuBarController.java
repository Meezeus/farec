package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller for the menu bar. The menu bar is found at the top of
 * every screen in the application. It contains buttons for converting finite
 * automata and regular expressions, saving and loading finite automata and
 * regular expressions, and opening the settings window.
 */
public final class MenuBarController {

    private FXMLLoader fxmlLoader;
    public MenuBar menuBar;

    /**
     * Changes the view to the screen for creating finite automata. This method
     * is called when the "convert finite automata" menu item is clicked.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openCreateFAScreen() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "create_fa_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                menuBar.getScene().getWidth(),
                                menuBar.getScene().getHeight());
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Changes the view to the screen for creating regular expressions. This
     * method is called when the "convert regular expression" menu item is
     * clicked.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openCreateREScreen() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "create_re_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                menuBar.getScene().getWidth(),
                                menuBar.getScene().getHeight());
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
    }

}
