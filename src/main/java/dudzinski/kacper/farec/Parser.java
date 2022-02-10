package dudzinski.kacper.farec;

import javafx.util.Pair;

/**
 * The parser is used to parse strings representing regular expressions and turn them into regular expression objects.
 */
public class Parser {

    private static char starOperator = '*';
    private static char concatenationOperator = '|';
    private static char unionOperator = '+';
    private static String alphanumericPattern = "^[a-zA-Z0-9]*$";
    private static String symbolPattern = "^[!£$%^&*\\-+=:;@~#|<>,.?]$";
    private static String validRegexPattern = "^[a-zA-Z0-9()" + starOperator + concatenationOperator + unionOperator + "]*$";

    public enum REOperators {
        STAR, CONCATENATION, UNION
    }

    /**
     * Given a REOperator and a char, links the REOperator to that char and returns true if the char is a valid symbol.
     * If the char is not a valid symbol, returns false.
     * @param operator The REOperator to set the char for.
     * @param operatorChar The char that will represent the REOperator.
     * @return True if the char was valid and set successfully, false otherwise.
     */
    public static Boolean setOperatorChar(REOperators operator, char operatorChar){
        if (!(""+operatorChar).matches(symbolPattern)){
            return false;
        }
        if (operator == REOperators.STAR){
            starOperator = operatorChar;
        }
        else if (operator == REOperators.CONCATENATION){
            concatenationOperator = operatorChar;
        }
        else if (operator == REOperators.UNION){
            unionOperator = operatorChar;
        }
        return true;
    }

    /**
     * Given a regex operator, returns the char corresponding to that operator or throws an exception.
     * @param operator The regex operator for which to find the corresponding char.
     * @return The char corresponding to the given regex operator.
     * @throws IllegalArgumentException
     */
    public static char getOperatorChar(REOperators operator) throws IllegalArgumentException{
        if (operator == REOperators.STAR){
            return starOperator;
        }
        else if (operator == REOperators.CONCATENATION){
            return concatenationOperator;
        }
        else if (operator == REOperators.UNION){
            return unionOperator;
        }
        else{
            throw new IllegalArgumentException("Argument is not an REOperator!");
        }
    }

    /**
     * Given a char representation a regex operator, returns an enum constant representation for that operator, or
     * throws an exception if the char does not represent any operator.
     * @param operatorChar A char representing the operator.
     * @return An enum constant representing the operator.
     * @throws IllegalArgumentException
     */
    public static REOperators getOperator(char operatorChar) throws IllegalArgumentException{
        if (operatorChar == starOperator){
            return REOperators.STAR;
        }
        else if (operatorChar == concatenationOperator){
            return REOperators.CONCATENATION;
        }
        else if (operatorChar == unionOperator){
            return REOperators.UNION;
        }
        else{
            throw new IllegalArgumentException("The given char is not linked to any regex operator!");
        }
    }

    /**
     * Tests is a string is a valid regex string, by checking that it contains only alphanumeric characters, brackets
     * and regex operators. Also checks that all opened brackets are closed.
     * @param regexString The string to be tested.
     * @return True if the string is valid, false otherwise.
     */
    public static Pair<Boolean, String> isValid(String regexString){
        // Check if string contains only alphanumeric characters and operators.
        if (!regexString.matches(validRegexPattern)){
            return new Pair<>(false, "Regular expressions can only contain alphanumeric characters and operators!");
        }

        // Check if number of opened brackets matches number of closed brackets.
        int openBracketCount = 0;
        for (int index = 0; index < regexString.length(); ++index) {
            char currentChar = regexString.charAt(index);
            if (currentChar == '(') {
                openBracketCount++;
            } else if (currentChar == ')') {
                openBracketCount--;
                if (openBracketCount < 0){
                    return new Pair<>(false, "The regular expression has a closing bracket without an opening bracket!");
                }
            }
        }
        if (openBracketCount != 0){
            return new Pair<>(false, "The regular expression has different numbers of opening and closing brackets!");
        }

        return new Pair<>(true, "");
    }

    /**
     * Given a string, removes the outer brackets if they are linked.
     * @param regexString The string to be trimmed.
     * @return The same string, without the outer brackets.
     */
    public static String removeOuterBrackets(String regexString){
        // If string does not start and end with brackets, it cannot be trimmed.
        if (!(regexString.startsWith("(") && regexString.endsWith(")"))){
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
                if (openBracketCount == 0 && index != regexString.length() - 1){
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
     * @param regexString A regex expression, without outer brackets.
     * @return The index of the root operator, or -1 if not found.
     */
    public static int findRootIndex(String regexString){
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
            else if (openBracketCount == 0 && (currentChar ==  unionOperator || currentChar == concatenationOperator)) {
                return index;
            }
            else if (openBracketCount == 0 && starIndex == -1 && currentChar == starOperator) {
                starIndex = index;
            }
        }
        // If a CONCATENATION or UNION root operator was not found, return the STAR root operator if available.
        if (starIndex != -1){
            return starIndex;
        }
        return -1;
    }

    /**
     * Given a string representing a regex, parses the string into a regular expression object. If the regex is invalid,
     * throws an exception.
     * @param regexString The string to be parsed.
     * @return The regular expression
     * @throws IllegalArgumentException
     */
    public static RegularExpression parse(String regexString) throws IllegalArgumentException {
        // Check if regexString is valid.
        Pair<Boolean, String> isValid = isValid(regexString);
        if (!isValid.getKey()){
            throw new IllegalArgumentException(isValid.getValue());
        }

        // Remove outer brackets.
        regexString = removeOuterBrackets(regexString);

        // Check if regexString is a symbol.
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
        if (leftSubstring.length() == 0){
            throw new IllegalArgumentException("The expression \"" + regexString + "\" contains an empty left operand!");
        }
        RegularExpression leftOperand = parse(leftSubstring);

        // Get the operator
        REOperators operator = getOperator(regexString.charAt(rootIndex));

        // Find right operand
        RegularExpression rightOperand = null;
        if (operator == REOperators.CONCATENATION || operator == REOperators.UNION){
            if (rootIndex == regexString.length() - 1) {
                throw new IllegalArgumentException("The expression \"" + regexString + "\" contains an empty right operand!");
            }
            else {
                rightOperand = parse(regexString.substring(rootIndex + 1));
            }
        }
        else if (operator == REOperators.STAR && rootIndex != regexString.length() - 1){
            throw new IllegalArgumentException("The expression \"" + regexString + "\" contains a STAR operator with a right operand!");
        }

        return new ComplexRegularExpression(leftOperand, operator, rightOperand);
    }

}

