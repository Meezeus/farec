package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.regex.RegularExpressionSettings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static dudzinski.kacper.farec.Settings.EMPTY_STRING;

/**
 * This is the controller for the new edge label window. The new edge label
 * window is displayed when the user wants to rename an edge. It contains a text
 * field where the user can enter the new text for the edge label and a submit
 * button. There is also an empty string button for selecting the empty string
 * character as the new edge text.
 * <p>
 * There are restrictions on what the label on an edge may be. An edge label
 * must be a valid regex operand.
 */
public class NewEdgeLabelWindowController implements Initializable {

    public TextField textField;
    public Button submitButton;
    private String newEdgeLabelText = null;

    /**
     * Adds a listener to the text field in order to enforce the restrictions on
     * what the label on an edge may be.
     */
    public void initialize(URL location, ResourceBundle resources) {
        textField.textProperty()
                 .addListener((observable, oldValue, newValue) -> {
                     // Don't allow more than one character.
                     if (newValue.length() > 1) {
                         textField.setText(oldValue);
                     }

                     // Check if the text is valid and enable/disable the submit
                     // button accordingly.
                     String text = textField.getText();
                     //noinspection RedundantIfStatement
                     if ((text.length() == 1)
                             && text.matches(
                             RegularExpressionSettings
                                     .getValidRegexOperandPattern())) {
                         submitButton.setDisable(false);
                     }
                     else {
                         submitButton.setDisable(true);
                     }
                 });
    }

    /**
     * Sets the users choice of text as the empty string character, and then
     * calls {@link #submit()}.
     */
    public void emptyString() {
        textField.setText(EMPTY_STRING);
        submit();
    }

    /**
     * Saves the users choice of text and closes the window. This method is
     * called when the empty string or submit button is pressed.
     */
    public void submit() {
        newEdgeLabelText = textField.getText();
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns the users desired new text for the label of an edge.
     *
     * @return the users desired new text for the label of an edge
     */
    public String getNewEdgeLabelText() {
        return newEdgeLabelText;
    }


}
