package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import dudzinski.kacper.farec.regex.ParseTree;
import dudzinski.kacper.farec.regex.Parser;
import dudzinski.kacper.farec.regex.RegularExpression;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the screen used to create regular expressions.
 */
public class CreateREScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    public TextField reInputField;
    public ScrollPane parseTreeContainer;
    public Label infoLabel;
    public Button helpButton;
    public Button parseButton;
    public Button convertButton;

    private ParseTree parseTree;

    /**
     * This method makes sure the input field starts off focused, and adds a listener to it so that any changes disable
     * the convert button.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Make sure the text field starts off focused.
        Platform.runLater(() -> reInputField.requestFocus());
        // Disable Convert button when the text in the input field is changed.
        reInputField.textProperty().addListener((observable, oldValue, newValue) -> convertButton.setDisable(true));
    }

    /**
     * This method is called when the Help button is pressed. It opens a small window with help information.
     */
    public void openHelpWindow() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Help: Regular Expressions");
        fxmlLoader = new FXMLLoader(App.class.getResource("help_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * This method is called when the Parse button is pressed. It will get the parser to parse the regular expression
     * entered in the text field. If the expression is valid, a parse tree is built and displayed and the info label is
     * updated accordingly. If the expression is invalid, an error message is shown and any currently displayed parse
     * tree is removed.
     */
    public void parseRegexString() {
        parseTreeContainer.setContent(new Label());     // Label makes sure that scroll bars disappear.
        // Get the regex string and remove whitespace.
        String regexString = reInputField.getText().replaceAll("\\s+", "").trim();
        try {
            RegularExpression regularExpression = Parser.parse(regexString);
            infoLabel.setText("Regular expression is valid!");
            parseTree = new ParseTree(regularExpression);
            parseTreeContainer.setContent(parseTree);
            // Reset scroll bar position.
            parseTreeContainer.setHvalue(0);
            parseTreeContainer.setVvalue(0);
            convertButton.setDisable(false);
        }
        catch (IllegalArgumentException e) {
            infoLabel.setText(e.getMessage());
            convertButton.setDisable(true);
        }
    }

    /**
     * This method is called when the Convert button is pressed. It will change the window to the RE conversion window.
     */
    public void openConvertREWindow() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("convert_RE_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), convertButton.getScene().getWidth(), convertButton.getScene().getHeight());
        Stage stage = (Stage) convertButton.getScene().getWindow();
        stage.setScene(scene);
        ConvertREScreenController convertREScreenController = fxmlLoader.getController();
        convertREScreenController.setParseTree(parseTree);
    }

}
