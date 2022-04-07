package dudzinski.kacper.farec.regex;

import dudzinski.kacper.farec.Settings;
import javafx.scene.paint.Color;

/**
 * /** This abstract class contains settings related to regular expressions
 * (including regular expression parse trees).
 */
public abstract class RegularExpressionSettings extends Settings {

    //////////////////////////
    // PARSE TREE CONTAINER //
    //////////////////////////

    // The color of the parse tree container.
    public static final Color CONTAINER_COLOR = Color.WHITE;

    //////////////////////
    // Parse Tree Nodes //
    //////////////////////

    // The radius of a regex parse tree node.
    public static final double NODE_RADIUS = 20;
    // The color of the fill of a regex parse tree node.
    public static final Color NODE_FILL = Color.LIGHTBLUE;
    // The radius of the stroke of a regex parse tree node.
    public static final double NODE_STROKE_RADIUS = 1;
    // The color of the stroke of a regex parse tree node.
    public static final Color NODE_STROKE_COLOR = Color.BLACK;
    // The color of the stroke of a regex parse tree node when it is
    // highlighted.
    public static final Color NODE_STROKE_HIGHLIGHT_COLOR = Color.RED;
    // The minimum horizontal separation between two nodes in a regex parse
    // tree.
    public static final double MIN_X_CHANGE = 50;
    // The minimum vertical separation between two nodes in a regex parse tree.
    public static final double MIN_Y_CHANGE = 80;

    //////////////////////
    // Parse Tree Edges //
    //////////////////////

    // The radius of the stroke of a regex parse tree edge.
    public static final double EDGE_STROKE_RADIUS = 1;
    // The color of the stroke of a regex parse tree edge.
    public static final Color EDGE_STROKE_COLOR = Color.BLACK;

    /////////////////////////
    // Regular Expressions //
    /////////////////////////

    private static final String VALID_REGEX_OPERATORS = "£$%^&*-+=:;@~#|<>";
    private static final String VALID_REGEX_OPERANDS = "a-zA-Z0-9";
    private static char starOperatorChar = '*';
    private static char concatenationOperatorChar = '|';
    private static char unionOperatorChar = '+';

    /**
     * Returns a regex string representing valid regex strings. A valid regex
     * string is made up of valid regex operands and valid regex operators that
     * are currently in use.
     *
     * @return a regex string representing valid regex strings
     */
    public static String getValidRegexStringPattern() {
        return "^[" +
                VALID_REGEX_OPERANDS +
                EMPTY_STRING +
                EMPTY_SET +
                starOperatorChar +
                concatenationOperatorChar +
                unionOperatorChar +
                "()]*$";
    }

    /**
     * Returns a regex string matching a single valid regex operator (regardless
     * of which regex operators are currently in use).
     *
     * @return a regex string representing valid regex operators
     */
    public static String getValidRegexOperatorPattern() {
        return "^[" +
                VALID_REGEX_OPERATORS +
                "]$";
    }

    /**
     * Returns a regex string representing a single valid regex operand.
     *
     * @return a regex string representing a single valid regex operand
     */
    public static String getValidRegexOperandPattern() {
        return "^[" +
                VALID_REGEX_OPERANDS +
                EMPTY_STRING +
                EMPTY_SET +
                "]$";
    }

    /**
     * Attempts to link the given regex operator to the given char. The char
     * must be a valid symbol in order for the link to be successful. The char
     * cannot be linked to any other regex operator.
     *
     * @param operator     the regex operator to set the char for
     * @param operatorChar the char that will represent the regex operator
     * @return true if the char was valid and set successfully, false otherwise
     */
    public static boolean setOperatorChar(RegexOperator operator,
                                          char operatorChar) {
        // Check the char is a valid symbol.
        if (!String.valueOf(operatorChar)
                   .matches(getValidRegexOperatorPattern())) {
            return false;
        }
        // Check the char is not already linked.
        if ((starOperatorChar == operatorChar)
                || (concatenationOperatorChar == operatorChar)
                || (unionOperatorChar == operatorChar)) {
            return false;
        }

        // Link the regex operator to the char.
        if (operator == RegexOperator.STAR) {
            starOperatorChar = operatorChar;
        }
        else if (operator == RegexOperator.CONCATENATION) {
            concatenationOperatorChar = operatorChar;
        }
        else if (operator == RegexOperator.UNION) {
            unionOperatorChar = operatorChar;
        }
        return true;
    }

    /**
     * Returns the char linked to the given regex operator.
     *
     * @param operator the regex operator for which to find the linked char
     * @return the char linked to the given regex operator
     * @throws IllegalArgumentException if the operator is not a regex operator
     */
    public static char getCharFromOperator(RegexOperator operator)
            throws IllegalArgumentException {
        if (operator == RegexOperator.STAR) {
            return starOperatorChar;
        }
        else if (operator == RegexOperator.CONCATENATION) {
            return concatenationOperatorChar;
        }
        else if (operator == RegexOperator.UNION) {
            return unionOperatorChar;
        }
        else {
            throw new IllegalArgumentException(
                    "Argument is not an REOperator!");
        }
    }

    /**
     * Returns the regex operator linked to the given char.
     *
     * @param operatorChar the char representation of an operator
     * @return the regex operator linked to the char
     * @throws IllegalArgumentException if the char is not linked to any regex
     *                                  operator
     */
    public static RegexOperator getOperatorFromChar(char operatorChar)
            throws IllegalArgumentException {
        if (operatorChar == starOperatorChar) {
            return RegexOperator.STAR;
        }
        else if (operatorChar == concatenationOperatorChar) {
            return RegexOperator.CONCATENATION;
        }
        else if (operatorChar == unionOperatorChar) {
            return RegexOperator.UNION;
        }
        else {
            throw new IllegalArgumentException(
                    "The given char is not linked to any regex operator!");
        }
    }

    /**
     * Returns the char linked to the STAR operator.
     *
     * @return the char linked to the STAR operator
     */
    public static char getStarOperatorChar() {
        return starOperatorChar;
    }

    /**
     * Returns the char linked to the CONCATENATION operator.
     *
     * @return the char linked to the CONCATENATION operator
     */
    public static char getConcatenationOperatorChar() {
        return concatenationOperatorChar;
    }

    /**
     * Returns the char linked to the UNION operator.
     *
     * @return the char linked to the UNION operator
     */
    public static char getUnionOperatorChar() {
        return unionOperatorChar;
    }

}
