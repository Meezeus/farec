package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings;
import dudzinski.kacper.farec.finiteautomata.smart.*;
import dudzinski.kacper.farec.regex.Parser;
import dudzinski.kacper.farec.regex.RegularExpressionSettings;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings.*;

/**
 * This is the controller for the view used to convert finite automata into
 * regular expressions.
 *
 * @see FiniteAutomatonSettings
 */
public class ConvertFAScreenController {

    /**
     * This enum represents the available work modes. The work mode changes how
     * the user can interact with the finite automaton. There are three work
     * modes:<br>
     * <ul>
     *     <li>SELECT:  allows the user to select a state to remove</li>
     *     <li>UPDATE:  allows the user to update edges</li>
     * </ul>>
     */
    enum WorkMode {
        SELECT, UPDATE
    }

    private static final Color USER_HIGHLIGHT_COLOR = Color.RED;
    private static final Color STATE_TO_REMOVE_COLOR = Color.DARKRED;
    private static final Color PATH_HIGHLIGHT_COLOR = Color.BLUE;
    private static final Color UPDATE_HIGHLIGHT_COLOR = Color.GREEN;
    private static final String SELECT_STRING = "Select a state to remove.";

    public ScrollPane scrollPane;
    public Label infoLabel;
    public Button prevButton;
    public Button nextButton;

    private SmartFiniteAutomaton finiteAutomaton;
    private SmartComponent currentlySelected;
    private Paint currentlySelectedColour;
    private WorkMode workMode = WorkMode.SELECT;
    private final ContextMenu loopContextMenu = createLoopContextMenu();
    private SmartState stateToRemove;
    private int incomingIndex = 0;
    private final ArrayList<SmartState> incomingStates = new ArrayList<>();
    private int outgoingIndex = 0;
    private final ArrayList<SmartState> outgoingStates = new ArrayList<>();
    private boolean updateFinished;

    /**
     * Starts off the process of converting the given finite automaton into a
     * regular expression.
     *
     * @param finiteAutomaton the finite automaton to convert into a regular
     *                        expression
     */
    public void setFiniteAutomaton(SmartFiniteAutomaton finiteAutomaton) {
        this.finiteAutomaton = finiteAutomaton;
        scrollPane.setContent(finiteAutomaton.getContainer());
        finiteAutomaton.finalise(this);

        // Get the previous initial and final states.
        SmartState oldInitialState = finiteAutomaton.getInitialState();
        SmartState oldFinalState = finiteAutomaton.getFinalState();

        // Add a new initial state.
        SmartState newInitialState = SmartFiniteAutomatonBuilder
                .createState("");
        newInitialState.getContainer().setTranslateX(
                oldInitialState.getContainer().getTranslateX() + 100);
        newInitialState.getContainer().setTranslateY(
                oldInitialState.getContainer().getTranslateY() + 100);
        finiteAutomaton.addState(newInitialState);
        finiteAutomaton.setInitialState(newInitialState);

        // Add a transition from the new initial state to the old initial state.
        SmartEdge iToI =
                SmartFiniteAutomatonBuilder
                        .createEdge(EMPTY_STRING,
                                    newInitialState,
                                    oldInitialState);
        finiteAutomaton.addEdge(iToI);

        // Add a new final state.
        SmartState newFinalState = SmartFiniteAutomatonBuilder
                .createState("");
        newFinalState.getContainer().setTranslateX(
                oldFinalState.getContainer().getTranslateX() + 100);
        newFinalState.getContainer().setTranslateY(
                oldFinalState.getContainer().getTranslateY() + 100);
        finiteAutomaton.addState(newFinalState);
        finiteAutomaton.setFinalState(newFinalState);

        // Add a transition from the old final state to the new final state.
        SmartEdge fToF =
                SmartFiniteAutomatonBuilder
                        .createEdge(EMPTY_STRING,
                                    oldFinalState,
                                    newFinalState);
        finiteAutomaton.addEdge(fToF);

        // Set the info label.
        infoLabel.setText(SELECT_STRING);
    }

    /**
     * Unselects the currently selected component and restores its
     * highlighting.
     */
    private void unselectCurrentlySelected() {
        if (currentlySelected != null) {
            currentlySelected.setStroke(currentlySelectedColour);
        }
        currentlySelected = null;
        currentlySelectedColour = null;
    }

    /**
     * Selects and highlights the clicked component.
     *
     * @param event the clicked object
     */
    private void selectComponent(Event event) {
        // Remove the currently selected component and its highlighting.
        unselectCurrentlySelected();

        // Find the container of the clicked component.
        Node target = (Node) event.getTarget();
        while ((target != null)
                && (!Objects.equals(target.getId(), "selectable"))) {
            target = target.getParent();
        }

        // If a container was found, find what component it belongs to. Select
        // and highlight that component.
        if (target instanceof Group container) {
            // Get the list of components.
            ArrayList<SmartComponent> components = new ArrayList<>();
            components.addAll(finiteAutomaton.getStates());
            components.addAll(finiteAutomaton.getEdges());

            // Find the component with a matching container to the one found.
            for (SmartComponent component : components) {
                if (component.getContainer() == container) {
                    currentlySelected = component;
                    currentlySelectedColour = component.getStroke();
                    component.setStroke(USER_HIGHLIGHT_COLOR);
                }
            }
        }
    }

    /**
     * Defines the behaviour for interacting with a state using a mouse.
     *
     * @param state the state for which to define mouse control behaviour
     */
    public void setStateMouseControl(SmartState state) {
        // Get the state container.
        Group container = state.getContainer();

        // Create the offset for dragging.
        class Offset {
            double x, y;
        }
        Offset offset = new Offset();

        // Clicking on the state will select it and set the offset.
        container.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                selectComponent(event);
                offset.x = container.getTranslateX() - event.getSceneX();
                offset.y = container.getTranslateY() - event.getSceneY();
                event.consume();
            }
        });

        // Start full drag. Used when creating edges.
        container.setOnDragDetected(event -> container.startFullDrag());

        // Dragging the mouse on the state will move it along with the mouse.
        container.setOnMouseDragged(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                // Don't allow the state to be moved past the left boundary.
                if (event.getSceneX() + offset.x >
                        STATE_RADIUS + STATE_STROKE_RADIUS) {
                    container.setTranslateX(event.getSceneX() + offset.x);
                }
                // Don't allow the state to be moved past the top boundary.
                if (event.getSceneY() + offset.y >
                        STATE_RADIUS + STATE_STROKE_RADIUS) {
                    container.setTranslateY(event.getSceneY() + offset.y);
                }
                // Update the size of the finite automaton container.
                finiteAutomaton.updateContainerSize();
            }
            event.consume();
        });

        // Remove the behaviour.
        container.setOnMouseDragReleased(event -> {
        });

        // Remove the behaviour.
        container.setOnContextMenuRequested(event -> {
        });
    }

    /**
     * Creates the context menu for a loop edge. The context menu for a loop
     * edge allows the user to flip the edge.
     *
     * @return the context menu for the loop edge
     */
    private ContextMenu createLoopContextMenu() {
        ContextMenu loopContextMenu = new ContextMenu();

        MenuItem flip = new MenuItem("Flip");
        flip.setOnAction(event -> {
            SmartLoopEdge loop = (SmartLoopEdge) currentlySelected;
            loop.flip();
        });

        loopContextMenu.getItems().addAll(flip);
        return loopContextMenu;
    }

    /**
     * Defines the behaviour for interacting with the edge using a mouse.
     *
     * @param edge the edge for which to define mouse control behaviour
     */
    public void setEdgeMouseControl(SmartEdgeComponent edge) {
        // Get the edge container.
        Group container = edge.getContainer();

        // Clicking on the edge will select it.
        container.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                selectComponent(event);
                event.consume();
            }
        });

        // Requesting the context menu for a loop edge will select it and show
        // the context menu.
        container.setOnContextMenuRequested(event -> {
            if (edge instanceof SmartLoopEdge) {
                selectComponent(event);
                loopContextMenu.show(container,
                                     event.getScreenX(),
                                     event.getScreenY());
            }
            event.consume();
        });
    }

    /**
     * Defines the behaviour for interacting with the finite automaton container
     * using a mouse.
     */
    public void setFAContainerMouseControl(
            SmartFiniteAutomaton finiteAutomaton) {
        // Get the finite automaton container.
        Pane container = finiteAutomaton.getContainer();

        // Clicking on the container will unselect the currently selected
        // component.
        container.setOnMousePressed(event -> unselectCurrentlySelected());
    }

    /**
     * Checks if the selected component is a valid state to remove. Updates the
     * info label accordingly. If a valid state was chosen, the state is
     * highlighted. A list of incoming states and a list of outgoing states is
     * also prepared. The work mode is then switched to UPDATE.
     */
    public void selectStateToRemove() {
        // If no state is selected, update the info label.
        if ((currentlySelected == null)
                || !(currentlySelected instanceof SmartState)) {
            infoLabel.setText(SELECT_STRING);
        }
        // If the state selected is the initial state, update the info
        // label.
        else if ((currentlySelected == finiteAutomaton.getInitialState())) {
            infoLabel.setText("You cannot select the initial state!");
        }
        // If the state selected is the final state, update the info label.
        else if (currentlySelected == finiteAutomaton.getFinalState()) {
            infoLabel.setText("You cannot select the final state!");
        }
        // Otherwise, the state selected is a valid state to remove.
        else {
            stateToRemove = (SmartState) currentlySelected;

            // Highlight the state to remove.
            stateToRemove.setFill(STATE_TO_REMOVE_COLOR);

            // Get the states with edges incoming to the state to remove.
            incomingStates.clear();
            for (SmartEdgeComponent incomingEdge :
                    stateToRemove.getIncomingEdges()) {
                incomingStates.add(incomingEdge.getStartState());
            }
            // Don't include the state to remove.
            incomingStates.remove(stateToRemove);

            // Get the states with edges incoming from the state to remove.
            outgoingStates.clear();
            for (SmartEdgeComponent outgoingEdge :
                    stateToRemove.getOutgoingEdges()) {
                outgoingStates.add(outgoingEdge.getEndState());
            }
            // Don't include the state to remove.
            outgoingStates.remove(stateToRemove);

            // Update the info label
            infoLabel.setText("Press next to begin updating the edges.");

            // Change work mode.
            workMode = WorkMode.UPDATE;
            updateFinished = false;
        }

    }

    /**
     * Updates the label on an edge of the finite automaton as a result of one
     * of its states being removed. Removes previous highlighting, and then
     * highlights the states and edges involved in the update. Generates and
     * sets the updated label on the edge (creating the edge if it did not
     * previously exist).
     */
    public void updateEdgeLabel() {
        // Remove highlighting.
        removeAllHighlighting();

        // Get and highlight the current start state.
        SmartState startState = incomingStates.get(incomingIndex);
        startState.setStroke(PATH_HIGHLIGHT_COLOR);

        // Get and highlight the current end state.
        SmartState endState = outgoingStates.get(outgoingIndex);
        endState.setStroke(PATH_HIGHLIGHT_COLOR);

        // Get the direct path.
        Pair<String, SmartEdgeComponent> directPath =
                getDirectPath(startState, endState);

        // Get the indirect path.
        Pair<String, ArrayList<SmartEdgeComponent>> indirectPath =
                getIndirectPath(startState, stateToRemove, endState);

        // Highlight the indirect path edges.
        indirectPath.getValue().forEach(
                edge -> {
                    if (edge != null) {
                        edge.setStroke(PATH_HIGHLIGHT_COLOR);
                    }
                });

        // Create the new label.
        String newLabel = directPath.getKey()
                + RegularExpressionSettings.getUnionOperatorChar()
                + indirectPath.getKey();

        // Simplify the label.
        String simplifiedLabel = simplifyLabel(newLabel);

        // If the edge being updated already exists, update its label and then
        // highlight the edge.
        SmartEdgeComponent updatedEdge = directPath.getValue();
        if (updatedEdge != null) {
            updatedEdge.setLabelText(simplifiedLabel);
            updatedEdge.setStroke(UPDATE_HIGHLIGHT_COLOR);
        }
        // If the edge does not exist, create it, set its label and then
        // highlight the edge.
        else {
            // If the start state and end state are different states, create
            // a straight edge.
            if (startState != endState) {
                updatedEdge =
                        SmartFiniteAutomatonBuilder
                                .createEdge(simplifiedLabel,
                                            startState,
                                            endState);
            }
            // Otherwise, the start state and end state are the same state.
            // Create a loop edge.
            else {
                updatedEdge =
                        SmartFiniteAutomatonBuilder
                                .createLoopEdge(simplifiedLabel,
                                                startState);
            }

            // Add the created edge to the finite automaton.
            finiteAutomaton.addEdge(updatedEdge);
        }

        // Highlight the updated edge.
        updatedEdge.setStroke(UPDATE_HIGHLIGHT_COLOR);

        // Update the info label.
        infoLabel.setText("New label: " + newLabel + " = " + simplifiedLabel);

        // Update the indices.
        outgoingIndex += 1;
        if (outgoingIndex == outgoingStates.size()) {
            outgoingIndex = 0;
            incomingIndex += 1;
            if (incomingIndex == incomingStates.size()) {
                incomingIndex = 0;
                updateFinished = true;
            }
        }
    }

    /**
     * Moves to the next step of the conversion process. In SELECT mode, the
     * user picks a state to remove. In UPDATE mode, edge labels are updated,
     * until all the updates are finished at which point the state is removed.
     */
    public void next() {
        // In SELECT mode, the user picks a state to remove.
        if (workMode == WorkMode.SELECT) {
            selectStateToRemove();
        }
        // In UPDATE mode, the edge labels are updated, until all the updates
        // are finished.
        else if ((workMode == WorkMode.UPDATE) && !updateFinished) {
            updateEdgeLabel();
        }

        // In UPDATE mode, when all the updates are finished, the state is
        // removed.
        else //noinspection ConstantConditions
            if ((workMode == WorkMode.UPDATE) && updateFinished) {
                // Remove highlighting.
                removeAllHighlighting();

                // Remove the state.
                finiteAutomaton.removeState(stateToRemove);
                stateToRemove = null;

                // If the finite automaton has only two states, the conversion
                // is complete.
                if (finiteAutomaton.getStates().size() == 2) {
                    // Get the regex string.
                    String regexString =
                            getDirectPath(finiteAutomaton.getInitialState(),
                                          finiteAutomaton.getFinalState())
                                    .getKey();

                    // Update the info label.
                    infoLabel.setText(
                            "Finished! The regular expression of the finite" +
                                    " automaton is: " +
                                    Parser.removeOuterBrackets(regexString));

                    // Disable the next button.
                    nextButton.setDisable(true);
                }
                // Otherwise, more states can be removed.
                else {
                    // Update the info label
                    infoLabel.setText(SELECT_STRING);

                    // Change work mode.
                    workMode = WorkMode.SELECT;
                }
            }

    }

    /**
     * Moves to the previous step of the conversion process. In SELECT mode, the
     * user picks a state to remove. In UPDATE mode, edge labels are updated,
     * until all the updates are finished at which point the state is removed.
     */
    public void prev() {

    }

    /**
     * Given a label in the form (A)+(B)|(C)|(D) where A, B, C and D are regular
     * expressions, returns the simplified version of the label. The label is
     * simplified according to the following rules (R is a regular expression):
     * <ul>
     *     <li>EMPTY_SET UNION R = R</li>
     *     <li>R CONCATENATION EMPTY_STRING = EMPTY_STRING CONCATENATION R = R</li>
     *     <li>If R = EMPTY_SET*, then R = EMPTY_STRING</li>
     * </ul>
     * <p>
     * In addition, unnecessary brackets are removed from the label: see
     * {@link Parser#simplifyRegexString(String)}
     *
     * @param label a label in the form (A)+(B)|(C)|(D) where A, B, C and D are
     *              regular expressions
     * @return a simplified version of the label
     */
    protected static String simplifyLabel(String label) {
        int depth = 0;
        int unionIndex = -1;
        int concatIndex1 = -1;
        int concatIndex2 = -1;

        // Find the positions of the root regex operators.
        for (int index = 0; index < label.length(); index++) {
            char currentChar = label.charAt(index);
            if (currentChar == '(') {
                depth++;
            }
            else if (currentChar == ')') {
                depth--;
            }
            else if ((depth == 0)
                    && (currentChar ==
                    RegularExpressionSettings.getUnionOperatorChar())) {
                unionIndex = index;
            }
            else if ((depth == 0)
                    && (currentChar ==
                    RegularExpressionSettings.getConcatenationOperatorChar())) {
                if (concatIndex1 == -1) {
                    concatIndex1 = index;
                }
                else if (concatIndex2 == -1) {
                    concatIndex2 = index;
                }
            }
        }

        // Get the component labels.
        String directLabel =
                label.substring(0, unionIndex);
        String indirectLabel1 =
                label.substring(unionIndex + 1, concatIndex1);
        // Note: don't include the star operator in the second indirect label.
        String indirectLabel2 =
                label.substring(concatIndex1 + 1, concatIndex2 - 1);
        String indirectLabel3 =
                label.substring(concatIndex2 + 1);

        // Get the component labels without outer brackets.
        String directLabelBracketless =
                Parser.removeOuterBrackets(directLabel);
        String indirectLabel1Bracketless =
                Parser.removeOuterBrackets(indirectLabel1);
        String indirectLabel2Bracketless =
                Parser.removeOuterBrackets(indirectLabel2);
        String indirectLabel3Bracketless =
                Parser.removeOuterBrackets(indirectLabel3);

        String[] simplifiedLabelArray = new String[7];

        // If the second indirect label is an empty set, then replace it with
        // an empty string.
        if (indirectLabel2Bracketless.equals(EMPTY_SET)) {
            indirectLabel2Bracketless = EMPTY_STRING;
        }

        // If the direct label is an empty set, ignore it and the union
        // operator.
        if (directLabelBracketless.equals(EMPTY_SET)) {
            simplifiedLabelArray[0] = "";
            simplifiedLabelArray[1] = "";
        }
        // Otherwise, include it and the union operator.
        else {
            simplifiedLabelArray[0] = directLabel;
            simplifiedLabelArray[1] = "" +
                    RegularExpressionSettings
                            .getUnionOperatorChar();
        }

        // If the first indirect label is not an empty string, include it.
        if (!indirectLabel1Bracketless.equals(EMPTY_STRING)) {
            simplifiedLabelArray[2] = indirectLabel1;

            // If one of the other indirect labels is also not an empty string,
            // include the first concatenation symbol.
            if (!indirectLabel2Bracketless.equals(EMPTY_STRING)
                    || !indirectLabel3Bracketless.equals(EMPTY_STRING)) {
                simplifiedLabelArray[3] = "" +
                        RegularExpressionSettings
                                .getConcatenationOperatorChar();
            }
            // Otherwise, ignore it.
            else {
                simplifiedLabelArray[3] = "";
            }
        }
        // Otherwise, ignore it and the first concatenation symbol.
        else {
            simplifiedLabelArray[2] = "";
            simplifiedLabelArray[3] = "";
        }

        // If the second indirect label is not an empty string, include it.
        if (!indirectLabel2Bracketless.equals(EMPTY_STRING)) {
            // Note: have to add the star operator back.
            simplifiedLabelArray[4] = indirectLabel2
                    + RegularExpressionSettings.getStarOperatorChar();

            // If the third indirect label is also not an empty string, include
            // the second concatenation symbol.
            if (!indirectLabel3Bracketless.equals(EMPTY_STRING)) {
                simplifiedLabelArray[5] = "" +
                        RegularExpressionSettings.
                                getConcatenationOperatorChar();
            }
            // Otherwise, ignore it.
            else {
                simplifiedLabelArray[5] = "";
            }
        }
        // Otherwise, ignore it and the second concatenation symbol.
        else {
            simplifiedLabelArray[4] = "";
            simplifiedLabelArray[5] = "";
        }

        // If the third indirect label is not an empty string we include it.
        if (!indirectLabel3Bracketless.equals(EMPTY_STRING)) {
            simplifiedLabelArray[6] = indirectLabel3;
        }
        // Otherwise, we ignore it.
        else {
            simplifiedLabelArray[6] = "";
        }

        // If all three indirect labels are an empty string, we have to include
        // one of them.
        if (indirectLabel1Bracketless.equals(EMPTY_STRING)
                && indirectLabel2Bracketless.equals(EMPTY_STRING)
                && indirectLabel3Bracketless.equals(EMPTY_STRING)) {
            simplifiedLabelArray[2] = EMPTY_STRING;
        }

        // If the label is in the form A UNION A, replace it with just A.
        if ((simplifiedLabelArray[0].equals(simplifiedLabelArray[2]))
                && (simplifiedLabelArray[3].equals(""))) {
            simplifiedLabelArray[1] = "";
            simplifiedLabelArray[2] = "";
        }

        // Build the simplified label.
        StringBuilder simplifiedLabelBuilder = new StringBuilder();
        Arrays.stream(simplifiedLabelArray).toList().forEach(
                simplifiedLabelBuilder::append);

        // Use the Parser to remove unnecessary brackets and return the
        // simplified label/
        return Parser.simplifyRegexString(simplifiedLabelBuilder.toString());
    }

    /**
     * Removes the highlighting from all states and edges in the finite
     * automaton and sets the currently selected component to
     * <code>null</code>.
     */
    private void removeAllHighlighting() {
        unselectCurrentlySelected();
        for (SmartState state : finiteAutomaton.getStates()) {
            state.setStroke(STATE_STROKE_COLOR);
        }
        for (SmartEdgeComponent edge : finiteAutomaton.getEdges()) {
            edge.setStroke(EDGE_STROKE_COLOR);
        }
    }

    /**
     * Returns the label (in brackets) and edge of the direct path between two
     * states. The direct path between two states is simply the edge between
     * them.
     *
     * @param startState the start state of the path
     * @param endState   the end state of the path
     * @return a pair ((K), V) where K is the label of the direct path between
     * the two states (or the empty set symbol if no such path exists) and V is
     * the edge between the two states (or <code>null</code> if no such edge
     * exists)
     */
    private Pair<String, SmartEdgeComponent> getDirectPath(
            SmartState startState, SmartState endState) {
        for (SmartEdgeComponent edge : finiteAutomaton.getEdges()) {
            if ((edge.getStartState() == startState)
                    && (edge.getEndState() == endState)) {
                return new Pair<>("(" + edge.getLabelText() + ")", edge);
            }
        }
        return new Pair<>("(" + EMPTY_SET + ")", null);
    }

    /**
     * Returns the label and a list of edges of the indirect path from the start
     * state to the end state, going through the middle state. The label of this
     * path is the concatenation of the labels of the individual direct paths,
     * with the star regex operator being applied to the path from the middle
     * state to the middle state. Thus, the label has the format:<br> (start to
     * middle) | (middle to middle)* | (middle to end)
     *
     * @param startState  the start state of the path
     * @param middleState the middle state of the path
     * @param endState    the end state of the path
     * @return a pair (K, V) where K is the label of the indirect path between
     * the start state and end state, going through the middle state, and V is a
     * list of edges on that path
     */
    private Pair<String, ArrayList<SmartEdgeComponent>> getIndirectPath(
            SmartState startState,
            SmartState middleState,
            SmartState endState) {
        // Create the list to store path edges.
        ArrayList<SmartEdgeComponent> pathEdges = new ArrayList<>();

        // Get the path from the start state to the middle state.
        Pair<String, SmartEdgeComponent> startToMiddle =
                getDirectPath(startState, middleState);

        // Get the path label.
        String startToMiddleLabel = startToMiddle.getKey();

        // If the path edge exists, add it to the path edges.
        if (startToMiddle.getValue() != null) {
            pathEdges.add(startToMiddle.getValue());
        }

        // Get the path from the middle state to the middle state.
        Pair<String, SmartEdgeComponent> middleToMiddle =
                getDirectPath(middleState, middleState);

        // Get the path label and add the star char.
        String middleToMiddleLabel = middleToMiddle.getKey()
                + RegularExpressionSettings.getStarOperatorChar();

        // If the path edge exists, add it to the path edges.
        if (middleToMiddle.getValue() != null) {
            pathEdges.add(middleToMiddle.getValue());
        }

        // Get the path from the middle state to the end state.
        Pair<String, SmartEdgeComponent> middleToEnd =
                getDirectPath(middleState, endState);

        // Get the path label.
        String middleToEndLabel = middleToEnd.getKey();

        // If the path edge exists, add it to the path edges.
        if (middleToEnd.getValue() != null) {
            pathEdges.add(middleToEnd.getValue());
        }

        // Create the path label for the indirect path.
        String pathLabel = startToMiddleLabel
                + RegularExpressionSettings.getConcatenationOperatorChar()
                + middleToMiddleLabel
                + RegularExpressionSettings.getConcatenationOperatorChar()
                + middleToEndLabel;

        // Return the path label and the path edges.
        return new Pair<>(pathLabel, pathEdges);
    }

}
