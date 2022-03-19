package dudzinski.kacper.farec.finiteautomata;

import dudzinski.kacper.farec.finiteautomata.graphical.GraphicalFiniteAutomatonBuilder;
import dudzinski.kacper.farec.finiteautomata.smart.SmartFiniteAutomatonBuilder;
import javafx.scene.paint.Color;

/**
 * This abstract class represents a finite automaton builder, which is used to
 * create and manipulate finite automata and their components.
 *
 * @see GraphicalFiniteAutomatonBuilder
 * @see SmartFiniteAutomatonBuilder
 */
public abstract class FiniteAutomatonBuilder {

    public static final double NODE_RADIUS = 20;
    public static final Color NODE_FILL_COLOR = Color.WHITE;
    public static final double NODE_STROKE_RADIUS = 1;
    public static final Color NODE_STROKE_COLOR = Color.BLACK;
    public static final double INITIAL_STATE_EDGE_LENGTH = 30;
    public static final double FINAL_STATE_CIRCLE_RADIUS = NODE_RADIUS - 5;

    public static final double EDGE_STROKE_RADIUS = 1;
    public static final Color EDGE_STROKE_COLOR = Color.BLACK;
    public static final double ARROWHEAD_SIZE = 10;

    public static final String EPSILON = "\u03B5";

}
