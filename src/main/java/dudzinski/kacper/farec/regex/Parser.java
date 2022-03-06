package dudzinski.kacper.farec.regex;

import javafx.util.Pair;

/**
 * The parser is used to parse strings representing regular expressions and turn them into regular expression objects.
 */
public class Parser {

    private static final String alphanumericPattern = "^[a-zA-Z0-9]*$";
    private static final String validRegexPattern = "^[a-zA-Z0-9()" +
            RegexOperatorChars.getStarOperatorChar() +
            RegexOperatorChars.getConcatenationOperatorChar() +
            RegexOperatorChars.getUnionOperatorChar() +
            "]*$";

    /**
     * Tests is a string is a valid regex string, by checking that it contains only alphanumeric characters, brackets
     * and regex operators. Also checks that all opened brackets are closed.
     *
     * @param regexString The string to be tested.
     * @return True if the string is valid, false otherwise.
     */
    public static Pair<Boolean, String> isValid(String regexString) {
        // Check if string contains only alphanumeric characters and operators.
        if (!regexString.matches(validRegexPattern)) {
            return new Pair<>(false, "Regular expressions can only contain alphanumeric characters and operators!");
        }

        // Check if number of opened brackets matches number of closed brackets.
        int openBracketCount = 0;
        for (int index = 0; index < regexString.length(); ++index) {
            char currentChar = regexString.charAt(index);
            if (currentChar == '(') {
                openBracketCount++;
            }
            else if (currentChar == ')') {
                openBracketCount--;
                if (openBracketCount < 0) {
                    return new Pair<>(false, "The regular expression has a closing bracket without an opening bracket!");
                }
            }
        }
        if (openBracketCount != 0) {
            return new Pair<>(false, "The regular expression has different numbers of opening and closing brackets!");
        }

        return new Pair<>(true, "");
    }

    /**
     * Given a regex string, removes the outer brackets if they are linked.
     *
     * @param regexString The string to be trimmed.
     * @return The same string, without the outer brackets.
     */
    public static String removeOuterBrackets(String regexString) {
        // If string does not start and end with brackets, it cannot be trimmed.
        if (!(regexString.startsWith("(") && regexString.endsWith(")"))) {
            return regexString;
        }

        int openBracketCount = 0;
        for (int index = 0; index < regexString.length(); ++index) {
            char currentChar = regexString.charAt(index);
            if (currentChar == '(') {
                openBracketCount++;
            }
            else if (currentChar == ')') {
                openBracketCount--;
                // If we have closed all brackets but are not at the end, the open bracket at the start and the close
                // bracket at the end are not linked. Return the string.
                if (openBracketCount == 0 && index != regexString.length() - 1) {
                    return regexString;
                }
            }
        }

        return regexString.substring(1, regexString.length() - 1);
    }

    /**
     * Given a regular expression, finds the index of the root operator. This is the operator that is not inside any
     * brackets. Will prefer a CONCATENATION or UNION operator over a STAR operator, otherwise returns the index of
     * the first root operator found.
     *
     * @param regexString A regex string, without outer brackets.
     * @return The index of the root operator, or -1 if not found.
     */
    public static int findRootIndex(String regexString) {
        int openBracketCount = 0;
        int starIndex = -1;
        for (int index = 0; index < regexString.length(); ++index) {
            char currentChar = regexString.charAt(index);
            if (currentChar == '(') {
                openBracketCount++;
            }
            else if (currentChar == ')') {
                openBracketCount--;
            }
            else if (openBracketCount == 0 && (currentChar == RegexOperatorChars.getUnionOperatorChar() || currentChar == RegexOperatorChars.getConcatenationOperatorChar())) {
                return index;
            }
            else if (openBracketCount == 0 && starIndex == -1 && currentChar == RegexOperatorChars.getStarOperatorChar()) {
                starIndex = index;
            }
        }
        return starIndex;
    }

    /**
     * Given a string representing a regular expression, parses the string into a regular expression object.
     * If the regular expression is invalid, throws an exception.
     *
     * @param regexString The string to be parsed.
     * @return The regular expression
     */
    public static RegularExpression parse(String regexString) throws IllegalArgumentException {
        // Check if regex string is valid.
        Pair<Boolean, String> isValid = isValid(regexString);
        if (!isValid.getKey()) {
            throw new IllegalArgumentException(isValid.getValue());
        }

        // Remove outer brackets.
        regexString = removeOuterBrackets(regexString);

        // Check if regex string is a symbol.
        if (regexString.length() == 1 && regexString.matches(alphanumericPattern)) {
            return new SimpleRegularExpression(regexString.charAt(0));
        }

        // Find root operator
        int rootIndex = findRootIndex(regexString);
        if (rootIndex == -1) {
            throw new IllegalArgumentException("The expression \"" + regexString + "\" does not have a root operator!");
        }

        // Find left operand
        String leftSubstring = regexString.substring(0, rootIndex);
        if (leftSubstring.length() == 0) {
            throw new IllegalArgumentException("The expression \"" + regexString + "\" contains an empty left operand!");
        }
        RegularExpression leftOperand = parse(leftSubstring);

        // Get the operator
        RegexOperator operator = RegexOperatorChars.getOperatorFromChar(regexString.charAt(rootIndex));

        // Find right operand
        RegularExpression rightOperand = null;
        if (operator == RegexOperator.CONCATENATION || operator == RegexOperator.UNION) {
            if (rootIndex == regexString.length() - 1) {
                throw new IllegalArgumentException("The expression \"" + regexString + "\" contains an empty right operand!");
            }
            else {
                rightOperand = parse(regexString.substring(rootIndex + 1));
            }
        }
        else if (operator == RegexOperator.STAR && rootIndex != regexString.length() - 1) {
            throw new IllegalArgumentException("The expression \"" + regexString + "\" contains a STAR operator with a right operand!");
        }

        return new ComplexRegularExpression(leftOperand, operator, rightOperand);
    }

}

