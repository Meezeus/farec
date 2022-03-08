package dudzinski.kacper.farec.finiteautomata;

/**
 * This class represents a simple finite automaton. A simple finite automaton is made up of only two states (an initial
 * state and a final state) with a labelled transition between them.
 */
public class SimpleFiniteAutomaton extends FiniteAutomaton {

    /**
     * Create a complex finite automaton.
     *
     * @param initialState The initial state of the simple finite automaton.
     * @param finalState   The final state of the simple finite automaton.
     * @param minWidth     The minimum width of the simple finite automaton.
     * @param minHeight    The minimum height of the simple finite automaton.
     */
    public SimpleFiniteAutomaton(State initialState, State finalState, Edge transition, double minWidth, double minHeight) {
        this.initialState = initialState;
        this.finalState = finalState;
        finiteAutomatonPane.getChildren().addAll(transition.getPane(), initialState.getPane(), finalState.getPane());
        finiteAutomatonPane.setMinSize(minWidth, minHeight);
        enableInitialState(true);
        enableFinalState(true);
    }

}
