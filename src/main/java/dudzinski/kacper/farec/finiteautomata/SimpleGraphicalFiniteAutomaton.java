package dudzinski.kacper.farec.finiteautomata;

/**
 * This class represents a simple graphical finite automaton. A simple finite
 * automaton is made up of only two states (an initial state and a final state)
 * with a single edge between them.
 *
 * @see GraphicalFiniteAutomaton
 * @see ComplexGraphicalFiniteAutomaton
 */
public class SimpleGraphicalFiniteAutomaton extends GraphicalFiniteAutomaton {

    /**
     * Creates a simple graphical finite automaton. Enables the marking on the
     * initial state and the final state.
     *
     * @param initialState the initial state of this finite automaton
     * @param finalState   the final state of this finite automaton
     * @param minWidth     the minimum width of this finite automaton
     * @param minHeight    the minimum height of this finite automaton
     */
    public SimpleGraphicalFiniteAutomaton(GraphicalState initialState,
                                          GraphicalState finalState,
                                          GraphicalEdge transition,
                                          double minWidth, double minHeight) {
        // Set the initial state and the final state.
        this.initialState = initialState;
        this.finalState = finalState;

        // Add the transition, initial state and final state containers to the
        // container of this finite automaton.
        container.getChildren().addAll(transition.getContainer(),
                                       initialState.getContainer(),
                                       finalState.getContainer());

        // Set the minimum width and height of this finite automaton.
        container.setMinSize(minWidth, minHeight);

        /* These two methods must be called after the initial state and final
        state containers have been added to the container of the finite
        automaton. */
        enableInitialState(true);
        enableFinalState(true);
    }

}
