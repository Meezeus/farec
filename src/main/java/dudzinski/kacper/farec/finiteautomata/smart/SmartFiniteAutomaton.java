package dudzinski.kacper.farec.finiteautomata.smart;

import dudzinski.kacper.farec.controllers.ConvertFAScreenController;
import dudzinski.kacper.farec.controllers.CreateFAScreenController;
import dudzinski.kacper.farec.finiteautomata.graphical.GraphicalFiniteAutomaton;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

import static dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings.*;

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
 * One of the states of the finite automaton is designated as the initial state
 * and a second, different state is designated as the final state.
 *
 * @see GraphicalFiniteAutomaton
 * @see SmartComponent
 */
public final class SmartFiniteAutomaton {

    private final CreateFAScreenController createFAController;
    private final Pane container = new Pane();
    private final ArrayList<SmartState> states = new ArrayList<>();
    private final ArrayList<SmartEdgeComponent> edges = new ArrayList<>();
    private ConvertFAScreenController convertFAController;
    private boolean underConstruction = true;
    private SmartState initialState;
    private SmartState finalState;

    /**
     * Creates a new finite automaton and sets the construction controller. The
     * controller is used during construction of this finite automaton, to
     * define how the user can interact with the components. Sets the background
     * color of the finite automaton container as well as its user interaction
     * behaviour.
     *
     * @param createFAController the controller for finite automaton
     *                           construction window
     */
    public SmartFiniteAutomaton(CreateFAScreenController createFAController) {
        // Set the controller for creating the finite automaton.
        this.createFAController = createFAController;

        // Set the background color of the container.
        container.setBackground(new Background(
                new BackgroundFill(CONTAINER_COLOR, CornerRadii.EMPTY,
                                   Insets.EMPTY)));

        // Set the user interaction behaviour for the container.
        createFAController.setFAContainerMouseControl(this);
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
     * Returns the list of edges for this finite automaton.
     *
     * @return the list of edges for this finite automaton
     */
    public ArrayList<SmartEdgeComponent> getEdges() {
        return edges;
    }

    /**
     * Adds the given state to this finite automaton. The state is added to the
     * list of states and its container is added to the container of this finite
     * automaton. The user interaction behaviour for the state is set. The
     * minimum size of this finite automaton container is also updated.
     *
     * @param state the state to add to this finite automaton
     */
    public void addState(SmartState state) {
        // Add the state.
        states.add(state);
        container.getChildren().add(state.getContainer());

        // Set the user interaction behaviour.
        if (underConstruction) {
            createFAController.setStateMouseControl(state);
        }
        else {
            convertFAController.setStateMouseControl(state);
        }

        // Update the minimum size of the finite automaton container.
        updateContainerSize();
    }

    /**
     * Removes the given state from this finite automaton. The state is removed
     * from the list of states and its container is removed from the container
     * of this finite automaton. If the state is the initial state or the final
     * state of this finite automaton, the corresponding class variables will be
     * set to <code>null</code>. Removing the state will also remove any edges
     * connected to it.
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
        Iterator<SmartEdgeComponent> it = edges.iterator();
        while (it.hasNext()) {
            SmartEdgeComponent edge = it.next();
            if (edge.getStartState() == state) {
                state.removeOutgoingEdge(edge);
                edge.getEndState().removeIncomingEdge(edge);
                container.getChildren().remove(edge.getContainer());
                it.remove();
            }
            else if (edge.getEndState() == state) {
                state.removeIncomingEdge(edge);
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
     * Adds the given edge to this finite automaton. The edge is added to the
     * list of edges and its container is added to the container of this finite
     * automaton. If there is already an edge that has the same start and end
     * state, it is replaced by the new edge. The list of outgoing edges of the
     * start state and the list of incoming edges of the end state is updated to
     * reflect the addition of the edge. The user interaction behaviour for the
     * edge is set. The states connected to the edge are checked for symmetric
     * edges.
     *
     * @param edge the edge to add to this finite automaton
     */
    public void addEdge(SmartEdgeComponent edge) {
        // Get the start and end states of the edge.
        SmartState startState = edge.getStartState();
        SmartState endState = edge.getEndState();

        // Check for an existing, equivalent edge.
        SmartEdgeComponent oldEdge = null;
        for (SmartEdgeComponent existingEdge : edges) {
            if ((existingEdge.getStartState() == startState) &&
                (existingEdge.getEndState() == endState)) {
                oldEdge = existingEdge;
            }
        }

        // If there is an existing, equivalent edge, remove it.
        if (oldEdge != null) {
            removeEdge(oldEdge);
        }

        // Update the states connected to the edge.
        startState.addOutgoingEdge(edge);
        endState.addIncomingEdge(edge);

        // Add the edge.
        edges.add(edge);
        container.getChildren().add(0, edge.getContainer());

        // Set the user interaction behaviour.
        if (underConstruction) {
            createFAController.setEdgeMouseControl(edge);
        }
        else {
            convertFAController.setEdgeMouseControl(edge);
        }

        // Check for symmetric edges.
        checkSymmetricEdges(startState, endState);
    }

    /**
     * Removes the given edge from this finite automaton. The edge is removed
     * from the list of edges and its container is removed from the container of
     * this finite automaton. The list of outgoing edges of the start state and
     * the list of incoming edges of the end state is updated to reflect the
     * removal of the edge. The states connected to the edge are checked for
     * symmetric edges.
     *
     * @param edge the edge to remove from this finite automaton
     */
    public void removeEdge(SmartEdgeComponent edge) {
        // Get the start and end states of the edge.
        SmartState startState = edge.getStartState();
        SmartState endState = edge.getEndState();

        // Update the states connected to the edge.
        startState.removeOutgoingEdge(edge);
        endState.removeIncomingEdge(edge);

        // Remove the edge.
        edges.remove(edge);
        container.getChildren().remove(edge.getContainer());

        // Check for symmetric edges.
        checkSymmetricEdges(startState, endState);
    }

    /**
     * Returns the initial state of this finite automaton. May be null if the
     * automaton is still under construction.
     *
     * @return the initial state of this finite automaton
     */
    public SmartState getInitialState() {
        return initialState;
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
     * Returns the final state of this finite automaton. May be null if the
     * automaton is still under construction.
     *
     * @return the final state of this finite automaton
     */
    public SmartState getFinalState() {
        return finalState;
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
     * Updates the minimum size of the container of this finite automaton such
     * that all the components are within the new minimum size. The minimum
     * height can only increase: it will not decrease.
     */
    public void updateContainerSize() {
        // Find the greatest translateX and translateY of any state in the
        // finite automaton.
        double greatestX = Double.NEGATIVE_INFINITY;
        double greatestY = Double.NEGATIVE_INFINITY;

        for (SmartState state : states) {
            double stateX = state.getContainer().getTranslateX();
            double stateY = state.getContainer().getTranslateY();
            if (stateX > greatestX) {
                greatestX = stateX;
            }
            if (stateY > greatestY) {
                greatestY = stateY;
            }
        }

        // Calculate the new minimum width and height.
        double newMinWidth = greatestX + STATE_RADIUS + STATE_STROKE_RADIUS;
        double newMinHeight = greatestY + STATE_RADIUS + STATE_STROKE_RADIUS;

        // If the new minimum width or height has increased, update the
        // container.
        if (newMinWidth > container.getMinWidth()) {
            container.setMinWidth(newMinWidth);

        }
        if (newMinHeight > container.getMinHeight()) {
            container.setMinHeight(newMinHeight);
        }
    }

    /**
     * Checks whether this finite automaton is a valid finite automaton. A valid
     * finite automaton must have an initial state and a final state. Every
     * state in this finite automaton must be reachable from the initial state.
     *
     * @return true if this finite automaton is valid, false otherwise
     */
    public boolean isValid() {
        // If there is no initial state or no final state, the finite automaton
        // is not valid.
        if ((initialState == null) || (finalState == null)) {
            return false;
        }

        // Get a list of all states reachable from the initial state by doing
        // breadth-first search.
        ArrayList<SmartState> openList = new ArrayList<>();
        openList.add(initialState);
        ArrayList<SmartState> closedList = new ArrayList<>();
        while (!openList.isEmpty()) {
            SmartState currentState = openList.remove(0);
            closedList.add(currentState);
            for (SmartEdgeComponent outgoingEdge : currentState.getOutgoingEdges()) {
                SmartState child = outgoingEdge.getEndState();
                if (!openList.contains(child) && !closedList.contains(child)) {
                    openList.add(child);
                }
            }
        }

        // If there is a state that cannot be reached from the initial state,
        // the finite automaton is not valid.
        for (SmartState state : states) {
            if (!closedList.contains(state)) {
                return false;
            }
        }

        // Otherwise, the finite automaton is valid.
        return true;
    }

    /**
     * Finalises the construction of this finite automaton by changing its user
     * interaction behaviour to that of the controller for the window used to
     * convert finite automata into regular expressions.
     *
     * @param convertFAController the controller for the window used to convert
     *                            finite automata into regular expressions
     */
    public void finalise(ConvertFAScreenController convertFAController) {
        underConstruction = false;
        this.convertFAController = convertFAController;
        convertFAController.setFAContainerMouseControl(this);
        for (SmartState state : states) {
            convertFAController.setStateMouseControl(state);
        }
        for (SmartEdgeComponent edge : edges) {
            convertFAController.setEdgeMouseControl(edge);
        }
    }

    /**
     * Checks for symmetric edges between two states. Symmetric edges are edges
     * between two states in both directions. If symmetric edges are found, the
     * edges are turned into curved edges. If there is only one edge between the
     * two states, it is turned into a straight edge. If there are no edges
     * between the two states, nothing happens.
     *
     * @param state1 one of the two states
     * @param state2 one of the two states
     */
    private void checkSymmetricEdges(SmartState state1, SmartState state2) {
        // If the two states are the same, they cannot have symmetric edges.
        if (state1 == state2) {
            return;
        }

        // Get the pair of edges between the two states.
        Pair<SmartEdge, SmartEdge> edgePair = getEdgePair(state1, state2);
        SmartEdge edge1 = edgePair.getKey();
        SmartEdge edge2 = edgePair.getValue();

        // If there are symmetric edges, turn them into curved edges.
        if ((edge1 != null) && (edge2 != null)) {
            edge1.setCurved(true);
            edge2.setCurved(true);
        }
        // Otherwise, if there is only one edge, turn it into a straight edge.
        else if (edge1 != null) {
            edge1.setCurved(false);
        }
        else if (edge2 != null) {
            edge2.setCurved(false);
        }
    }

    /**
     * Returns a pair of edges between two different states.
     *
     * @param state1 one of the two states
     * @param state2 one of the two states
     * @return a pair (K, V), where K is an edge from state1 to state2 (or
     * <code>null</code> if such an edge does not exist) and V is an edge from
     * state2 to state1 (or <code>null</code> if such an edge does not exist)
     * @throws IllegalArgumentException if the two states are the same
     */
    private Pair<SmartEdge, SmartEdge> getEdgePair(SmartState state1,
                                                   SmartState state2)
            throws IllegalArgumentException {
        // Check that the states are different.
        if (state1 == state2) {
            throw new IllegalArgumentException(
                    "The two states cannot be the same!");
        }

        // Declare the edges
        SmartEdge s1ToS2 = null;
        SmartEdge s2ToS1 = null;

        // Get the edge from state1 to state2 if it exists.
        for (SmartEdgeComponent edge : state1.getOutgoingEdges()) {
            if (edge.getEndState() == state2) {
                s1ToS2 = (SmartEdge) edge;
            }
        }

        // Get the edge from state2 to state1 if it exists.
        for (SmartEdgeComponent edge : state2.getOutgoingEdges()) {
            if (edge.getEndState() == state1) {
                s2ToS1 = (SmartEdge) edge;
            }
        }

        // Return the edges.
        return new Pair<>(s1ToS2, s2ToS1);
    }

}
