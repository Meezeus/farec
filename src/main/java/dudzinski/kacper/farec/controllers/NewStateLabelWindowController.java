package dudzinski.kacper.farec.controllers;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is the controller for the new state label window. The new state label
 * window is displayed when the user wants to rename a state. It contains a text
 * field where the user can enter the new text for the state label and a submit
 * button.
 */
public final class NewStateLabelWindowController {

    public TextField textField;
    private String newStateLabelText = null;

    /**
     * Saves the users choice of text and closes the window. This method is
     * called when the submit button is pressed.
     */
    public void submit() {
        newStateLabelText = textField.getText();
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns the users desired new text for the label of a state.
     *
     * @return the users desired new text for the label of a state
     */
    public String getNewStateLabelText() {
        return newStateLabelText;
    }

}
