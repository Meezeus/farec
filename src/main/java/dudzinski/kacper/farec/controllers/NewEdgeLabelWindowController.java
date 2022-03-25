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
 * button. There is also an empty string button for adding the empty string
 * symbol to the text field.
 * <p>
 * There are restrictions on what the label on an edge may be. An edge label
 * must be a valid regex operand, or a list of valid regex operands separated by
 * commas.
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
        // Get the valid operand pattern and remove the start and end anchors.
        String validOperands =
                RegularExpressionSettings.getValidRegexOperandPattern();
        validOperands =
                validOperands.substring(1, validOperands.length() - 1);

        // Create the valid label pattern.
        String validLabelPattern = "^"
                + validOperands
                + "(,\s?"
                + validOperands
                + ")*$";

        // Add a listener to the text field to enable/disable the submit button
        // depending on if the text conforms to the restrictions on an edge
        // label.
        textField.textProperty()
                 .addListener((observable, oldValue, newValue) -> {
                     String text = textField.getText();
                     submitButton.setDisable(!text.matches(validLabelPattern));
                 });
    }

    /**
     * Adds the empty string character to the text in the text field.
     */
    public void emptyString() {
        int pos = textField.getCaretPosition();
        textField.insertText(pos, EMPTY_STRING);
    }

    /**
     * Saves the users choice of text and closes the window. This method is
     * called when the submit button is pressed.
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
