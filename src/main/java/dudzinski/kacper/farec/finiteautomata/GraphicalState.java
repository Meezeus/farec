package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * This class represents a graphical representation of a finite automaton state.
 * This class concerns itself with just the graphical aspect of a state. As
 * such, the state does not keep track of any edges associated with it. The
 * state should be treated as an isolated graphical component.
 * <p>
 * A finite automaton state is a circular node. It is connected to other states
 * by directed edges (transitions). A state may be marked as initial or final.
 * An initial state has a short, incoming edge that is not connected to any
 * other state. A final state has a second, inner circle.
 *
 * @see SmartState
 */
public class GraphicalState {

    private final StackPane container = new StackPane();
    private boolean isInitial = false;
    private boolean isFinal = false;

    /**
     * Creates a graphical state. The state is neither initial nor final.
     *
     * @param circle the state circle
     */
    public GraphicalState(Circle circle) {
        container.getChildren().addAll(circle);
    }

    /**
     * Returns the container of this state. The container contains the state
     * circle as well as any extra marking (if the state is an initial or final
     * state).
     *
     * @return the container of this state
     */
    public StackPane getContainer() {
        return container;
    }

    /**
     * Returns whether this state is marked as an initial state.
     *
     * @return true if this state is marked as an initial state, false otherwise
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * Returns whether this state is marked as a final state.
     *
     * @return true if this state is marked as a final state, false otherwise
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Enables and disables this state as an initial state. If
     * <code>setInitial</code> is set to true, this state will be marked as an
     * initial state (if it is not already). If <code>setInitial</code> is set
     * to false, the initial state marking will be removed from this state (if
     * it is present).
     * <p>
     * The marking for an initial state is a short, incoming edge that is not
     * connected to any other state.
     *
     * @param setInitial whether to mark the state as an initial state or not
     */
    public void setAsInitial(boolean setInitial) {
        if (!isInitial && setInitial) {
            isInitial = true;
            GraphicalFiniteAutomatonBuilder.setAsInitial(this);
        }
        else if (isInitial && !setInitial) {
            isInitial = false;
            GraphicalFiniteAutomatonBuilder.setAsNonInitial(this);
        }
    }

    /**
     * Enables and disables the state as a final state. If <code>setFinal</code>
     * is set to true, this state will be marked as a final state (if it is not
     * already). If <code>setFinal</code> is set to false, the final state
     * marking will be removed from this state (if it is present).
     * <p>
     * The marking for a final state is a second, inner circle.
     *
     * @param setFinal whether to mark the state as a final state or not
     */
    public void setAsFinal(boolean setFinal) {
        if (!isFinal && setFinal) {
            isFinal = true;
            GraphicalFiniteAutomatonBuilder.setAsFinal(this);
        }
        else if (isFinal && !setFinal) {
            isFinal = false;
            GraphicalFiniteAutomatonBuilder.setAsNonFinal(this);
        }
    }

}
