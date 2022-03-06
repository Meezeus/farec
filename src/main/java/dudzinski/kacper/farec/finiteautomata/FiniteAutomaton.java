package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

/**
 * This class represents a finite automaton: a set of states with labelled transitions between them. There are two
 * special states: an initial state and a final state.
 */
public abstract class FiniteAutomaton {

    protected StackPane finiteAutomatonPane = new StackPane();
    protected State initialState;
    protected State finalState;

    /**
     * @return The pane holding the finite automaton.
     */
    public StackPane getFiniteAutomatonPane() {
        return finiteAutomatonPane;
    }

    /**
     * Enables and disables the initial state as an initial state.
     * If enable is set to true, the initial state will be marked as an initial state.
     * If enable is set to false, the initial state will not be marked as an initial state.
     *
     * @param enable Whether to mark the initial state as an initial state.
     */
    public void enableInitialState(boolean enable) {
        initialState.setAsInitial(enable);
    }

    /**
     * Enables and disables the final state as a final state.
     * If enable is set to true, the final state will be marked as a final state.
     * If enable is set to false, the final state will not be marked as a final state.
     *
     * @param enable Whether to mark the initial state as a final state.
     */
    public void enableFinalState(boolean enable) {
        finalState.setAsFinal(enable);
    }

}
