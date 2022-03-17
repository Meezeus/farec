package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

/**
 * This class represents a graphical finite automaton. This class concerns
 * itself with just the graphical aspect of a finite automaton. As such, the
 * finite automaton does not keep track of its components, except for the
 * initial state and final state. The finite automaton should be treated as a
 * collection of isolated graphical components. It needs to keep track of its
 * own size through the minimum width and height of its container.
 * <p>
 * A finite automaton consists of a set of states with directed edges
 * (transitions) between them. One of the states is designated as the initial
 * state and a second, different state is designated as the final state.
 *
 * @see SimpleGraphicalFiniteAutomaton
 * @see ComplexGraphicalFiniteAutomaton
 * @see SmartFiniteAutomaton
 */
public abstract class GraphicalFiniteAutomaton {

    protected StackPane container = new StackPane();
    protected GraphicalState initialState;
    protected GraphicalState finalState;

    /**
     * Returns the container of this finite automaton. The container holds the
     * containers of all the components of this finite automaton.
     *
     * @return the container of this finite automaton
     */
    public StackPane getContainer() {
        return container;
    }

    /**
     * Enables and disables the marking on the initial state. If
     * <code>enable</code> is set to true, the initial state will be
     * marked as an initial state. If <code>enable</code> is set to false, the
     * initial state marking will be removed from the initial state.
     *
     * @param enable whether to enable the marking on the initial state
     */
    public void enableInitialState(boolean enable) {
        initialState.setAsInitial(enable);
    }

    /**
     * Enables and disables the marking on the final state. If
     * <code>enable</code> is set to true, the final state will be
     * marked as a final state. If <code>enable</code> is set to false, the
     * final state marking will be removed from the final state.
     *
     * @param enable whether to enable the marking on the final state
     */
    public void enableFinalState(boolean enable) {
        finalState.setAsFinal(enable);
    }

}
