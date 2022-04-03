package dudzinski.kacper.farec.finiteautomata;

import dudzinski.kacper.farec.Settings;
import javafx.scene.paint.Color;

/**
 * This abstract class contains settings related to finite automata.
 */
public abstract class FiniteAutomatonSettings extends Settings {

    ////////////
    // States //
    ////////////

    // The radius of a finite automaton state.
    public static final double STATE_RADIUS = 20;
    // The color of the fill of a finite automaton state.
    public static final Color STATE_FILL_COLOR = Color.WHITE;
    // The radius of the stroke of a finite automaton state.
    public static final double STATE_STROKE_RADIUS = 1;
    // The color of the stroke of a finite automaton state.
    public static final Color STATE_STROKE_COLOR = Color.BLACK;
    // The length of the edge marking a finite automaton state as an initial
    // state.
    public static final double INITIAL_STATE_EDGE_LENGTH = 30;
    // The radius of the circle marking a finite automaton state as a final
    // state.
    public static final double FINAL_STATE_CIRCLE_RADIUS = STATE_RADIUS - 5;
    // The horizontal distance between two states, measured from their
    // circumference.
    public static final double STATE_SEPARATION = 60;

    ///////////
    // Edges //
    ///////////

    // The radius of the stroke of a finite automaton edge.
    public static final double EDGE_STROKE_RADIUS = 1;
    // The color of the stroke of a finite automaton edge.
    public static final Color EDGE_STROKE_COLOR = Color.BLACK;
    // The size (width and height) of the arrowhead of a finite automaton edge.
    public static final double ARROWHEAD_SIZE = 10;
    // The background color of the label on an edge.
    public static final Color EDGE_LABEL_COLOR = Color.WHITESMOKE;

    //////////////////
    // Curved Edges //
    //////////////////

    // The distance to move the control point perpendicularly from the centre
    // of a line between two states for a curved edge.
    public static final double CURVED_CONTROL_POINT_DISTANCE = 25;
    // The distance to move the control point from the start or end of a loop
    // edge.
    public static final double LOOP_CONTROL_POINT_DISTANCE = 50;

    /////////////////
    // Controllers //
    /////////////////

    // The color of the stroke of a finite automaton component when it is
    // selected by a user.
    public static final Color USER_HIGHLIGHT_COLOR = Color.RED;
    // The color of the fill of a finite automaton state when it has been chosen
    // by the user as the state to remove.
    public static final Color STATE_TO_REMOVE_FILL_COLOR = Color.DARKRED;
    // The color of the stroke of a finite automaton component when it is one
    // of the components on the indirect path between two states.
    public static final Color PATH_HIGHLIGHT_COLOR = Color.BLUE;
    // The color of the stroke of a finite automaton edge when its label is
    // being updated due to a finite automaton state being removed.
    public static final Color UPDATE_HIGHLIGHT_COLOR = Color.GREEN;

}
