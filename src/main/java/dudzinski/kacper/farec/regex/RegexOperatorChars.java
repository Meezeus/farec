package dudzinski.kacper.farec.regex;

/**
 * This class contains methods for converting between regex operators and their
 * char representations, as well as changing the char representations.
 *
 * @see RegexOperator
 */
public class RegexOperatorChars {

    private static final String validOperatorSymbolPattern =
            "^[!£$%^&*\\-+=:;@~#|<>,.?]$";
    private static char starOperatorChar = '*';
    private static char concatenationOperatorChar = '|';
    private static char unionOperatorChar = '+';

    /**
     * Attempts to link the given regex operator to the given char. The char
     * must be a valid symbol in order for the link to be successful. The char
     * cannot be linked to any other regex operator.
     *
     * @param regexOperator the regex operator to set the char for
     * @param operatorChar  the char that will represent the regex operator
     * @return true if the char was valid and set successfully, false otherwise
     */
    public static Boolean setOperatorChar(RegexOperator regexOperator,
                                          char operatorChar) {
        // Check the char is a valid symbol.
        if (!("" + operatorChar).matches(validOperatorSymbolPattern)) {
            return false;
        }
        // Check the char is not already linked.
        if ((starOperatorChar == operatorChar)
                || (concatenationOperatorChar == operatorChar)
                || (unionOperatorChar == operatorChar)) {
            return false;
        }

        // Link the regex operator to the char.
        if (regexOperator == RegexOperator.STAR) {
            starOperatorChar = operatorChar;
        }
        else if (regexOperator == RegexOperator.CONCATENATION) {
            concatenationOperatorChar = operatorChar;
        }
        else if (regexOperator == RegexOperator.UNION) {
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
