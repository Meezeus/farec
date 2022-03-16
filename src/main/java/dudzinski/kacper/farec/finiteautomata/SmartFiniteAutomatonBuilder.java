package dudzinski.kacper.farec.finiteautomata;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.TextAlignment;

/**
 * This class contains methods for building and manipulating smart finite
 * automaton components
 */
public class SmartFiniteAutomatonBuilder {

    public static final double NODE_RADIUS = 20;
    public static final Color NODE_FILL = Color.WHITE;
    public static final double NODE_STROKE_RADIUS = 1;
    public static final Color NODE_STROKE_COLOR = Color.BLACK;

    public static final double INITIAL_STATE_LENGTH = 30;
    public static final double FINAL_STATE_RADIUS = NODE_RADIUS - 5;

    public static final double EDGE_STROKE_RADIUS = 1;
    public static final Color EDGE_STROKE_COLOR = Color.BLACK;
    public static final double CONTROL_POINT_OFFSET = 25;
    public static final double ARROWHEAD_SIZE = 10;
    public static final Color EDGE_LABEL_COLOR = Color.WHITESMOKE;

    public static final String EPSILON = "\u03B5";

    /**
     * Creates a smart state.
     *
     * @param labelText the text for the smart state's label
     * @return a smart state
     */
    public static SmartState createSmartState(String labelText) {
        // Create the circle.
        Circle circle = new Circle();
        circle.setRadius(NODE_RADIUS);
        circle.setFill(NODE_FILL);
        circle.setStrokeWidth(2 * NODE_STROKE_RADIUS);
        circle.setStroke(NODE_STROKE_COLOR);

        // Create the label.
        Label label = new Label(labelText);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.translateXProperty().bind(
                label.widthProperty().divide(2).multiply(-1));
        label.translateYProperty().bind
                (label.heightProperty().divide(2).multiply(-1));

        // Create the smart state.
        return new SmartState(circle, label);
    }

    /**
     * Sets the smart state as an initial state, by adding the initial state
     * marking. The marking consists of a short incoming edge that is not
     * connected to any other state.
     *
     * @param state the smart state to set as initial
     */
    public static void setAsInitial(SmartState state) {
        // Get the container.
        Group container = state.getContainer();

        // Create the line.
        Line line = new Line(0, 0, INITIAL_STATE_LENGTH, 0);
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(EDGE_STROKE_COLOR);
        line.setTranslateX(-(NODE_RADIUS + NODE_STROKE_RADIUS
                + INITIAL_STATE_LENGTH + EDGE_STROKE_RADIUS));
        line.setId("line");

        // Create the arrowhead.
        Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE,
                                        0, -ARROWHEAD_SIZE,
                                        ARROWHEAD_SIZE, 0);
        arrowhead.setTranslateX(-(NODE_RADIUS + ARROWHEAD_SIZE));
        arrowhead.setId("arrowhead");

        // Add the line and arrowhead to the back of the container.
        container.getChildren().add(0, line);
        container.getChildren().add(0, arrowhead);
    }

    /**
     * Sets the smart state as a non-initial state, by removing the initial
     * state marking. The marking consists of a short incoming edge that is not
     * connected to any other state.
     *
     * @param state the smart state to set as non-initial
     */
    public static void setAsNonInitial(SmartState state) {
        // Get the container.
        Group container = state.getContainer();

        // Remove the line.
        Line line = (Line) container.lookup("#line");
        container.getChildren().remove(line);

        // Remove the arrowhead.
        Polygon arrowhead = (Polygon) container.lookup("#arrowhead");
        container.getChildren().remove(arrowhead);
    }

    /**
     * Sets the smart state as a final state, by adding the final state marking.
     * The marking consists of a second, inner circle.
     *
     * @param state the smart state to set as final
     */
    public static void setAsFinal(SmartState state) {
        // Get the container.
        Group container = state.getContainer();

        // Create the circle.
        Circle innerCircle = new Circle();
        innerCircle.setRadius(FINAL_STATE_RADIUS);
        innerCircle.setFill(Color.TRANSPARENT);
        innerCircle.setStrokeType(StrokeType.OUTSIDE);
        innerCircle.setStrokeWidth(NODE_STROKE_RADIUS);
        innerCircle.setStroke(NODE_STROKE_COLOR);
        innerCircle.setId("innerCircle");

        // Add the circle to the container.
        container.getChildren().add(innerCircle);
    }

    /**
     * Sets the smart state as a non-final state, by removing the final state
     * marking. The marking consists of a second, inner circle.
     *
     * @param state the smart state to set as non-final
     */
    public static void setAsNonFinal(SmartState state) {
        // Get the container.
        Group container = state.getContainer();

        // Remove the inner circle
        Circle innerCircle = (Circle) container.lookup("#innerCircle");
        container.getChildren().remove(innerCircle);
    }

    /**
     * Creates a smart edge.
     *
     * @param labelText  the text for the edge's label
     * @param startState the start state of the edge
     * @param endState   the end state of the edge
     * @return a smart edge
     */
    public static SmartEdge createSmartEdge(String labelText,
                                            SmartState startState,
                                            SmartState endState) {
        // Create the line.
        Line line = new Line();
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(EDGE_STROKE_COLOR);

        // Bind the start of the line to the start state and the end of the
        // line to the end state.
        line.startXProperty().bind(
                startState.getContainer().translateXProperty());
        line.startYProperty().bind(
                startState.getContainer().translateYProperty());
        line.endXProperty().bind(
                endState.getContainer().translateXProperty());
        line.endYProperty().bind(
                endState.getContainer().translateYProperty());

        // Create the bindings for the translation needed to get from the start
        // state to the end state.
        DoubleBinding sToEX =
                line.endXProperty().subtract(line.startXProperty());
        DoubleBinding sToEY =
                line.endYProperty().subtract(line.startYProperty());

        // Create the binding for the arrowhead's x offset.
        DoubleBinding arrowheadXOffset = new DoubleBinding() {
            {
                super.bind(sToEX, sToEY);
            }

            @Override
            protected double computeValue() {
                double angleInRadians =
                        Math.atan(sToEY.getValue() / sToEX.getValue());
                double offset = (NODE_RADIUS + NODE_STROKE_RADIUS
                        + (0.5 * ARROWHEAD_SIZE))
                        * Math.abs(Math.cos(angleInRadians));
                if (sToEX.getValue() >= 0) {
                    return -offset;
                }
                else {
                    return offset;
                }
            }
        };

        // Create the binding for the arrowhead's y offset.
        DoubleBinding arrowheadYOffset = new DoubleBinding() {
            {
                super.bind(sToEX, sToEY);
            }

            @Override
            protected double computeValue() {
                double angle = Math.atan(sToEY.getValue() / sToEX.getValue());
                double offset = (NODE_RADIUS + NODE_STROKE_RADIUS
                        + (0.5 * ARROWHEAD_SIZE))
                        * Math.abs(Math.sin(angle));
                if (sToEY.getValue() >= 0) {
                    return -offset;
                }
                else {
                    return offset;
                }
            }
        };

        //  Create the binding for the arrowhead angle.
        DoubleBinding angleInDegrees = new DoubleBinding() {
            {
                super.bind(sToEX, sToEY);
            }

            @Override
            protected double computeValue() {
                double angleInDegrees = Math.toDegrees(
                        Math.atan(sToEY.getValue() / sToEX.getValue()));
                if (sToEX.getValue() >= 0) {
                    return angleInDegrees;
                }
                else {
                    return (180 + angleInDegrees);
                }
            }
        };

        // Create the arrowhead.
        Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE,
                                        0, -ARROWHEAD_SIZE,
                                        ARROWHEAD_SIZE, 0);
        arrowhead.setStrokeWidth(EDGE_STROKE_RADIUS);
        arrowhead.setStroke(EDGE_STROKE_COLOR);

        // Bind the arrowhead's position and angle so that the tip is always
        // touching the end state and aiming at its centre.
        arrowhead.translateXProperty().bind(
                line.endXProperty().
                    subtract(0.5 * ARROWHEAD_SIZE).
                    add(arrowheadXOffset));
        arrowhead.translateYProperty().bind(
                line.endYProperty().
                    add(arrowheadYOffset));
        arrowhead.rotateProperty().bind(angleInDegrees);

        // Create the label.
        Label label = new Label(labelText);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(0, 5, 0, 5));
        label.setBackground(
                new Background(
                        new BackgroundFill(EDGE_LABEL_COLOR, CornerRadii.EMPTY,
                                           Insets.EMPTY)));

        // Bind the label to the centre of the edge.
        label.translateXProperty().bind(
                line.startXProperty().add(line.endXProperty()).divide(2).
                    subtract(label.widthProperty().divide(2)));
        label.translateYProperty().bind(
                line.startYProperty().add(line.endYProperty()).divide(2).
                    subtract(label.heightProperty().divide(2)));

        // Create the edge.
        return new SmartEdge(line, arrowhead, label, startState, endState);
    }

    /**
     * Creates a smart curved edge.
     *
     * @param labelText  the text for the edge's label
     * @param startState the start state of the edge
     * @param endState   the end state of the edge
     * @return a smart curved edge
     */
    public static SmartEdge createSmartCurvedEdge(String labelText,
                                                  SmartState startState,
                                                  SmartState endState) {
        // Create the curve.
        QuadCurve curve = new QuadCurve();
        curve.setFill(Color.TRANSPARENT);
        curve.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        curve.setStroke(EDGE_STROKE_COLOR);

        // Bind the start of the curve to the start state and the end of the
        // curve to the end state.
        curve.startXProperty().bind(
                startState.getContainer().translateXProperty());
        curve.startYProperty().bind(
                startState.getContainer().translateYProperty());
        curve.endXProperty().bind(
                endState.getContainer().translateXProperty());
        curve.endYProperty().bind(
                endState.getContainer().translateYProperty());

        // Create the bindings for the translation needed to get from the start
        // state to the end state.
        DoubleBinding sToEX =
                curve.endXProperty().subtract(curve.startXProperty());
        DoubleBinding sToEY =
                curve.endYProperty().subtract(curve.startYProperty());

        // Create the binding for the controlX offset.
        DoubleBinding controlXOffset = new DoubleBinding() {
            {
                super.bind(sToEX, sToEY);
            }

            @Override
            protected double computeValue() {
                double angle = Math.atan(sToEY.getValue() / sToEX.getValue());
                double controlPointOffsetSin =
                        CONTROL_POINT_OFFSET * Math.abs(Math.sin(angle));
                if ((sToEY.getValue() >= 0) && (sToEX.getValue() >= 0)) {
                    return (sToEX.getValue() / 2) + controlPointOffsetSin;
                }
                else if ((sToEY.getValue() >= 0) && (sToEX.getValue() < 0)) {
                    return (sToEX.getValue() / 2) + controlPointOffsetSin;
                }
                else if ((sToEY.getValue() < 0) && (sToEX.getValue() < 0)) {
                    return (sToEX.getValue() / 2) - controlPointOffsetSin;
                }
                else if ((sToEY.getValue() < 0) && (sToEX.getValue() >= 0)) {
                    return (sToEX.getValue() / 2) - controlPointOffsetSin;
                }
                else {
                    throw new AssertionError(
                            "Something's wrong I can feel it.");
                }
            }
        };

        // Create the binding for controlY offset.
        DoubleBinding controlYOffset = new DoubleBinding() {
            {
                super.bind(sToEX, sToEY);
            }

            @Override
            protected double computeValue() {
                double angle = Math.atan(sToEY.getValue() / sToEX.getValue());
                double controlPointOffsetCos =
                        CONTROL_POINT_OFFSET * Math.abs(Math.cos(angle));
                if ((sToEY.getValue() >= 0) && (sToEX.getValue() >= 0)) {
                    return (sToEY.getValue() / 2) - controlPointOffsetCos;
                }
                else if ((sToEY.getValue() >= 0) && (sToEX.getValue() < 0)) {
                    return (sToEY.getValue() / 2) + controlPointOffsetCos;
                }
                else if ((sToEY.getValue() < 0) && (sToEX.getValue() < 0)) {
                    return (sToEY.getValue() / 2) + controlPointOffsetCos;
                }
                else if ((sToEY.getValue() < 0) && (sToEX.getValue() >= 0)) {
                    return (sToEY.getValue() / 2) - controlPointOffsetCos;
                }
                else {
                    throw new AssertionError(
                            "Something's wrong I can feel it.");
                }
            }
        };

        // Bind the curved line's controlX and controlY properties.
        curve.controlXProperty().bind(
                curve.startXProperty().add(controlXOffset));
        curve.controlYProperty().bind(
                curve.startYProperty().add(controlYOffset));

        // Create the bindings for the translation needed to get from the
        // control point to the end state.
        DoubleBinding cToEX =
                curve.endXProperty().subtract(curve.controlXProperty());
        DoubleBinding cToEY =
                curve.endYProperty().subtract(curve.controlYProperty());

        // Create the binding for the arrowhead's x offset.
        DoubleBinding arrowheadXOffset = new DoubleBinding() {
            {
                super.bind(cToEX, cToEY);
            }

            @Override
            protected double computeValue() {
                double angleInRadians =
                        Math.atan(cToEY.getValue() / cToEX.getValue());
                double offset = (NODE_RADIUS + NODE_STROKE_RADIUS
                        + (0.5 * ARROWHEAD_SIZE))
                        * Math.abs(Math.cos(angleInRadians));
                if (cToEX.getValue() >= 0) {
                    return -offset;
                }
                else {
                    return offset;
                }
            }
        };

        // Create the binding for the arrowhead's y offset.
        DoubleBinding arrowheadYOffset = new DoubleBinding() {
            {
                super.bind(cToEX, cToEY);
            }

            @Override
            protected double computeValue() {
                double angleInRadians =
                        Math.atan(cToEY.getValue() / cToEX.getValue());
                double offset = (NODE_RADIUS + NODE_STROKE_RADIUS
                        + (0.5 * ARROWHEAD_SIZE))
                        * Math.abs(Math.sin(angleInRadians));
                if (cToEY.getValue() >= 0) {
                    return -offset;
                }
                else {
                    return offset;
                }
            }
        };

        //  Create the binding for the arrowhead angle.
        DoubleBinding angleInDegrees = new DoubleBinding() {
            {
                super.bind(cToEX, cToEY);
            }

            @Override
            protected double computeValue() {
                double angleInDegrees = Math.toDegrees(
                        Math.atan(cToEY.getValue() / cToEX.getValue()));
                if (cToEX.getValue() >= 0) {
                    return angleInDegrees;
                }
                else {
                    return (180 + angleInDegrees);
                }
            }
        };

        // Create the arrowhead.
        Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE,
                                        0, -ARROWHEAD_SIZE,
                                        ARROWHEAD_SIZE, 0);
        arrowhead.setStrokeWidth(EDGE_STROKE_RADIUS);
        arrowhead.setStroke(EDGE_STROKE_COLOR);

        // Bind the arrowhead's position and angle so that the tip is always
        // touching the end state and aiming at its centre.
        arrowhead.translateXProperty().bind(
                curve.endXProperty().
                     subtract(ARROWHEAD_SIZE / 2).
                     add(arrowheadXOffset));
        arrowhead.translateYProperty().bind(
                curve.endYProperty().
                     add(arrowheadYOffset));
        arrowhead.rotateProperty().bind(angleInDegrees);

        // Create the label.
        Label label = new Label(labelText);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(0, 5, 0, 5));
        label.setBackground(
                new Background(
                        new BackgroundFill(EDGE_LABEL_COLOR, CornerRadii.EMPTY,
                                           Insets.EMPTY)));

        // Bind the label to the control point.
        label.translateXProperty().bind(
                curve.controlXProperty().
                     subtract(label.widthProperty().divide(2)));
        label.translateYProperty().bind(
                curve.controlYProperty().
                     subtract(label.heightProperty().divide(2)));

        // Create the edge.
        return new SmartEdge(curve, arrowhead, label, startState, endState);
    }

    /**
     * Creates a smart loop edge.
     *
     * @param labelText the text for the edge's label
     * @param state     the state the edge is connected to
     * @return a smart loop edge
     */
    public static SmartLoopEdge createSmartLoopEdge(String labelText,
                                                    SmartState state) {
        // Create the curve.
        CubicCurve curve = new CubicCurve();
        curve.setFill(Color.TRANSPARENT);
        curve.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        curve.setStroke(EDGE_STROKE_COLOR);

        // Create the arrowhead.
        Polygon arrowhead = new Polygon(0, ARROWHEAD_SIZE,
                                        0, -ARROWHEAD_SIZE,
                                        ARROWHEAD_SIZE, 0);
        arrowhead.setStrokeWidth(EDGE_STROKE_RADIUS);
        arrowhead.setStroke(EDGE_STROKE_COLOR);

        // Create the label.
        Label label = new Label(labelText);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(0, 5, 0, 5));
        label.setBackground(
                new Background(
                        new BackgroundFill(EDGE_LABEL_COLOR, CornerRadii.EMPTY,
                                           Insets.EMPTY)));

        // Set the bindings
        setSmartLoopEdgeBindings(curve, arrowhead, label, state, true);

        // Create the edge.
        return new SmartLoopEdge(curve, arrowhead, label, state);
    }

    /**
     * Sets the bindings for the components of a smart loop edge. This includes
     * binding the start, end and control points of the curve, the position and
     * angle of the arrowhead and the position of the label. These bindings may
     * be above the state or below the state depending on the value of the
     * <code>topside</code> parameter.
     *
     * @param curve     the curve of the loop edge
     * @param arrowhead the arrowhead at the end of the loop edge
     * @param label     the label on the loop edge
     * @param state     the state the loop edge is connected to
     * @param topside   whether to bind the components above or below the state
     *                  (true for above, false for below)
     */
    public static void setSmartLoopEdgeBindings(CubicCurve curve,
                                                Polygon arrowhead,
                                                Label label,
                                                SmartState state,
                                                boolean topside) {
        // Create the side multiplier.
        final int SIDE_MULTIPLIER = topside ? 1 : -1;

        // Bind the start of the curve to the left side of the state and the
        // end of the curve to the right side of the state.
        curve.startXProperty().bind(
                state.getContainer().translateXProperty().
                     subtract(NODE_RADIUS));
        curve.startYProperty().bind(
                state.getContainer().translateYProperty());
        curve.endXProperty().bind(
                state.getContainer().translateXProperty().
                     add(NODE_RADIUS));
        curve.endYProperty().bind(
                state.getContainer().translateYProperty());

        // Bind the curved line's controlX and controlY properties.
        curve.controlX1Property().bind(
                curve.startXProperty().subtract(1 * NODE_RADIUS));
        curve.controlY1Property().bind(
                curve.startYProperty().
                     subtract(2 * SIDE_MULTIPLIER * CONTROL_POINT_OFFSET));
        curve.controlX2Property().bind(
                curve.endXProperty().add(1 * NODE_RADIUS));
        curve.controlY2Property().bind(
                curve.endYProperty().
                     subtract(2 * SIDE_MULTIPLIER * CONTROL_POINT_OFFSET));

        // Create the bindings for the translation needed to get from the
        // control point to the end of the curve.
        DoubleBinding cToEX =
                curve.endXProperty().subtract(curve.controlX2Property());
        DoubleBinding cToEY =
                curve.endYProperty().subtract(curve.controlY2Property());

        // Create the binding for the arrowhead's x offset.
        DoubleBinding arrowheadXOffset = new DoubleBinding() {
            {
                super.bind(cToEX, cToEY);
            }

            @Override
            protected double computeValue() {
                double angleInRadians =
                        Math.atan(cToEY.getValue() / cToEX.getValue());
                double offset = (0.5 * ARROWHEAD_SIZE
                        * Math.abs(Math.cos(angleInRadians)));
                if (cToEX.getValue() >= 0) {
                    return -offset;
                }
                else {
                    return offset;
                }
            }
        };

        // Create the binding for the arrowhead's Y offset.
        DoubleBinding arrowheadYOffset = new DoubleBinding() {
            {
                super.bind(cToEX, cToEY);
            }

            @Override
            protected double computeValue() {
                double angleInRadians =
                        Math.atan(cToEY.getValue() / cToEX.getValue());
                double offset = (0.5 * ARROWHEAD_SIZE
                        * Math.abs(Math.sin(angleInRadians)));
                if (cToEY.getValue() >= 0) {
                    return -offset;
                }
                else {
                    return offset;
                }
            }
        };

        //  Create the binding for the angle.
        DoubleBinding angleInDegrees = new DoubleBinding() {
            {
                super.bind(cToEX, cToEY);
            }

            @Override
            protected double computeValue() {
                double angleInDegrees = Math.toDegrees(
                        Math.atan(cToEY.getValue() / cToEX.getValue()));
                if (cToEX.getValue() >= 0) {
                    return angleInDegrees;
                }
                else {
                    return (180 + angleInDegrees);
                }
            }
        };

        // Bind the arrowhead's position and angle so that the tip is always
        // touching the state and aiming at its centre.
        arrowhead.translateXProperty().bind(
                curve.endXProperty().
                     subtract(ARROWHEAD_SIZE / 2).
                     add(arrowheadXOffset));
        arrowhead.translateYProperty().bind(
                curve.endYProperty().
                     add(arrowheadYOffset));
        arrowhead.rotateProperty().bind(angleInDegrees);

        // Bind the label to the middle of the curve.
        label.translateXProperty().bind(
                state.getContainer().translateXProperty().
                     subtract(label.widthProperty().divide(2)));
        label.translateYProperty().bind(
                state.getContainer().translateYProperty().
                     subtract(label.heightProperty().divide(2).
                                   add(2 * SIDE_MULTIPLIER *
                                               CONTROL_POINT_OFFSET)));
    }

}
