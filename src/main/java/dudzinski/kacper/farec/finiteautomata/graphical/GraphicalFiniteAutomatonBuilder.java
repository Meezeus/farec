package dudzinski.kacper.farec.finiteautomata.graphical;

import dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings;
import dudzinski.kacper.farec.finiteautomata.smart.SmartFiniteAutomatonBuilder;
import dudzinski.kacper.farec.regex.*;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

import static dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings.*;

/**
 * This class contains methods for building and manipulating graphical finite
 * automata and their components.
 *
 * @see FiniteAutomatonSettings
 * @see SmartFiniteAutomatonBuilder
 */
public final class GraphicalFiniteAutomatonBuilder {

    /**
     * Objects of this class should never be created.
     */
    private GraphicalFiniteAutomatonBuilder() {
        throw new RuntimeException(
                "The GraphicalFiniteAutomatonBuilder class should never be" +
                        " instantiated!");
    }

    /**
     * Builds a finite automaton for the given regular expression.
     *
     * @param regularExpression the regular expression for which to build a
     *                          finite automaton
     * @return a finite automaton for the given regular expression
     */
    public static GraphicalFiniteAutomaton
    buildFiniteAutomaton(
            RegularExpression regularExpression) {
        // Base case: the regular expression is a simple regular expression.
        if (regularExpression instanceof SimpleRegularExpression) {
            // Build the corresponding simple graphical finite automaton.
            return buildSimpleFiniteAutomaton(
                    (SimpleRegularExpression) regularExpression);
        }

        // Recursive case: the regular expression is a complex regular
        // expression.
        else if (regularExpression instanceof
                ComplexRegularExpression complexRegularExpression) {
            // Get the regex operator of the regular expression.
            RegexOperator regexOperator =
                    complexRegularExpression.getOperator();

            // Get the operands.
            RegularExpression leftOperand =
                    complexRegularExpression.getLeftOperand();
            RegularExpression rightOperand =
                    complexRegularExpression.getRightOperand();

            // Use the construction rule of the STAR operator.
            if (regexOperator == RegexOperator.STAR) {
                // Build the finite automaton of the operand.
                GraphicalFiniteAutomaton finiteAutomaton1 =
                        buildFiniteAutomaton(leftOperand);

                // Extend the finite automaton of the operand to create the
                // complex finite automaton.
                return buildComplexFiniteAutomatonStar(finiteAutomaton1);
            }

            // Use the construction rule of the CONCATENATION operator.
            else if (regexOperator == RegexOperator.CONCATENATION) {
                // Build the finite automata of the operands.
                GraphicalFiniteAutomaton finiteAutomaton1 =
                        buildFiniteAutomaton(leftOperand);
                GraphicalFiniteAutomaton finiteAutomaton2 =
                        buildFiniteAutomaton(rightOperand);

                // Combine the finite automata of the operands to create the
                // complex finite automaton.
                return buildComplexFiniteAutomatonConcatenation(
                        finiteAutomaton1, finiteAutomaton2);
            }

            // Use the construction rule of the UNION operator.
            else if (regexOperator == RegexOperator.UNION) {
                // Build the finite automata of the operands.
                GraphicalFiniteAutomaton finiteAutomaton1 =
                        buildFiniteAutomaton(leftOperand);
                GraphicalFiniteAutomaton finiteAutomaton2 =
                        buildFiniteAutomaton(rightOperand);

                // Combine the finite automata of the operands to create the
                // complex finite automaton.
                return buildComplexFiniteAutomatonUnion(
                        finiteAutomaton1, finiteAutomaton2);
            }
            else {
                throw new IllegalArgumentException(
                        "Regex operator is not STAR, CONCATENATION or UNION!");
            }
        }
        else {
            throw new IllegalArgumentException(
                    "Regular expression is neither simple nor complex!");
        }
    }

    /**
     * Sets the given state as an initial state, by adding the initial state
     * marking. The marking consists of a short incoming edge that is not
     * connected to any other state. The state must already belong to a finite
     * automaton, so that the minimum width of the finite automaton can be
     * adjusted accordingly.
     *
     * @param state the state to set as initial
     */
    public static void setAsInitial(GraphicalState state) {
        // Get the container.
        StackPane stateContainer = state.getContainer();

        // Create the line.
        Line line = new Line(0, 0, INITIAL_STATE_EDGE_LENGTH, 0);
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(EDGE_STROKE_COLOR);
        line.setTranslateX(-(STATE_RADIUS + STATE_STROKE_RADIUS
                + (INITIAL_STATE_EDGE_LENGTH / 2)));
        line.setId("line");

        // Create the arrowhead.
        Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE,
                                        0, -ARROWHEAD_SIZE,
                                        ARROWHEAD_SIZE, 0);
        arrowhead.setTranslateX(-(STATE_RADIUS + (ARROWHEAD_SIZE / 2)));
        arrowhead.setId("arrowhead");

        // Add the line and arrowhead to the back of the container.
        stateContainer.getChildren().add(0, line);
        stateContainer.getChildren().add(0, arrowhead);

        // Adjust the minimum width of the finite automaton.
        Pane parentPane = (Pane) stateContainer.getParent();
        parentPane.setMinWidth(
                parentPane.getMinWidth() + (2 * (
                        INITIAL_STATE_EDGE_LENGTH
                                + EDGE_STROKE_RADIUS)));
    }

    /**
     * Sets the given state as a non-initial state, by removing the initial
     * state marking. The marking consists of a short incoming edge that is not
     * connected to any other state. The state must already belong to a finite
     * automaton, so that the minimum width of the finite automaton can be
     * adjusted accordingly.
     *
     * @param state the state to set as non-initial
     */
    public static void setAsNonInitial(GraphicalState state) {
        // Get the container.
        StackPane container = state.getContainer();

        // Remove the line.
        Line line = (Line) container.lookup("#line");
        container.getChildren().remove(line);

        // Remove the arrowhead.
        Polygon arrowhead = (Polygon) container.lookup("#arrowhead");
        container.getChildren().remove(arrowhead);

        // Adjust the minimum width of the finite automaton.
        Pane parentPane = (Pane) container.getParent();
        parentPane.setMinWidth
                          (parentPane.getMinWidth() - (2 * (
                                  INITIAL_STATE_EDGE_LENGTH
                                          + EDGE_STROKE_RADIUS)));
    }

    /**
     * Sets the given state as a final state, by adding the final state marking.
     * The marking consists of a second, inner circle.
     *
     * @param state the state to set as final
     */
    public static void setAsFinal(GraphicalState state) {
        // Get the container.
        StackPane container = state.getContainer();

        // Create the circle.
        Circle innerCircle = new Circle();
        innerCircle.setRadius(FINAL_STATE_CIRCLE_RADIUS);
        innerCircle.setFill(Color.TRANSPARENT);
        innerCircle.setStrokeType(StrokeType.OUTSIDE);
        innerCircle.setStrokeWidth(STATE_STROKE_RADIUS);
        innerCircle.setStroke(STATE_STROKE_COLOR);
        innerCircle.setId("innerCircle");

        // Add the circle to the container.
        container.getChildren().add(innerCircle);
    }

    /**
     * Sets the given state as a non-final state, by removing the final state
     * marking. The marking consists of a second, inner circle.
     *
     * @param state the state to set as non-final
     */
    public static void setAsNonFinal(GraphicalState state) {
        // Get the container.
        StackPane container = state.getContainer();

        // Remove the inner circle
        Circle innerCircle = (Circle) container.lookup("#innerCircle");
        container.getChildren().remove(innerCircle);
    }

    /**
     * Generates text explaining how the finite automaton for the given regular
     * expression is constructed from the finite automata of its
     * subexpressions.
     *
     * @param regularExpression the regular expression for which the explanation
     *                          text is being generated for
     * @return text explaining how the finite automaton is constructed
     */
    public static String getExplanationText(
            RegularExpression regularExpression) {
        if (regularExpression instanceof
                SimpleRegularExpression simpleRegularExpression) {
            return "The finite automaton for the regular expression" +
                    " \"" + simpleRegularExpression + "\" is built by" +
                    " creating an initial state and final state with a" +
                    " labelled edge between them. The label of the edge is" +
                    " \"" + simpleRegularExpression.getSymbol() + "\".";
        }
        else if (regularExpression instanceof
                ComplexRegularExpression complexRegularExpression) {
            String explanation;
            RegexOperator regexOperator =
                    complexRegularExpression.getOperator();

            if (regexOperator == RegexOperator.UNION) {
                String regexString = Parser.simplifyRegexString(
                        complexRegularExpression.toString());
                String leftOperand = Parser.simplifyRegexString(
                        complexRegularExpression.getLeftOperand().toString());
                String rightOperand = Parser.simplifyRegexString(
                        complexRegularExpression.getRightOperand().toString());

                explanation = "The finite automaton for the regular" +
                        " expression \"" + regexString + "\" is built by" +
                        " combining the finite automata of its two" +
                        " subexpressions: \"" + leftOperand + "\" and" +
                        " \"" + rightOperand + "\". A new initial state and" +
                        " a new final state are created. The new initial" +
                        " state is connected to the previous initial states" +
                        " by empty string transitions and the previous final" +
                        " states are connected to the new final state by" +
                        " empty string transitions.";
            }
            else if (regexOperator == RegexOperator.CONCATENATION) {
                String regexString = Parser.simplifyRegexString(
                        complexRegularExpression.toString());
                String leftOperand = Parser.simplifyRegexString(
                        complexRegularExpression.getLeftOperand().toString());
                String rightOperand = Parser.simplifyRegexString(
                        complexRegularExpression.getRightOperand().toString());

                explanation = "The finite automaton for the regular" +
                        " expression \"" + regexString + "\" is built by" +
                        " combining the finite automata of its two" +
                        " subexpressions: \"" + leftOperand + "\" and" +
                        " \"" + rightOperand + "\". The final state of the" +
                        " finite automaton for \"" + leftOperand + "\"" +
                        " and the initial state of the finite automaton" +
                        " for \"" + rightOperand + "\" are merged together.";
            }
            else if (regexOperator == RegexOperator.STAR) {
                String regexString = Parser.simplifyRegexString(
                        complexRegularExpression.toString());
                String leftOperand = Parser.simplifyRegexString(
                        complexRegularExpression.getLeftOperand().toString());

                explanation = "The finite automaton for the regular" +
                        " expression \"" + regexString + "\" is built by" +
                        " expanding the finite automaton of its" +
                        " subexpression \"" + leftOperand + "\". A new" +
                        " initial state and a new final state are created." +
                        " The new initial state is connected to the previous" +
                        " initial state by an empty string transition and the" +
                        " previous final state is connected to the new final" +
                        " state by an empty string transition. In addition," +
                        " the previous final state is connected to the" +
                        " previous initial state by an empty string" +
                        " transition and the new initial state is connected" +
                        " to the new final state by an empty string" +
                        " transition.";
            }
            else {
                throw new IllegalArgumentException(
                        "Regex operator is not STAR, CONCATENATION or UNION!");
            }
            return explanation;
        }
        else {
            throw new IllegalArgumentException(
                    "Regular expression is neither Simple nor Complex!");
        }
    }

    /**
     * Creates a state.
     *
     * @return a state
     */
    private static GraphicalState createState() {
        // Create the circle.
        Circle circle = new Circle();
        circle.setRadius(STATE_RADIUS);
        circle.setFill(STATE_FILL_COLOR);
        circle.setStrokeWidth(2 * STATE_STROKE_RADIUS);
        circle.setStroke(STATE_STROKE_COLOR);

        // Create the state.
        return new GraphicalState(circle);
    }

    /**
     * Creates an edge. The edge may be directed or undirected.
     *
     * @param labelText  the label on the edge
     * @param lineWidth  the width of the edge
     * @param lineHeight the height of the edge
     * @param angle      the angle to rotate the edge by, in degrees
     * @param directed   whether to include an arrowhead
     * @return an edge
     */
    private static GraphicalEdge createEdge(String labelText,
                                            double lineWidth,
                                            double lineHeight,
                                            double angle,
                                            boolean directed) {
        // Get the length of the line.
        double lineLength = Math.sqrt(Math.pow(lineWidth, 2)
                                              + Math.pow(lineHeight, 2));

        // Create the line.
        Line line = new Line(0, 0, lineLength, 0);
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(EDGE_STROKE_COLOR);

        // Create the group to hold the line (and arrowhead).
        Group edgeGroup = new Group();
        edgeGroup.getChildren().add(line);
        if (directed) {
            // Create the arrowhead and move it to the end of the line.
            Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE,
                                            0, -ARROWHEAD_SIZE,
                                            ARROWHEAD_SIZE, 0);
            arrowhead.setTranslateX(lineLength
                                            - (ARROWHEAD_SIZE + STATE_RADIUS));
            edgeGroup.getChildren().add(arrowhead);
        }

        // Rotate the group, pivoted at its centre.
        edgeGroup.getTransforms().add(new Rotate(angle, 0.5 * lineLength, 0));

        // Create the label and move it above the edge.
        Label label = new Label(labelText);
        label.setTranslateY(-label.getFont().getSize());

        // Create the edge.
        return new GraphicalEdge(edgeGroup, label);
    }

    /**
     * Builds a simple finite automaton for the given simple regular
     * expression.
     *
     * @param simpleRegularExpression the simple regular expression for which to
     *                                build a simple finite automaton
     * @return a simple finite automaton for the given simple regular
     * expression.
     */
    private static SimpleGraphicalFiniteAutomaton
    buildSimpleFiniteAutomaton(
            SimpleRegularExpression simpleRegularExpression) {
        // Create the initial state and move it into position.
        GraphicalState initialState = createState();
        StackPane initialStateContainer = initialState.getContainer();
        initialStateContainer.setTranslateX(
                -(STATE_RADIUS + STATE_STROKE_RADIUS
                        + (0.5 * STATE_SEPARATION)));

        // Create the final state and move it into position.
        GraphicalState finalState = createState();
        StackPane finalStateContainer = finalState.getContainer();
        finalStateContainer.setTranslateX(
                STATE_RADIUS + STATE_STROKE_RADIUS
                        + (0.5 * STATE_SEPARATION));

        // Create the edge.
        double lineWidth = Math.abs(
                Math.abs(initialStateContainer.getTranslateX())
                        + Math.abs(finalStateContainer.getTranslateX()));
        String edgeLabel = String.valueOf(simpleRegularExpression.getSymbol());
        GraphicalEdge edge = createEdge(edgeLabel, lineWidth, 0, 0, true);

        // Create the simple finite automaton.
        double minWidth = finalStateContainer.getTranslateX()
                - initialStateContainer.getTranslateX()
                + (2 * STATE_STROKE_RADIUS) + (2 * STATE_RADIUS);
        double minHeight = (2 * STATE_RADIUS) + (2 * STATE_STROKE_RADIUS);
        return new SimpleGraphicalFiniteAutomaton(initialState, finalState,
                                                  edge, minWidth, minHeight);
    }

    /**
     * Builds a complex finite automaton by extending the given finite automaton
     * according to the STAR construction rule.
     *
     * @param finiteAutomaton the finite automaton to be extended
     * @return a complex finite automaton
     */
    private static ComplexGraphicalFiniteAutomaton
    buildComplexFiniteAutomatonStar(
            GraphicalFiniteAutomaton finiteAutomaton) {
        // Disable the initial state and final state of the finite automaton.
        finiteAutomaton.enableInitialState(false);
        finiteAutomaton.enableFinalState(false);

        // Get the finite automaton container.
        StackPane finiteAutomatonContainer = finiteAutomaton.getContainer();

        // Get the offsets of the old initial and final states.
        double oldInitialStateOffset =
                finiteAutomaton.initialState.getContainer().getTranslateX();
        double oldFinalStateOffset =
                finiteAutomaton.finalState.getContainer().getTranslateX();

        // Calculate the offset needed to move a state from the position of
        // some other state to its new position, in the positive direction.
        double newStatePositiveOffset = (2 * STATE_RADIUS)
                + (2 * STATE_STROKE_RADIUS)
                + STATE_SEPARATION;

        // Create the new initial state and move it into position.
        GraphicalState newInitialState = createState();
        StackPane newInitialStateContainer = newInitialState.getContainer();
        newInitialStateContainer.setTranslateX(
                oldInitialStateOffset - newStatePositiveOffset);

        // Create the new final state and move it into position.
        GraphicalState newFinalState = createState();
        StackPane newFinalStateContainer = newFinalState.getContainer();
        newFinalStateContainer.setTranslateX(
                oldFinalStateOffset + newStatePositiveOffset);

        // Create a list to hold the new edges and create the edge variables.
        ArrayList<GraphicalEdge> edges = new ArrayList<>();
        double lineWidth;
        double lineHeight;

        // Calculate line width and height for i to i and f to f edges.
        lineWidth = newStatePositiveOffset;
        lineHeight = 0;

        // Connect the new initial state to the old initial state.
        GraphicalEdge iToI =
                createEdge(EMPTY_STRING, lineWidth, lineHeight, 0, true);
        StackPane iToIContainer = iToI.getContainer();
        iToIContainer.setTranslateX(
                newInitialStateContainer.getTranslateX() + (0.5 * lineWidth));
        edges.add(iToI);

        // Connect the old final state to the new final state.
        GraphicalEdge fToF =
                createEdge(EMPTY_STRING, lineWidth, lineHeight, 0, true);
        StackPane fToFContainer = fToF.getContainer();
        fToFContainer.setTranslateX(
                newFinalStateContainer.getTranslateX() - (0.5 * lineWidth));
        edges.add(fToF);

        // Calculate the vertical line height.
        double verticalLineHeight =
                (0.5 * finiteAutomatonContainer.getMinHeight())
                        + (2 * STATE_RADIUS);  // Extra node radius for spacing.

        // Calculate line width and height for the f to i transition.
        lineWidth = Math.abs(oldInitialStateOffset)
                + Math.abs(oldFinalStateOffset);
        lineHeight = verticalLineHeight;

        // Connect the old final state to the old initial state.
        GraphicalEdge fToIUp = createEdge("", 0, lineHeight, -90, false);
        StackPane fToIUpContainer = fToIUp.getContainer();
        fToIUpContainer.setTranslateX(oldFinalStateOffset);
        fToIUpContainer.setTranslateY(-(0.5 * lineHeight));
        edges.add(fToIUp);

        GraphicalEdge fToILeft = createEdge(EMPTY_STRING, lineWidth, 0, 180, false);
        StackPane fToILeftContainer = fToILeft.getContainer();
        fToILeftContainer.setTranslateY(-lineHeight);
        edges.add(fToILeft);

        GraphicalEdge fToIDown = createEdge("", 0, lineHeight, 90, true);
        StackPane fToIDownContainer = fToIDown.getContainer();
        fToIDownContainer.setTranslateX(oldInitialStateOffset);
        fToIDownContainer.setTranslateY(-(0.5 * lineHeight));
        edges.add(fToIDown);

        // Calculate line width and height for i to f transition.
        lineWidth = Math.abs(newInitialStateContainer.getTranslateX())
                + Math.abs(newFinalStateContainer.getTranslateX());
        lineHeight = verticalLineHeight;

        // Connect the new initial state to the new final state.
        GraphicalEdge iToFDown = createEdge("", 0, lineHeight, 90, false);
        StackPane iToFDownContainer = iToFDown.getContainer();
        iToFDownContainer.setTranslateX(
                newInitialStateContainer.getTranslateX());
        iToFDownContainer.setTranslateY(0.5 * lineHeight);
        edges.add(iToFDown);

        GraphicalEdge iToFRight = createEdge(EMPTY_STRING, lineWidth, 0, 0, false);
        StackPane iToFRightContainer = iToFRight.getContainer();
        iToFRightContainer.setTranslateY(lineHeight);
        edges.add(iToFRight);

        GraphicalEdge iToFUp = createEdge("", 0, lineHeight, -90, true);
        StackPane iToFUpContainer = iToFUp.getContainer();
        iToFUpContainer.setTranslateX(
                newFinalStateContainer.getTranslateX());
        iToFUpContainer.setTranslateY(0.5 * lineHeight);
        edges.add(iToFUp);

        // Create the new complex finite automaton.
        double minWidth = Math.abs(newFinalStateContainer.getTranslateX())
                + Math.abs(newInitialStateContainer.getTranslateX())
                + (2 * STATE_STROKE_RADIUS) + (2 * STATE_RADIUS);
        double minHeight = 2 * verticalLineHeight
                + 50;   // Extra 50 for labels
        return new ComplexGraphicalFiniteAutomaton(newInitialState,
                                                   newFinalState,
                                                   finiteAutomatonContainer,
                                                   edges,
                                                   null,
                                                   minWidth, minHeight);
    }

    /**
     * Builds a complex finite automaton by combining the given finite automata
     * according to the CONCATENATION construction rule.
     *
     * @param leftFiniteAutomaton  the finite automaton on the left
     * @param rightFiniteAutomaton the finite automaton on the right
     * @return a complex finite automaton
     */
    private static ComplexGraphicalFiniteAutomaton
    buildComplexFiniteAutomatonConcatenation(
            GraphicalFiniteAutomaton leftFiniteAutomaton,
            GraphicalFiniteAutomaton rightFiniteAutomaton) {
        // Disable the initial state and final state of the left finite
        // automaton.
        leftFiniteAutomaton.enableInitialState(false);
        leftFiniteAutomaton.enableFinalState(false);

        // Get the left finite automaton container.
        StackPane leftFiniteAutomatonContainer =
                leftFiniteAutomaton.getContainer();

        // Set the initial state to the initial state of the left finite
        // automaton.
        GraphicalState initialState = leftFiniteAutomaton.initialState;
        StackPane initialStateContainer = initialState.getContainer();

        // Remove the initial state from the left finite automaton.
        leftFiniteAutomatonContainer.getChildren().remove(
                initialStateContainer);

        // Move the left finite automaton and the initial state to the left,
        // such that the final state of the left finite automaton is in the
        // centre. Note that the initial state is no longer connected to the
        // left finite automaton, and so needs to be moved separately.
        double leftOffset =
                (-(0.5 * leftFiniteAutomatonContainer.getMinWidth()))
                        + (STATE_RADIUS + STATE_STROKE_RADIUS);
        leftFiniteAutomatonContainer.setTranslateX(leftOffset);
        initialStateContainer.setTranslateX(
                leftFiniteAutomatonContainer.getTranslateX()
                        + initialStateContainer.getTranslateX());

        // Disable the initial and final states of the right finite automaton.
        rightFiniteAutomaton.enableInitialState(false);
        rightFiniteAutomaton.enableFinalState(false);

        // Get the right finite automaton container.
        StackPane rightFiniteAutomatonContainer =
                rightFiniteAutomaton.getContainer();

        // Set the final state to the final state of the right finite automaton.
        GraphicalState finalState = rightFiniteAutomaton.finalState;
        StackPane finalStateContainer = finalState.getContainer();

        // Remove the final state from the right finite automaton.
        rightFiniteAutomatonContainer.getChildren().remove(
                finalStateContainer);

        // Move the right finite automaton and the final state to the right,
        // such that the initial state of the right finite automaton is in the
        // centre. Note that the final state is no longer connected to the
        // right finite automaton, and so needs to be moved separately.
        double rightOffset =
                (0.5 * rightFiniteAutomatonContainer.getMinWidth())
                        - (STATE_RADIUS + STATE_STROKE_RADIUS);
        rightFiniteAutomatonContainer.setTranslateX(rightOffset);
        finalStateContainer.setTranslateX(
                rightFiniteAutomatonContainer.getTranslateX()
                        + finalStateContainer.getTranslateX());

        // Find the distance needed to center the finite automaton.
        double centerOffset = 0.5 *
                (rightFiniteAutomatonContainer.getMinWidth()
                        - leftFiniteAutomatonContainer.getMinWidth());

        // Move everything to the left by the offset (note offset may be
        // negative, moving things to the right).
        leftFiniteAutomatonContainer.setTranslateX(
                leftFiniteAutomatonContainer.getTranslateX() - centerOffset);
        initialStateContainer.setTranslateX(
                initialStateContainer.getTranslateX() - centerOffset);
        rightFiniteAutomatonContainer.setTranslateX(
                rightFiniteAutomatonContainer.getTranslateX() - centerOffset);
        finalStateContainer.setTranslateX(
                finalStateContainer.getTranslateX() - centerOffset);

        // Create the new complex finite automaton.
        double minWidth = leftFiniteAutomatonContainer.getMinWidth()
                + rightFiniteAutomatonContainer.getMinWidth()
                - ((2 * STATE_RADIUS) + (2 * STATE_STROKE_RADIUS));
        double minHeight = Math.max(
                leftFiniteAutomatonContainer.getMinHeight(),
                rightFiniteAutomatonContainer.getMinHeight());
        return new ComplexGraphicalFiniteAutomaton(initialState,
                                                   finalState,
                                                   leftFiniteAutomatonContainer,
                                                   new ArrayList<>(),
                                                   rightFiniteAutomatonContainer,
                                                   minWidth, minHeight);
    }

    /**
     * Builds a complex finite automaton by combining the given finite automata
     * according to the UNION construction rule.
     *
     * @param topFiniteAutomaton    the finite automaton on the top
     * @param bottomFiniteAutomaton the finite automaton on the bottom
     * @return a complex finite automaton
     */
    private static ComplexGraphicalFiniteAutomaton
    buildComplexFiniteAutomatonUnion(
            GraphicalFiniteAutomaton topFiniteAutomaton,
            GraphicalFiniteAutomaton bottomFiniteAutomaton) {
        // Disable the initial state and final state of the top finite
        // automaton.
        topFiniteAutomaton.enableInitialState(false);
        topFiniteAutomaton.enableFinalState(false);

        // Move the top finite automaton above the baseline.
        StackPane topFiniteAutomatonContainer =
                topFiniteAutomaton.getContainer();
        double topOffset = -(
                (0.5 * topFiniteAutomatonContainer.getMinHeight())
                        + STATE_RADIUS + STATE_STROKE_RADIUS);
        topFiniteAutomatonContainer.setTranslateY(topOffset);

        // Disable the initial state and final state of the bottom finite
        // automaton.
        bottomFiniteAutomaton.enableInitialState(false);
        bottomFiniteAutomaton.enableFinalState(false);

        // Move the bottom finite automaton below the baseline.
        StackPane bottomFiniteAutomatonContainer =
                bottomFiniteAutomaton.getContainer();
        double bottomOffset =
                (0.5 * bottomFiniteAutomatonContainer.getMinHeight())
                        + STATE_RADIUS + STATE_STROKE_RADIUS;
        bottomFiniteAutomatonContainer.setTranslateY(bottomOffset);

        // Find out which automaton is the widest.
        GraphicalFiniteAutomaton widestFiniteAutomaton =
                topFiniteAutomatonContainer.getMinWidth()
                        > bottomFiniteAutomatonContainer.getMinWidth()
                        ? topFiniteAutomaton : bottomFiniteAutomaton;

        // Get the offset of the initial state and final state of the widest
        // finite automaton.
        double widestInitialStateOffset =
                widestFiniteAutomaton.initialState
                        .getContainer().getTranslateX();
        double widestFinalStateOffset =
                widestFiniteAutomaton.finalState
                        .getContainer().getTranslateX();

        // Calculate the offset needed to move a state from the position of
        // some other state to its new position, in the positive direction.
        double newStatePositiveOffset = (2 * STATE_RADIUS)
                + (2 * STATE_STROKE_RADIUS)
                + STATE_SEPARATION;

        // Create the new initial state and move it into position.
        GraphicalState newInitialState = createState();
        StackPane newInitialStateContainer = newInitialState.getContainer();
        newInitialStateContainer.setTranslateX(
                widestInitialStateOffset - newStatePositiveOffset);

        // Create the new final state and move it into position.
        GraphicalState finalState = createState();
        StackPane finalStateContainer = finalState.getContainer();
        finalStateContainer.setTranslateX(
                widestFinalStateOffset + newStatePositiveOffset);

        // Create a list to hold the new edges and create the edge variables.
        ArrayList<GraphicalEdge> edges = new ArrayList<>();
        double lineWidth;
        double lineHeight;
        double angleInDegrees;

        // Calculate the width, height and angle of the new edges for the top
        // finite automaton.
        StackPane topInitialStateContainer =
                topFiniteAutomaton.initialState.getContainer();
        lineWidth = Math.abs(
                Math.abs(newInitialStateContainer
                                 .getTranslateX())
                        - Math.abs(topInitialStateContainer
                                           .getTranslateX()));
        lineHeight = Math.abs(
                Math.abs(newInitialStateContainer
                                 .getTranslateY())
                        - Math.abs(topFiniteAutomatonContainer
                                           .getTranslateY()));
        angleInDegrees = Math.toDegrees(Math.atan(lineHeight / lineWidth));

        // Create the edge from the new initial state to the old initial state
        // of the top finite automaton.
        GraphicalEdge iToI1 = createEdge(EMPTY_STRING,
                                         lineWidth,
                                         lineHeight,
                                         -angleInDegrees,
                                         true);
        StackPane iToI1Container = iToI1.getContainer();
        iToI1Container.setTranslateX(
                newInitialStateContainer.getTranslateX() + (0.5 * lineWidth));
        iToI1Container.setTranslateY(-(0.5 * lineHeight));
        edges.add(iToI1);

        // Create the edge from the old final state of the top finite automaton
        // to the new final state.
        GraphicalEdge f1ToF = createEdge(EMPTY_STRING,
                                         lineWidth,
                                         lineHeight,
                                         angleInDegrees,
                                         true);
        StackPane f1ToFContainer = f1ToF.getContainer();
        f1ToFContainer.setTranslateX(
                finalStateContainer.getTranslateX() - (0.5 * lineWidth));
        f1ToFContainer.setTranslateY(-(0.5 * lineHeight));
        edges.add(f1ToF);

        // Calculate the width, height and angle of the new edges for the bottom
        // finite automaton.
        StackPane bottomInitialStateContainer =
                bottomFiniteAutomaton.initialState.getContainer();
        lineWidth = Math.abs(
                Math.abs(newInitialStateContainer
                                 .getTranslateX())
                        - Math.abs(bottomInitialStateContainer
                                           .getTranslateX()));
        lineHeight = Math.abs(
                Math.abs(
                        newInitialStateContainer
                                .getTranslateY())
                        - Math.abs(bottomFiniteAutomatonContainer
                                           .getTranslateY()));
        angleInDegrees = Math.toDegrees(Math.atan(lineHeight / lineWidth));

        // Create the edge from the new initial state to the old initial state
        // of the bottom finite automaton.
        GraphicalEdge iToI2 = createEdge(EMPTY_STRING,
                                         lineWidth,
                                         lineHeight,
                                         angleInDegrees,
                                         true);
        StackPane iToI2Container = iToI2.getContainer();
        iToI2Container.setTranslateX(
                newInitialStateContainer.getTranslateX() + (0.5 * lineWidth));
        iToI2Container.setTranslateY(0.5 * lineHeight);
        edges.add(iToI2);

        // Create the edge from the old final state of the bottom finite
        // automaton to the new final state.
        GraphicalEdge f2ToF = createEdge(EMPTY_STRING,
                                         lineWidth,
                                         lineHeight,
                                         -angleInDegrees,
                                         true);
        StackPane f2ToFContainer = f2ToF.getContainer();
        f2ToFContainer.setTranslateX(
                finalStateContainer.getTranslateX() - (0.5 * lineWidth));
        f2ToFContainer.setTranslateY(0.5 * lineHeight);
        edges.add(f2ToF);

        // Create the new complex finite automaton.
        double tallestFiniteAutomatonHeight = Math.max(
                topFiniteAutomatonContainer.getMinHeight(),
                bottomFiniteAutomatonContainer.getMinHeight());
        double minWidth = (finalStateContainer.getTranslateX()
                - newInitialStateContainer.getTranslateX())
                + (2 * STATE_STROKE_RADIUS) + (2 * STATE_RADIUS);
        double minHeight = (2 * tallestFiniteAutomatonHeight)
                + (2 * STATE_RADIUS) + (2 * STATE_STROKE_RADIUS);
        return new ComplexGraphicalFiniteAutomaton(newInitialState,
                                                   finalState,
                                                   topFiniteAutomatonContainer,
                                                   edges,
                                                   bottomFiniteAutomatonContainer,
                                                   minWidth, minHeight);
    }

}
