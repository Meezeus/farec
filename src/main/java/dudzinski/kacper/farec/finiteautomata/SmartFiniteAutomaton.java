package dudzinski.kacper.farec.finiteautomata;

import dudzinski.kacper.farec.controllers.CreateFAScreenController;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a smart finite automaton. A smart finite automaton is a
 * finite automaton consisting of smart components, which are components that
 * manage their own size, position, relationship to other components etc. This
 * makes the smart finite automaton much more dynamic.
 * <p>
 * A finite automaton consists of a set of states with directed edges
 * (transitions) between them. There may be only one transition between any pair
 * of states in each direction: multiple transitions between two states in the
 * same direction are not allowed. A state may have a single transition to
 * itself, called a loop, but multiple loops for the same state are not allowed.
 * One of the states is designated as the initial state and a second, different
 * state is designated as the final state.
 */
public class SmartFiniteAutomaton {

    private final CreateFAScreenController controller;
    private boolean underConstruction = true;
    private final Pane container = new Pane();
    private final ArrayList<SmartState> states = new ArrayList<>();
    private final ArrayList<SmartEdge> edges = new ArrayList<>();
    private SmartState initialState;
    private SmartState finalState;

    /**
     * Creates a new smart finite automaton and sets the controller. The
     * controller is during construction of this finite automaton, to define how
     * the user can interact with the components.
     *
     * @param controller the controller for finite automaton construction
     *                   window
     */
    public SmartFiniteAutomaton(CreateFAScreenController controller) {
        this.controller = controller;
    }

    /**
     * Returns the container of this finite automaton. The container holds the
     * containers of all the components of this finite automaton.
     *
     * @return the container of this finite automaton
     */
    public Pane getContainer() {
        return container;
    }

    /**
     * Returns the list of states for this finite automaton.
     *
     * @return the list of states for this finite automaton
     */
    public ArrayList<SmartState> getStates() {
        return states;
    }

    /**
     * Returns the list of edges for this finite automaton. The edges represent
     * transitions between states.
     *
     * @return the list of edges for this finite automaton
     */
    public ArrayList<SmartEdge> getEdges() {
        return edges;
    }

    /**
     * Adds a state to this finite automaton. The state is added to the list of
     * states and its container is added to the container of this finite
     * automaton. If this finite automaton is still under construction, the
     * controller is used to define the mouse control behaviour for the state.
     *
     * @param state the state to add to this finite automaton
     */
    public void addState(SmartState state) {
        states.add(state);
        container.getChildren().add(state.getContainer());
        if (underConstruction) {
            controller.setStateMouseControl(state);
        }
    }

    /**
     * Removes a state from this finite automaton. The state is removed from the
     * list of states and its container is removed from the container of this
     * finite automaton. If the state is the initial state or the final state of
     * this finite automaton, the corresponding class variables will be set to
     * <code>null</code>. Removing a state will also remove any edges connected
     * to that state.
     *
     * @param state the state to remove
     */
    public void removeState(SmartState state) {
        // If the state is an initial or final state, set these to null.
        if (initialState == state) {
            initialState = null;
        }
        if (finalState == state) {
            finalState = null;
        }

        // Remove edges attached to the state.
        Iterator<SmartEdge> it = edges.iterator();
        while (it.hasNext()) {
            SmartEdge edge = it.next();
            if (edge.getStartState() == state) {
                edge.getEndState().removeIncomingEdge(edge);
                container.getChildren().remove(edge.getContainer());
                it.remove();
            }
            else if (edge.getEndState() == state) {
                edge.getStartState().removeOutgoingEdge(edge);
                container.getChildren().remove(edge.getContainer());
                it.remove();
            }
        }

        // Remove the state.
        states.remove(state);
        container.getChildren().remove(state.getContainer());
    }

    /**
     * Adds an edge to this finite automaton. The edge is added to the list of
     * edges and its container is added to the container of this finite
     * automaton. If there is already an edge that has the same start state and
     * end state, it is replaced by the new edge. The list of outgoing edges of
     * the start state and the list of incoming edges of the end state is
     * updated to reflect the addition of the edge. If this finite automaton is
     * still under construction, the controller is used to define the mouse
     * control behaviour for the edge. If adding this edge results in symmetric
     * edges between two states (edges in both directions), the two symmetric
     * edges are replaced with curved edges.
     *
     * @param edge the edge to add to this finite automaton
     */
    public void addEdge(SmartEdge edge, boolean checkSymmetry) {
        // Check for existing, equivalent edges.
        SmartEdge oldEdge = null;
        for (SmartEdge existingEdge : edges) {
            if ((existingEdge.getStartState() == edge.getStartState())
                    && (existingEdge.getEndState() == edge.getEndState())) {
                oldEdge = existingEdge;
            }
        }

        // If there is an existing, equivalent edge, remove it.
        if (oldEdge != null) {
            removeEdge(oldEdge, true);
        }

        // Update the states connected to the edge.
        edge.getStartState().addOutgoingEdge(edge);
        edge.getEndState().addIncomingEdge(edge);

        // Add the edge.
        edges.add(edge);
        container.getChildren().add(0, edge.getContainer());
        if (underConstruction) {
            controller.setEdgeMouseControl(edge);
        }

        // Check for symmetric edges.
        if (checkSymmetry) {
            addSymmetricEdges(edge);
        }
    }

    /**
     * Removes an edge from this finite automaton. The edge is removed from the
     * list of edges and its container is removed from the container of this
     * finite automaton. The list of outgoing edges of the start state and the
     * list of incoming edges of the end state is updated to reflect the removal
     * of the edge. If the edge to be removed is a symmetric edge (edges in both
     * directions between two states), the other symmetric edge is removed and
     * replaced with a straight edge.
     *
     * @param edge the edge to remove from this finite automaton
     */
    public void removeEdge(SmartEdge edge, boolean checkSymmetry) {
        // Check for symmetric edges.
        if (checkSymmetry) {
            removeSymmetricEdges(edge);
        }

        // Update the states connected to the edge.
        edge.getStartState().removeOutgoingEdge(edge);
        edge.getEndState().removeIncomingEdge(edge);

        // Remove the edge.
        edges.remove(edge);
        container.getChildren().remove(edge.getContainer());
    }

    /**
     * Sets the state as the initial state of this finite automaton. The state
     * is updated to look like an initial state. The previous initial state is
     * updated to look like a normal state. A state cannot be both initial and
     * final, so if the state is also the final state of this finite automaton,
     * it is removed as the final state.
     *
     * @param state the state to be set as the initial state of this finite
     *              automaton
     */
    public void setInitialState(SmartState state) {
        // If there is already an initial state, set it back to normal.
        if (initialState != null) {
            initialState.setAsInitial(false);
        }

        // If the state is also the final state, remove it as the final state.
        if (state == finalState) {
            finalState = null;
            state.setAsFinal(false);
        }

        // Set the state as the initial state.
        initialState = state;
        state.setAsInitial(true);
    }

    /**
     * Sets the state as the final state of this finite automaton. The state is
     * updated to look like a final state. The previous final state is updated
     * to look like a normal state. A state cannot be both initial and final, so
     * if the state is also the initial state of this finite automaton, it is
     * removed as the initial state.
     *
     * @param state the state to be set as the final state of this finite
     *              automaton
     */
    public void setFinalState(SmartState state) {
        // If there is already a final state, set it back to normal.
        if (finalState != null) {
            finalState.setAsFinal(false);
        }

        // If the state is also the initial state, remove it as the initial
        // state.
        if (state == initialState) {
            initialState = null;
            state.setAsInitial(false);
        }

        // Set the state as the final state.
        finalState = state;
        state.setAsFinal(true);
    }

    /**
     * Returns a pair of edges between two states.
     *
     * @param state1 one of the two states
     * @param state2 one of the two states
     * @return a pair (K, V), where K is an edge from state1 to state2 (or
     * <code>null</code>) and V is an edge from state2 to state1 (or
     * <code>null</code>)
     */
    private Pair<SmartEdge, SmartEdge> getPairEdges(SmartState state1,
                                                    SmartState state2) {
        // Declare the edges
        SmartEdge s1ToS2 = null;
        SmartEdge s2ToS1 = null;

        // Get the edge from state1 to state2 if it exists.
        for (SmartEdge edge : state1.getOutgoingEdges()) {
            if (edge.getEndState() == state2) {
                s1ToS2 = edge;
            }
        }

        // Get the edge from state2 to state1 if it exists.
        for (SmartEdge edge : state2.getOutgoingEdges()) {
            if (edge.getEndState() == state1) {
                s2ToS1 = edge;
            }
        }

        // Return the edges.
        return new Pair<>(s1ToS2, s2ToS1);
    }

    /**
     * Checks for and adds symmetric edges between two states, as a result of an
     * edge being added between them. Symmetric edges are edges between two
     * states in both directions. Symmetric edges must be curved so as not to
     * overlap each other. If adding the edge results in symmetric edges between
     * the two states, the edges are replaced by equivalent curved edges.
     *
     * @param edge the edge being added
     */
    public void addSymmetricEdges(SmartEdge edge) {
        // Get the states connected to the edge.
        SmartState state1 = edge.getStartState();
        SmartState state2 = edge.getEndState();

        // If the two states are the same, they cannot have symmetric edges.
        if (state1 == state2) {
            return;
        }

        // Get the pair of edges between the two states.
        Pair<SmartEdge, SmartEdge> edgePair = getPairEdges(state1, state2);

        // If there are symmetric edges, replace them with equivalent curved
        // edges.
        if ((edgePair.getKey() != null) && (edgePair.getValue() != null)) {
            // Remove the existing edge.
            SmartEdge edge1 = edgePair.getKey();
            removeEdge(edge1, false);
            // Add the new edge.
            SmartEdge curvedEdge1 =
                    SmartFiniteAutomatonBuilder.createSmartCurvedEdge(
                            edge1.getLabelText(),
                            edge1.getStartState(),
                            edge1.getEndState());
            addEdge(curvedEdge1, false);

            // Remove the existing edge.
            SmartEdge edge2 = edgePair.getValue();
            removeEdge(edge2, false);
            // Add the new edge.
            SmartEdge curvedEdge2 =
                    SmartFiniteAutomatonBuilder.createSmartCurvedEdge(
                            edge2.getLabelText(),
                            edge2.getStartState(),
                            edge2.getEndState());
            addEdge(curvedEdge2, false);
        }
    }

    /**
     * Checks for and removes symmetric edges between two states, as a result of
     * an edge being removed between them. Symmetric edges are edges between two
     * states in both directions. Symmetric edges must be curved so as not to
     * overlap each other. If the edge being removed is a symmetric edge, the
     * other symmetric edge is replaced by an equivalent straight edge.
     *
     * @param edge the edge being removed
     */
    public void removeSymmetricEdges(SmartEdge edge) {
        // Get the states connected to the edge.
        SmartState state1 = edge.getStartState();
        SmartState state2 = edge.getEndState();

        // If the two states are the same, they cannot have symmetric edges.
        if (state1 == state2) {
            return;
        }

        // Get the pair of edges between the two states.
        Pair<SmartEdge, SmartEdge> edgePair = getPairEdges(state1, state2);

        // If there are symmetric edges, replace the one not being removed by
        // an equivalent straight edge.
        if ((edgePair.getKey() != null) && (edgePair.getValue() != null)) {
            // Get one of the edges and check that it's not the edge being
            // removed.
            SmartEdge curvedEdge1 = edgePair.getKey();
            if (curvedEdge1 != edge) {
                // Replace the edge with an equivalent straight edge.
                removeEdge(curvedEdge1, false);
                SmartEdge edge1 = SmartFiniteAutomatonBuilder.createSmartEdge(
                        curvedEdge1.getLabelText(),
                        curvedEdge1.getStartState(),
                        curvedEdge1.getEndState());
                addEdge(edge1, false);
            }

            // Get one of the edges and check that it's not the edge being
            // removed.
            SmartEdge curvedEdge2 = edgePair.getValue();
            if (curvedEdge2 != edge) {
                // Replace the edge with an equivalent straight edge.
                removeEdge(curvedEdge2, false);
                SmartEdge edge2 = SmartFiniteAutomatonBuilder.createSmartEdge(
                        curvedEdge2.getLabelText(),
                        curvedEdge2.getStartState(),
                        curvedEdge2.getEndState());
                addEdge(edge2, false);
            }
        }
    }
}
