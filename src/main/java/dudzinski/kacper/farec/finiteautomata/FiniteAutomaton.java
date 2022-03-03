package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

/**
 * This class represents a finite automaton: a set of states with labelled transitions between them. There are two
 * special states: an initial state and a final state.
 */
public abstract class FiniteAutomaton {

    protected StackPane initialState;
    protected StackPane finalState;
    protected StackPane finiteAutomatonPane = new StackPane();

    /**
     * @return The pane holding the finite automaton.
     */
    public StackPane getFiniteAutomatonPane() {
        return finiteAutomatonPane;
    }

    /**
     * Enables and disables the state as an initial state.
     * If enable is set to true, the initial state will be marked as an initial state.
     * If enable is set to false, the initial state will not be marked as an initial state.
     *
     * @param enable Whether to mark the initial state as an initial state.
     */
    public void enableInitialState(boolean enable) {
        if (enable) {
            FiniteAutomatonBuilder.setAsInitial(initialState);
        }
        else {
            FiniteAutomatonBuilder.setAsNonInitial(initialState);
        }
    }

    /**
     * Enables and disables the state as a final state.
     * If enable is set to true, the final state will be marked as a final state.
     * If enable is set to false, the final state will not be marked as a final state.
     *
     * @param enable Whether to mark the initial state as a final state.
     */
    public void enableFinalState(boolean enable) {
        if (enable) {
            FiniteAutomatonBuilder.setAsFinal(finalState);
        }
        else {
            FiniteAutomatonBuilder.setAsNonFinal(finalState);
        }
    }

}
