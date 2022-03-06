package dudzinski.kacper.farec.finiteautomata;

import dudzinski.kacper.farec.regex.ComplexRegularExpression;
import dudzinski.kacper.farec.regex.RegexOperator;
import dudzinski.kacper.farec.regex.RegularExpression;
import dudzinski.kacper.farec.regex.SimpleRegularExpression;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * This class contains functions for building and changing finite automata.
 */
public class FiniteAutomatonBuilder {

    private static final double NODE_SEPARATION = 100;
    private static final double NODE_RADIUS = 20;
    private static final double NODE_STROKE_RADIUS = 1;
    private static final double EDGE_STROKE_RADIUS = 1;
    private static final double ARROWHEAD_SIZE = 10;
    private static final double INITIAL_EDGE_LENGTH = 25;
    private static final String EPSILON = "\u03B5";

    /**
     * Builds the finite automaton for the given regular expression.
     *
     * @param regularExpression The regular expression for which to build a finite automaton.
     * @return The finite automaton for the given regular expression.
     */
    public static FiniteAutomaton buildFiniteAutomaton(RegularExpression regularExpression) {
        if (regularExpression instanceof SimpleRegularExpression) {
            return buildSimpleFiniteAutomaton((SimpleRegularExpression) regularExpression);
        }
        else if (regularExpression instanceof ComplexRegularExpression complexRegularExpression) {
            RegexOperator regexOperator = complexRegularExpression.getOperator();
            if (regexOperator == RegexOperator.STAR) {
                FiniteAutomaton finiteAutomaton1 = FiniteAutomatonBuilder.buildFiniteAutomaton(complexRegularExpression.getLeftOperand());
                return FiniteAutomatonBuilder.buildComplexFiniteAutomatonStar(finiteAutomaton1);
            }
            else if (regexOperator == RegexOperator.CONCATENATION) {
                FiniteAutomaton finiteAutomaton1 = FiniteAutomatonBuilder.buildFiniteAutomaton(complexRegularExpression.getLeftOperand());
                FiniteAutomaton finiteAutomaton2 = FiniteAutomatonBuilder.buildFiniteAutomaton(complexRegularExpression.getRightOperand());
                return FiniteAutomatonBuilder.buildComplexFiniteAutomatonConcatenation(finiteAutomaton1, finiteAutomaton2);
            }
            else if (regexOperator == RegexOperator.UNION) {
                FiniteAutomaton finiteAutomaton1 = FiniteAutomatonBuilder.buildFiniteAutomaton(complexRegularExpression.getLeftOperand());
                FiniteAutomaton finiteAutomaton2 = FiniteAutomatonBuilder.buildFiniteAutomaton(complexRegularExpression.getRightOperand());
                return FiniteAutomatonBuilder.buildComplexFiniteAutomatonUnion(finiteAutomaton1, finiteAutomaton2);
            }
            else {
                throw new IllegalArgumentException("Regex operator is not STAR, CONCATENATION or UNION!");
            }
        }
        else {
            throw new IllegalArgumentException("Regular expression is neither Simple nor Complex!");
        }
    }

    /**
     * Builds a complex finite automaton by transforming the given finite automata using the STAR transformation.
     *
     * @param finiteAutomaton The finite automaton to be transformed.
     * @return The result of transforming the finite automaton using the STAR transformation.
     */
    private static FiniteAutomaton buildComplexFiniteAutomatonStar(FiniteAutomaton finiteAutomaton) {
        // Disable the initial and final states of the finite automaton.
        finiteAutomaton.enableInitialState(false);
        finiteAutomaton.enableFinalState(false);

        // Get the finite automaton pane.
        StackPane finiteAutomatonPane = finiteAutomaton.getFiniteAutomatonPane();

        // Create the new initial state and move it into position.
        StackPane initialState = createState("");
        initialState.setTranslateX(-(Math.abs(finiteAutomaton.initialState.getTranslateX()) + (2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS) + NODE_SEPARATION));

        // Create the new final state and move it into position.
        StackPane finalState = createState("");
        finalState.setTranslateX(Math.abs(finiteAutomaton.finalState.getTranslateX()) + (2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS) + NODE_SEPARATION);

        // Create the list for holding the new transitions and create the transition variables.
        ArrayList<StackPane> transitions = new ArrayList<>();
        double lineWidth;
        double lineHeight;

        // Calculate line width and height for i to i and f to f transitions.
        lineWidth = Math.abs(Math.abs(initialState.getTranslateX()) - Math.abs(finiteAutomaton.initialState.getTranslateX()));
        lineHeight = 0;

        // Connect the new initial state to the old initial state.
        StackPane iToI = createEdge(EPSILON, lineWidth, lineHeight, 0, true);
        iToI.setTranslateX(initialState.getTranslateX() + (0.5 * lineWidth));
        transitions.add(iToI);

        // Connect the old final state to the new final state.
        StackPane fToF = createEdge(EPSILON, lineWidth, lineHeight, 0, true);
        fToF.setTranslateX(finalState.getTranslateX() - (0.5 * lineWidth));
        transitions.add(fToF);

        // Calculate line width and height for f to i transition.
        lineWidth = Math.abs(Math.abs(finiteAutomaton.finalState.getTranslateX()) + Math.abs(finiteAutomaton.initialState.getTranslateX()));
        lineHeight = 0.5 * finiteAutomatonPane.getMinHeight() + 2 * NODE_RADIUS;

        // Connect the old final state to the old initial state.
        StackPane fToIUp = createEdge("", 0, lineHeight, -90, false);
        fToIUp.setTranslateX(finiteAutomaton.finalState.getTranslateX());
        fToIUp.setTranslateY(-(0.5 * lineHeight));
        transitions.add(fToIUp);

        StackPane fToILeft = createEdge(EPSILON, lineWidth, 0, 180, false);
        fToILeft.setTranslateY(-lineHeight);
        transitions.add(fToILeft);

        StackPane fToIDown = createEdge("", 0, lineHeight, 90, true);
        fToIDown.setTranslateX(finiteAutomaton.initialState.getTranslateX());
        fToIDown.setTranslateY(-(0.5 * lineHeight));
        transitions.add(fToIDown);

        // Calculate line width and height for i to f transition.
        lineWidth = Math.abs(Math.abs(finalState.getTranslateX()) + Math.abs(initialState.getTranslateX()));
        lineHeight = 0.5 * finiteAutomatonPane.getMinHeight() + 2 * NODE_RADIUS;

        // Connect the new initial state to the new final state.
        StackPane iToFDown = createEdge("", 0, lineHeight, 90, false);
        iToFDown.setTranslateX(initialState.getTranslateX());
        iToFDown.setTranslateY(0.5 * lineHeight);
        transitions.add(iToFDown);

        StackPane iToFRight = createEdge(EPSILON, lineWidth, 0, 0, false);
        iToFRight.setTranslateY(lineHeight);
        transitions.add(iToFRight);

        StackPane iToFUp = createEdge("", 0, lineHeight, -90, true);
        iToFUp.setTranslateX(finalState.getTranslateX());
        iToFUp.setTranslateY(0.5 * lineHeight);
        transitions.add(iToFUp);

        // Create the new complex finite automaton.
        double minWidth = (finalState.getTranslateX() - initialState.getTranslateX()) + (2 * NODE_STROKE_RADIUS) + (2 * NODE_RADIUS);
        double minHeight = finiteAutomatonPane.getMinHeight() + (2 * (0.5 * finiteAutomatonPane.getMinHeight() + 2 * NODE_RADIUS));
        return new ComplexFiniteAutomaton(initialState, finalState,
                finiteAutomatonPane, transitions, null,
                minWidth, minHeight);
    }

    /**
     * Builds a complex finite automaton by combining the two given finite automata using the CONCATENATION transformation.
     *
     * @param leftFiniteAutomaton  The first of the two finite automata to be combined.
     * @param rightFiniteAutomaton The second of the two finite automata to be combined.
     * @return A combination of the two finite automata using the CONCATENATION transformation.
     */
    private static FiniteAutomaton buildComplexFiniteAutomatonConcatenation(FiniteAutomaton leftFiniteAutomaton, FiniteAutomaton rightFiniteAutomaton) {
        // Disable the initial and final states of the left finite automaton.
        leftFiniteAutomaton.enableInitialState(false);
        leftFiniteAutomaton.enableFinalState(false);

        // Get the left finite automaton pane.
        StackPane leftFiniteAutomatonPane = leftFiniteAutomaton.getFiniteAutomatonPane();

        // Set the initial state to the initial state of the left finite automaton.
        StackPane initialState = leftFiniteAutomaton.initialState;

        // Remove the initial state from the left finite automaton.
        leftFiniteAutomatonPane.getChildren().remove(leftFiniteAutomaton.initialState);

        // Move the left finite automaton pane and the initial state to the left, such that the final state of the
        // left finite automaton is in the centre.
        leftFiniteAutomatonPane.setTranslateX(-(0.5 * leftFiniteAutomatonPane.getMinWidth()) + NODE_RADIUS + NODE_STROKE_RADIUS);
        initialState.setTranslateX(leftFiniteAutomatonPane.getTranslateX() + initialState.getTranslateX());

        // Disable the initial and final states of the right finite automaton.
        rightFiniteAutomaton.enableInitialState(false);
        rightFiniteAutomaton.enableFinalState(false);

        // Get the right finite automaton pane.
        StackPane rightFiniteAutomatonPane = rightFiniteAutomaton.getFiniteAutomatonPane();

        // Set the final state to the final state of the right finite automaton.
        StackPane finalState = rightFiniteAutomaton.finalState;

        // Remove the final state from the right finite automaton.
        rightFiniteAutomatonPane.getChildren().remove(rightFiniteAutomaton.finalState);

        // Move the right finite automaton pane and the final state to the right, such that the initial state of the
        // right finite automaton is in the centre.
        rightFiniteAutomatonPane.setTranslateX(0.5 * rightFiniteAutomatonPane.getMinWidth() - (NODE_RADIUS + NODE_STROKE_RADIUS));
        finalState.setTranslateX(rightFiniteAutomatonPane.getTranslateX() + finalState.getTranslateX());

        // Find the distance needed to centre the finite automaton.
        double offset = (rightFiniteAutomatonPane.getMinWidth() - leftFiniteAutomatonPane.getMinWidth()) / 2;
        // Move everything to the left by the offset (note offset may be negative, moving things to the right).
        leftFiniteAutomatonPane.setTranslateX(leftFiniteAutomatonPane.getTranslateX() - offset);
        initialState.setTranslateX(initialState.getTranslateX() - offset);
        rightFiniteAutomatonPane.setTranslateX(rightFiniteAutomatonPane.getTranslateX() - offset);
        finalState.setTranslateX(finalState.getTranslateX() - offset);

        // Create the new complex finite automaton.
        double minWidth = leftFiniteAutomatonPane.getMinWidth() + rightFiniteAutomatonPane.getMinWidth() - ((2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS));
        double minHeight = Math.max(leftFiniteAutomatonPane.getMinHeight(), rightFiniteAutomatonPane.getMinHeight());
        return new ComplexFiniteAutomaton(initialState, finalState,
                leftFiniteAutomatonPane, null, rightFiniteAutomatonPane,
                minWidth, minHeight);
    }

    /**
     * Builds a complex finite automaton by combining the two given finite automata using the UNION transformation.
     *
     * @param topFiniteAutomaton    The first of the two finite automata to be combined.
     * @param bottomFiniteAutomaton The second of the two finite automata to be combined.
     * @return A combination of the two finite automata using the UNION transformation.
     */
    private static FiniteAutomaton buildComplexFiniteAutomatonUnion(FiniteAutomaton topFiniteAutomaton, FiniteAutomaton bottomFiniteAutomaton) {
        // Disable the initial and final states of the top finite automaton.
        topFiniteAutomaton.enableInitialState(false);
        topFiniteAutomaton.enableFinalState(false);
        // Get the left finite automaton pane and move it above the baseline.
        StackPane topFiniteAutomatonPane = topFiniteAutomaton.getFiniteAutomatonPane();
        topFiniteAutomatonPane.setTranslateY(-((0.5 * topFiniteAutomatonPane.getMinHeight()) + NODE_RADIUS + NODE_STROKE_RADIUS));

        // Disable the initial and final states of the bottom finite automaton.
        bottomFiniteAutomaton.enableInitialState(false);
        bottomFiniteAutomaton.enableFinalState(false);
        // Get the right finite automaton pane and move it below the baseline.
        StackPane bottomFiniteAutomatonPane = bottomFiniteAutomaton.getFiniteAutomatonPane();
        bottomFiniteAutomatonPane.setTranslateY((0.5 * bottomFiniteAutomatonPane.getMinHeight()) + NODE_RADIUS + NODE_STROKE_RADIUS);

        // Find which automaton is the widest.
        FiniteAutomaton widestFiniteAutomaton = topFiniteAutomatonPane.getMinWidth() > bottomFiniteAutomatonPane.getMinWidth() ? topFiniteAutomaton : bottomFiniteAutomaton;

        // Create the new initial state and move it into position.
        StackPane initialState = createState("");
        initialState.setTranslateX(-(Math.abs(widestFiniteAutomaton.initialState.getTranslateX()) + (2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS) + NODE_SEPARATION));

        // Create the new final state and move it into position.
        StackPane finalState = createState("");
        finalState.setTranslateX(Math.abs(widestFiniteAutomaton.finalState.getTranslateX()) + (2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS) + NODE_SEPARATION);

        // Create the list for holding the new transitions and create the transition variables.
        ArrayList<StackPane> transitions = new ArrayList<>();
        double lineWidth;
        double lineHeight;
        double angle;

        // Calculate the width, height and angle of the new transitions for the top finite automaton.
        lineWidth = Math.abs(Math.abs(initialState.getTranslateX()) - Math.abs(topFiniteAutomaton.initialState.getTranslateX()));
        lineHeight = Math.abs(Math.abs(initialState.getTranslateY()) - Math.abs(topFiniteAutomatonPane.getTranslateY()));
        angle = Math.toDegrees(Math.atan(lineHeight / lineWidth));

        // Create the transition from the new initial state to the old initial state of the top finite automaton.
        StackPane iToI1 = FiniteAutomatonBuilder.createEdge(EPSILON, lineWidth, lineHeight, -angle, true);
        iToI1.setTranslateX(initialState.getTranslateX() + (0.5 * lineWidth));
        iToI1.setTranslateY(-(0.5 * lineHeight));
        transitions.add(iToI1);

        // Create the transition from the old final state of the top finite automaton to the new final state.
        StackPane f1ToF = FiniteAutomatonBuilder.createEdge(EPSILON, lineWidth, lineHeight, angle, true);
        f1ToF.setTranslateX(finalState.getTranslateX() - (0.5 * lineWidth));
        f1ToF.setTranslateY(-(0.5 * lineHeight));
        transitions.add(f1ToF);

        // Calculate the width, height and angle of the new transitions for the bottom finite automaton.
        lineWidth = Math.abs(Math.abs(initialState.getTranslateX()) - Math.abs(bottomFiniteAutomaton.initialState.getTranslateX()));
        lineHeight = Math.abs(Math.abs(initialState.getTranslateY()) - Math.abs(bottomFiniteAutomatonPane.getTranslateY()));
        angle = Math.toDegrees(Math.atan(lineHeight / lineWidth));

        // Create the transition from the new initial state to the old initial state of the bottom finite automaton.
        StackPane iToI2 = FiniteAutomatonBuilder.createEdge(EPSILON, lineWidth, lineHeight, angle, true);
        iToI2.setTranslateX(initialState.getTranslateX() + (0.5 * lineWidth));
        iToI2.setTranslateY(0.5 * lineHeight);
        transitions.add(iToI2);

        // Create the transition from the old final state of the bottom finite automaton to the new final state.
        StackPane f2ToF = FiniteAutomatonBuilder.createEdge(EPSILON, lineWidth, lineHeight, -angle, true);
        f2ToF.setTranslateX(finalState.getTranslateX() - (0.5 * lineWidth));
        f2ToF.setTranslateY(0.5 * lineHeight);
        transitions.add(f2ToF);

        // Create the new complex finite automaton.
        double tallestFiniteAutomatonHeight = Math.max(topFiniteAutomatonPane.getMinHeight(), bottomFiniteAutomatonPane.getMinHeight());
        double minWidth = (finalState.getTranslateX() - initialState.getTranslateX()) + (2 * NODE_STROKE_RADIUS) + (2 * NODE_RADIUS);
        double minHeight = (2 * tallestFiniteAutomatonHeight) + (2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS);

        return new ComplexFiniteAutomaton(initialState, finalState,
                topFiniteAutomatonPane, transitions, bottomFiniteAutomatonPane,
                minWidth, minHeight);
    }

    /**
     * Builds a simple finite automaton for the given simple regular expression. A simple finite automaton consists of
     * two states with a labelled edge between them. The label on the edge is a single character.
     *
     * @param simpleRegularExpression The simple regular expression for which the finite automaton corresponds to.
     * @return The finite automaton for the given simple regular expression.
     */
    private static SimpleFiniteAutomaton buildSimpleFiniteAutomaton(SimpleRegularExpression simpleRegularExpression) {
        // Create the initial state and move it into position.
        StackPane initialState = createState("");
        initialState.setTranslateX(-(NODE_RADIUS + NODE_STROKE_RADIUS + (0.5 * NODE_SEPARATION)));

        // Create the final state and move it into position.
        StackPane finalState = createState("");
        finalState.setTranslateX(NODE_RADIUS + NODE_STROKE_RADIUS + (0.5 * NODE_SEPARATION));

        // Create the transition.
        double lineWidth = Math.abs(Math.abs(initialState.getTranslateX()) + Math.abs(finalState.getTranslateX()));
        StackPane transition = createEdge("" + simpleRegularExpression.getSymbol(), lineWidth, 0, 0, true);

        // Create the simple finite automaton.
        double minWidth = (finalState.getTranslateX() - initialState.getTranslateX() + (2 * NODE_STROKE_RADIUS) + (2 * NODE_RADIUS));
        double minHeight = (2 * NODE_RADIUS) + (2 * NODE_STROKE_RADIUS);
        return new SimpleFiniteAutomaton(initialState, finalState, transition, minWidth, minHeight);
    }

    /**
     * Creates a labelled state.
     *
     * @param labelText The state label.
     * @return Labelled state.
     */
    private static StackPane createState(String labelText) {
        // Create the node
        Circle node = new Circle();
        node.setRadius(NODE_RADIUS);
        node.setFill(Color.WHITE);
        node.setStrokeWidth(2 * NODE_STROKE_RADIUS);
        node.setStroke(Color.BLACK);

        // Create the node label.
        Label nodeTitle = new Label(labelText);

        // Create the node container and add the node and the label.
        StackPane nodePane = new StackPane();
        nodePane.setMaxSize(2 * NODE_RADIUS, 2 * NODE_RADIUS);  // Prevents it from being resized past its size.
        nodePane.getChildren().addAll(node, nodeTitle);
        return nodePane;
    }

    /**
     * Creates an edge. The edge may be labelled and may be directed.
     *
     * @param labelText  The label on the edge.
     * @param lineWidth  The width of the edge.
     * @param lineHeight The height of the edge.
     * @param angle      The clockwise angle used to rotate the edge, in degrees.
     * @param directed   Whether to include an arrowhead.
     * @return The edge.
     */
    private static StackPane createEdge(String labelText, double lineWidth, double lineHeight, double angle, boolean directed) {
        // Get the length of the line.
        double lineLength = Math.sqrt(Math.pow(lineWidth, 2) + Math.pow(lineHeight, 2));

        // Create the line.
        Line line = new Line(0, 0, lineLength, 0);
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(Color.BLACK);

        // Create the arrowhead and move it to the end of the line.
        Polygon arrowhead = null;
        if (directed) {
            arrowhead = new Polygon(0, ARROWHEAD_SIZE, 0, -ARROWHEAD_SIZE, ARROWHEAD_SIZE, 0);
            arrowhead.setTranslateX(lineLength - (ARROWHEAD_SIZE + NODE_RADIUS));
        }

        // Group together the line (and arrowhead) and rotate the group, pivoted at its centre.
        Group edgeGroup = new Group();
        edgeGroup.getChildren().add(line);
        if (directed) {
            edgeGroup.getChildren().add(arrowhead);
        }
        edgeGroup.getTransforms().add(new Rotate(angle, 0.5 * lineLength, 0));

        // Create the label and move it above the edge.
        Label label = new Label(labelText);
        label.setTranslateY(-label.getFont().getSize());

        // Create the edge container and add the edge and label.
        StackPane edgePane = new StackPane();
        edgePane.setMaxSize(lineWidth, lineHeight);  // Prevents it being resized past its size.
        edgePane.getChildren().addAll(edgeGroup, label);

        return edgePane;
    }

    /**
     * Sets the given state as initial. The state must already belong to a finite automaton, so that the min width of
     * the finite automaton can be adjusted accordingly.
     *
     * @param state The state to set as initial.
     */
    public static void setAsInitial(StackPane state) {
        Line line = new Line(0, 0, INITIAL_EDGE_LENGTH, 0);
        line.setId("line");
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(Color.BLACK);
        line.setTranslateX(-(NODE_RADIUS + (INITIAL_EDGE_LENGTH / 2)));

        Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE, 0, -ARROWHEAD_SIZE, ARROWHEAD_SIZE, 0);
        arrowhead.setId("arrowhead");
        arrowhead.setTranslateX(-(NODE_RADIUS + (ARROWHEAD_SIZE / 2)));

        state.getChildren().addAll(line, arrowhead);
        Pane parentPane = (Pane) state.getParent();
        parentPane.setMinWidth(parentPane.getMinWidth() + (2 * INITIAL_EDGE_LENGTH));
    }

    /**
     * Sets the given state as non-initial. The state must already belong to a finite automaton, so that the min width
     * of the finite automaton can be adjusted accordingly.
     *
     * @param state The state to set as non-initial.
     */
    public static void setAsNonInitial(StackPane state) {
        Line line = (Line) state.lookup("#line");
        state.getChildren().remove(line);
        Polygon arrowhead = (Polygon) state.lookup("#arrowhead");
        state.getChildren().remove(arrowhead);
        Pane parentPane = (Pane) state.getParent();
        parentPane.setMinWidth(parentPane.getMinWidth() - (2 * INITIAL_EDGE_LENGTH));
    }

    /**
     * Sets the given state as final.
     *
     * @param state The state to set as final.
     */
    public static void setAsFinal(StackPane state) {
        Circle innerCircle = new Circle();
        innerCircle.setId("innerCircle");
        innerCircle.setRadius(NODE_RADIUS - 5);
        innerCircle.setFill(Color.TRANSPARENT);
        innerCircle.setStrokeWidth(NODE_STROKE_RADIUS);
        innerCircle.setStroke(Color.BLACK);
        state.getChildren().add(innerCircle);
    }

    /**
     * Sets the given state as non-final.
     *
     * @param state The state to set as non-final.
     */
    public static void setAsNonFinal(StackPane state) {
        Circle innerCircle = (Circle) state.lookup("#innerCircle");
        state.getChildren().remove(innerCircle);
    }

    /**
     * Generates text explaining how the finite automaton for the given regular expression is built from the finite
     * automata for its subexpressions.
     *
     * @param regularExpression The regular expression for which the explanation text is being generated for.
     * @return Text explaining how the finite automaton is created.
     */
    public static String getExplanationText(RegularExpression regularExpression) {
        if (regularExpression instanceof SimpleRegularExpression simpleRegularExpression) {
            return "The finite automaton for the regular expression \"" + simpleRegularExpression +
                    "\" is built by creating an initial state i and final state f with a labelled transition between " +
                    "them. The label of the transition is \"" + simpleRegularExpression.getSymbol() + "\".";
        }
        else if (regularExpression instanceof ComplexRegularExpression complexRegularExpression) {
            String explanation;
            RegexOperator regexOperator = complexRegularExpression.getOperator();

            if (regexOperator == RegexOperator.UNION) {
                RegularExpression leftOperand = complexRegularExpression.getLeftOperand();
                RegularExpression rightOperand = complexRegularExpression.getRightOperand();
                explanation = "The finite automaton for the regular expression \"" + complexRegularExpression +
                        "\" is built by combining the finite automata of its two subexpressions: \"" + leftOperand +
                        "\" and \"" + rightOperand + "\". A new initial state and a new final state are created. " +
                        "The new initial state is connected to the previous initial states by epsilon-transitions " +
                        "and the previous final states are connected to the new final state by epsilon-transitions.";
            }
            else if (regexOperator == RegexOperator.CONCATENATION) {
                RegularExpression leftOperand = complexRegularExpression.getLeftOperand();
                RegularExpression rightOperand = complexRegularExpression.getRightOperand();
                explanation = "The finite automaton for the regular expression \"" + complexRegularExpression +
                        "\" is built by combining the finite automata of its two subexpressions: \"" + leftOperand +
                        "\" and \"" + rightOperand + "\". The final state of the finite automaton for \"" + leftOperand +
                        "\" and the initial state of the finite automaton for \"" + rightOperand + "\" are merged together.";
            }
            else if (regexOperator == RegexOperator.STAR) {
                RegularExpression leftOperand = complexRegularExpression.getLeftOperand();
                explanation = "The finite automaton for the regular expression " + complexRegularExpression +
                        " is built by expanding the finite automaton of its subexpression \"" + leftOperand + "\"." +
                        " A new initial state and a new final state are created. The new initial state is " +
                        "connected to the previous initial state by an epsilon-transition and the previous final " +
                        "state is connected to the new final state by an epsilon transition. In addition, the " +
                        "previous final state is connected to the previous initial state by an epsilon-transition " +
                        "and the new initial state is connected to the new final state by an epsilon-transition.";
            }
            else {
                throw new IllegalArgumentException("Regex operator is not STAR, CONCATENATION or UNION!");
            }
            return explanation;
        }
        else {
            throw new IllegalArgumentException("Regular expression is neither Simple nor Complex!");
        }
    }

}
