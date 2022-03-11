package dudzinski.kacper.farec.regex;

import javafx.util.Pair;

import java.util.Objects;

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
        while (!regexString.equals(removeOuterBrackets(regexString))) {
            regexString = removeOuterBrackets(regexString);
        }

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

    /**
     * Simplifies the given regex string by removing all brackets that can be safely removed without altering the
     * regular expression. It checks that the regular expression is not altered by parsing the new regex string and
     * checking that the resulting regular expression is equal to the original regular expression.
     *
     * @param regexString The regex string to simplify.
     */
    public static String simplifyRegexString(String regexString) {
        // Get the string representation of the regular expression of the original regex string.
        String originalRegularExpressionString = parse(regexString).toString();

        int offset = 0;
        while (offset < regexString.length()) {
            int currentDepth = 0;
            int openBracketDepth = -1;
            int openBracketIndex = -1;
            int closeBracketIndex = -1;
            // Find the positions of matching open and closing brackets.
            for (int index = offset; index < regexString.length(); index++) {
                // The current char being examined.
                char currentChar = regexString.charAt(index);

                // If the current char is an open bracket, increase the depth.
                if (currentChar == '(') {
                    currentDepth++;
                }

                // If we haven't found an open bracket yet and the current char is an open bracket, take note of the
                // depth and index.
                if ((openBracketIndex == -1) && (currentChar == '(')) {
                    openBracketDepth = currentDepth;
                    openBracketIndex = index;
                }
                // If we have found an open bracket but not a close bracket, and the current depth matches the depth
                // of the open bracket found, and the current char is a close bracket, take note of the index and stop.
                else if ((openBracketIndex != -1) && (closeBracketIndex == -1) && (currentDepth == openBracketDepth) && (currentChar == ')')) {
                    closeBracketIndex = index;
                    break;
                }

                // If the current char is a close bracket, decrease the depth.
                if (currentChar == ')') {
                    currentDepth--;
                }
            }

            // If we found open and close bracket indices, we can attempt to remove the found brackets.
            if ((openBracketIndex != -1) && (closeBracketIndex != -1)) {
                // Remove the brackets from the string.
                StringBuilder newRegexStringBuilder = new StringBuilder(regexString);
                newRegexStringBuilder.deleteCharAt(closeBracketIndex).deleteCharAt(openBracketIndex);

                // Get the string representation of the regular expression of the new regex string.
                String newRegularExpressionString = parse(newRegexStringBuilder.toString()).toString();

                // If the regular expression has not changed, update the regex string.
                if (Objects.equals(originalRegularExpressionString, newRegularExpressionString)) {
                    regexString = newRegexStringBuilder.toString();
                }
                // If the regular expression has changed, increase the offset.
                else {
                    offset += 1;
                }
            }
            // If we have not found open and close bracket indices, there are no brackets to simplify. Stop searching.
            else {
                break;
            }
        }
        return regexString;
    }

}

