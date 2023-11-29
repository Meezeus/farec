package dudzinski.kacper.farec.regex;

import javafx.util.Pair;

import java.util.Objects;

/**
 * This class is responsible for parsing strings representing regular
 * expressions (regex strings) and converting them into regular expression
 * objects.
 *
 * @see RegularExpression
 */
public final class Parser {

    /**
     * This class contains static methods only and so objects of this class
     * should never be created.
     */
    private Parser() {
        throw new RuntimeException(
                "The Parser class should never be instantiated!");
    }

    /**
     * Tests if a string is a valid regex string. A valid regex string contains
     * only regex operands, brackets and regex operators. The number and
     * placement of brackets must be valid. This method DOES NOT test if this
     * string represents a valid regular expression.
     *
     * @param regexString the regex string to test for validity
     * @return a pair (K, V) where K is a boolean representing whether the regex
     * string is valid, and V is an error message in the case that it is not
     */
    public static Pair<Boolean, String> isValid(String regexString) {
        // Check that string contains only regex operands and regex operators.
        if (!regexString.matches(
                RegularExpressionSettings.getValidRegexStringPattern())) {
            return new Pair<>(false, "Regular expressions can only contain" +
                                     " regex operands and regex operators!");
        }

        // Check that the number and placement of brackets is valid.
        int openingBracketCount = 0;
        for (int index = 0; index < regexString.length(); ++index) {
            char currentChar = regexString.charAt(index);
            if (currentChar == '(') {
                openingBracketCount++;
            }
            else if (currentChar == ')') {
                openingBracketCount--;
                if (openingBracketCount < 0) {
                    return new Pair<>(false, "The regular expression has a " +
                                             "closing bracket without an " +
                                             "opening bracket!");
                }
            }
        }
        if (openingBracketCount != 0) {
            return new Pair<>(false, "The regular expression has different " +
                                     "numbers of opening and closing " +
                                     "brackets!");
        }

        // At this point, the regex string is considered valid.
        return new Pair<>(true, "");
    }

    /**
     * Removes the outer brackets of the given regex string, if they are
     * linked.
     *
     * @param regexString the string from which to remove outer brackets
     * @return the same string, without the outer brackets
     */
    public static String removeOuterBrackets(String regexString) {
        // If the string does not have an opening bracket at the start and a
        // closing bracket at the end, it cannot be trimmed.
        if (!(regexString.startsWith("(") && regexString.endsWith(")"))) {
            return regexString;
        }

        // See if the outer brackets are linked.
        int openingBracketCount = 0;
        for (int index = 0; index < regexString.length(); ++index) {
            char currentChar = regexString.charAt(index);
            if (currentChar == '(') {
                openingBracketCount++;
            }
            else if (currentChar == ')') {
                openingBracketCount--;
                // If we have closed all brackets but are not at the end, the
                // opening bracket at the start and the closing bracket at the
                // end are not linked.
                if ((openingBracketCount == 0) &&
                    (index != regexString.length() - 1)) {
                    return regexString;
                }
            }
        }

        // At this point, the outer brackets must be linked: remove them.
        return regexString.substring(1, regexString.length() - 1);
    }

    /**
     * Finds the index of the root operator of the given regex string. The root
     * operator is the operator with the lowest precedence that is not inside
     * any brackets.
     * <p>
     * Regex operators have a certain order of operations, with the STAR
     * operator having the highest precedence, then CONCATENATION, and then
     * UNION. However, the root operator is the operator with the lowest
     * precedence, so this method will return UNION over CONCATENATION and
     * CONCATENATION over STAR.
     * <p>
     * The regex string is parsed right to left, so chains such as a|b|c are
     * parsed as (a|b)|c instead of a|(b|c).
     *
     * @param regexString the regex string, without outer brackets
     * @return the index of the root operator, or -1 if not found
     */
    public static int findRootIndex(String regexString) {
        int depth = 0;
        int unionIndex = -1;
        int concatenationIndex = -1;
        int starIndex = -1;

        // Find the index of the first regex operator of each type which is not
        // contained within any brackets, going right to left.
        for (int index = regexString.length() - 1; index >= 0; --index) {
            char currentChar = regexString.charAt(index);

            if (currentChar == ')') {
                depth++;
            }
            else if (currentChar == '(') {
                depth--;
            }
            else if ((depth == 0) && (unionIndex == -1) && (currentChar ==
                                                            RegularExpressionSettings.getUnionOperatorChar())) {
                unionIndex = index;
            }
            else if ((depth == 0) && (concatenationIndex == -1) &&
                     (currentChar ==
                      RegularExpressionSettings.getConcatenationOperatorChar())) {
                concatenationIndex = index;
            }
            else if ((depth == 0) && (starIndex == -1) && (currentChar ==
                                                           RegularExpressionSettings.getStarOperatorChar())) {
                starIndex = index;
            }
        }

        // Return the index of the regex operators found, starting with the
        // regex operator with the lowest precedence.
        if (unionIndex != -1) {
            return unionIndex;
        }
        else if (concatenationIndex != -1) {
            return concatenationIndex;
        }
        else //noinspection RedundantIfStatement
            if (starIndex != -1) {
                return starIndex;
            }
            // If no regex operators were found at the root, return -1.
            else {
                return -1;
            }
    }

    /**
     * Parses the given regex string into a regular expression object. For
     * information about how the regex string is parsed, see {@link
     * #findRootIndex(String)}.
     *
     * @param regexString the string to be parsed
     * @return the regular expression represented by the regex string
     * @throws IllegalArgumentException if the regex string is invalid or does
     *                                  not represent a valid regular
     *                                  expression
     * @see #findRootIndex(String)
     */
    public static RegularExpression parseRegexString(String regexString)
            throws IllegalArgumentException {
        // Check that regex string is valid.
        Pair<Boolean, String> isValid = isValid(regexString);
        if (!isValid.getKey()) {
            throw new IllegalArgumentException(isValid.getValue());
        }

        // Remove outer brackets.
        while (!regexString.equals(removeOuterBrackets(regexString))) {
            regexString = removeOuterBrackets(regexString);
        }

        // If the regex string is a single regex operand character, it
        // represents a simple regular expression.
        if (regexString.length() == 1) {
            // Check if the operand is valid.
            if (regexString.matches(
                    RegularExpressionSettings.getValidRegexOperandPattern())) {
                return new SimpleRegularExpression(regexString.charAt(0));
            }
            else {
                throw new IllegalArgumentException(
                        regexString + " is not a valid regex operand!");
            }
        }

        // Find the index of the root operator.
        int rootIndex = findRootIndex(regexString);
        if (rootIndex == -1) {
            throw new IllegalArgumentException(
                    "The expression \"" + regexString + "\" does not have a" +
                    " root operator!");
        }

        // Parse the left operand substring.
        String leftSubstring = regexString.substring(0, rootIndex);
        if (leftSubstring.isEmpty()) {
            throw new IllegalArgumentException(
                    "The expression \"" + regexString + "\" contains an" +
                    " empty left operand!");
        }
        RegularExpression leftOperand = parseRegexString(leftSubstring);

        // Get the operator
        RegexOperator operator = RegularExpressionSettings.getOperatorFromChar(
                regexString.charAt(rootIndex));

        // Parse the right operand substring, if it exists.
        RegularExpression rightOperand = null;
        // If the regex operator is CONCATENATION or UNION, there should be a
        // right operand substring.
        if ((operator == RegexOperator.CONCATENATION) ||
            (operator == RegexOperator.UNION)) {
            if (rootIndex == regexString.length() - 1) {
                throw new IllegalArgumentException(
                        "The expression \"" + regexString + "\" contains an" +
                        " empty right operand!");
            }
            else {
                rightOperand =
                        parseRegexString(regexString.substring(rootIndex + 1));
            }
        }
        // If the regex operator is STAR, there shouldn't be a right operand
        // substring.
        else if (operator == RegexOperator.STAR) {
            if (rootIndex != regexString.length() - 1) {
                throw new IllegalArgumentException(
                        "The expression \"" + regexString + "\" contains a" +
                        " STAR operator with a right operand!");
            }
        }

        // Return the complex regular expression.
        return new ComplexRegularExpression(leftOperand, operator,
                                            rightOperand);
    }

    /**
     * Simplifies the given regex string by removing all brackets that can be
     * safely removed without altering the regular expression.
     * <p>
     * The regular expression is checked to see that it is not altered by
     * parsing the new regex string and checking that the resulting regular
     * expression is equal to the original regular expression.
     *
     * @param regexString the regex string to simplify
     * @return an equivalent regex string with all superfluous brackets removed
     */
    public static String simplifyRegexString(String regexString) {
        // Get the regular expression represented by the regex string as a
        // string.
        String originalRegularExpressionAsString =
                parseRegexString(regexString).toString();

        int offset = 0;
        // Iterate through the regex string to find brackets to remove.
        while (offset < regexString.length()) {
            int currentDepth = 0;
            int openingBracketDepth = -1;
            int openingBracketIndex = -1;
            int closingBracketIndex = -1;

            // Find the positions of matching opening and closing brackets to
            // try and remove.
            for (int index = offset; index < regexString.length(); index++) {
                // The current char being examined.
                char currentChar = regexString.charAt(index);

                // If the current char is an opening bracket, increase the
                // depth.
                if (currentChar == '(') {
                    currentDepth++;
                    // If this is the first opening bracket found, take note of
                    // its depth and index.
                    if (openingBracketIndex == -1) {
                        openingBracketDepth = currentDepth;
                        openingBracketIndex = index;
                    }
                }
                // If the current char is a closing bracket at the same depth as
                // the found opening bracket, take note of its index and stop
                // searching.
                if ((currentChar == ')') && (openingBracketIndex != -1) &&
                    (currentDepth == openingBracketDepth)) {
                    closingBracketIndex = index;
                    break;
                }

                // If the current char is a closing bracket, decrease the depth.
                if (currentChar == ')') {
                    currentDepth--;
                }
            }

            // If opening and closing brackets were found, attempt to remove
            // them.
            if ((openingBracketIndex != -1) && (closingBracketIndex != -1)) {
                // Remove the brackets from the string.
                StringBuilder newRegexStringBuilder =
                        new StringBuilder(regexString);
                newRegexStringBuilder.deleteCharAt(closingBracketIndex)
                        .deleteCharAt(openingBracketIndex);
                String newRegexString = newRegexStringBuilder.toString();

                // Get the regular expression represented by the new regex
                // string as a string.
                String newRegularExpressionAsString =
                        parseRegexString(newRegexString).toString();

                // If the regular expression has not changed, set the regex
                // string to the new regex string.
                if (Objects.equals(originalRegularExpressionAsString,
                                   newRegularExpressionAsString)) {
                    regexString = newRegexString;
                }
                // If the regular expression has changed, increase the offset.
                else {
                    offset += 1;
                }
            }
            // If we have not found opening and closing brackets, there are no
            // more brackets to simplify. Stop searching.
            else {
                break;
            }
        }

        // Return the (potentially simplified) regex string.
        return regexString;
    }

}

