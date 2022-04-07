package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import dudzinski.kacper.farec.regex.ParseTree;
import dudzinski.kacper.farec.regex.Parser;
import dudzinski.kacper.farec.regex.RegularExpression;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static dudzinski.kacper.farec.regex.RegularExpressionSettings.CONTAINER_COLOR;

/**
 * This is the controller for the view used for creating regular expressions.
 * This view is displayed when the user wants to create a regular expression, so
 * that it can be converted into a finite automaton. It allows the user to enter
 * a regex string to be parsed and displayed as a parse tree.
 */
public final class CreateREScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    @FXML
    private TextField regexStringTextField;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane blankPane;
    @FXML
    private Label infoLabel;
    @FXML
    private Button convertButton;

    private ParseTree parseTree;

    /**
     * Adds a listener to the regex string text field so that any changes to it
     * disable the convert button. Sets the background color of the scroll pane
     * and the blank pane.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Add a listener to the regex string text field so that any changes to
        // it disable the convert button.
        regexStringTextField.textProperty()
                            .addListener(
                                    (observable, oldValue, newValue)
                                            -> convertButton.setDisable(true));

        // Set the background color of the scroll pane.
        scrollPane.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));

        // Set the background color of the blank pane.
        blankPane.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));
    }

    /**
     * Opens a small window with help information. The window will block events
     * to other application windows. This method is called when the help button
     * is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openHelpWindow() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Help: Regular Expressions");
        fxmlLoader = new FXMLLoader(
                App.class.getResource("re_help_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, -1);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Parses the regex string from the regex string text field into a regular
     * expression. If parsing is successful, a parse tree is built and
     * displayed, and the info label is updated accordingly. If parsing is
     * unsuccessful, an error message is shown in the info label and any
     * currently displayed parse tree is removed. This method is called when the
     * parse button is pressed.
     */
    public void parseRegexString() {
        // Remove the current parse tree.
        scrollPane.setContent(blankPane);

        // Get the regex string and remove whitespace.
        String regexString = regexStringTextField.getText()
                                                 .replaceAll("\\s+", "")
                                                 .trim();

        // Try parsing the regex string. If it's valid, set the info label,
        // display the parse tree and enable the convert button.
        try {
            // Parse the regex string.
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);

            // Updated the info label and build and display the parse tree.
            infoLabel.setText("Regular expression is valid!");
            parseTree = new ParseTree(regularExpression);
            scrollPane.setContent(parseTree.getContainer());

            // Reset scroll bar position.
            scrollPane.setHvalue(0);
            scrollPane.setVvalue(0);

            // Enable the convert button.
            convertButton.setDisable(false);
        }
        // If invalid, set the info label and disable the convert button.
        catch (IllegalArgumentException e) {
            // Update the info label.
            infoLabel.setText(e.getMessage());

            // Disable the convert button.
            convertButton.setDisable(true);
        }
    }

    /**
     * Changes the view to the screen for converting a regular expression into a
     * finite automaton and passes the parse tree to the controller of the new
     * view. This method is called when the convert button is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openConvertREScreen() throws IOException {
        fxmlLoader = new FXMLLoader(
                App.class.getResource("convert_re_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                convertButton.getScene().getWidth(),
                                convertButton.getScene().getHeight());
        Stage stage = (Stage) convertButton.getScene().getWindow();
        stage.setScene(scene);

        // Get the controller for the new view and pass it the parse tree.
        ConvertREScreenController convertREScreenController =
                fxmlLoader.getController();
        convertREScreenController.setParseTree(parseTree);
    }

}
