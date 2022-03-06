package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * This class represents a state in a finite automaton.
 */
public class State extends Control implements FiniteAutomatonComponent {

    private StackPane statePane = new StackPane();
    private Circle circle;
    private Label label;
    private boolean isInitial = false;
    private boolean isFinal = false;

    /**
     * Creates a state using the given components.
     *
     * @param circle The state circle.
     * @param label  The label of the state.
     */
    public State(Circle circle, Label label) {
        this.circle = circle;
        this.label = label;
        statePane.getChildren().addAll(circle, label);
    }

    /**
     * @return The StackPane holding the state.
     */
    public StackPane getPane() {
        return statePane;
    }

    /**
     * Set the stroke colour of the state.
     *
     * @param paint The new stroke colour.
     */
    public void setStroke(Paint paint) {
        circle.setStroke(paint);
    }

    /**
     * Sets the text of the state label.
     *
     * @param labelText The new text of the label.
     */
    public void setLabel(String labelText) {
        label.setText(labelText);
    }

    /**
     * @return Whether the state is an initial state.
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * @return Whether the state is a final state.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Enables and disables the state as an initial state.
     * If enable is set to true, the state will be marked as an initial state.
     * If enable is set to false, the state will not be marked as an initial state.
     *
     * @param setInitial Whether to mark the state as an initial state or not.
     */
    public void setAsInitial(boolean setInitial) {
        if (!isInitial && setInitial) {
            isInitial = true;
            FiniteAutomatonBuilder.setAsInitial(this);
        }
        else if (isInitial && !setInitial) {
            isInitial = false;
            FiniteAutomatonBuilder.setAsNonInitial(this);
        }
    }

    /**
     * Enables and disables the state as a final state.
     * If enable is set to true, the state will be marked as a final state.
     * If enable is set to false, the state will not be marked as a final state.
     *
     * @param setFinal Whether to mark the initial state as a final state or not.
     */
    public void setAsFinal(boolean setFinal) {
        if (!isFinal && setFinal) {
            isFinal = true;
            FiniteAutomatonBuilder.setAsFinal(this);
        }
        else if (isFinal && !setFinal) {
            isFinal = false;
            FiniteAutomatonBuilder.setAsNonFinal(this);
        }
    }

}
