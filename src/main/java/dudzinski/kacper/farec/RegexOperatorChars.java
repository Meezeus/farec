package dudzinski.kacper.farec;

public class RegexOperatorChars {

    private static String validOperatorSymbolPattern = "^[!£$%^&*\\-+=:;@~#|<>,.?]$";
    private static char starOperatorChar = '*';
    private static char concatenationOperatorChar = '|';
    private static char unionOperatorChar = '+';

    /**
     * Given a REOperator and a char, links the REOperator to that char and returns true if the char is a valid symbol.
     * If the char is not a valid symbol, returns false.
     * @param operator The REOperator to set the char for.
     * @param operatorChar The char that will represent the REOperator.
     * @return True if the char was valid and set successfully, false otherwise.
     */
    public static Boolean setOperatorChar(RegexOperator operator, char operatorChar){
        if (!(""+operatorChar).matches(validOperatorSymbolPattern)){
            return false;
        }
        if (operator == RegexOperator.STAR){
            starOperatorChar = operatorChar;
        }
        else if (operator == RegexOperator.CONCATENATION){
            concatenationOperatorChar = operatorChar;
        }
        else if (operator == RegexOperator.UNION){
            unionOperatorChar = operatorChar;
        }
        return true;
    }

    /**
     * Given a regex operator, returns the char corresponding to that operator or throws an exception.
     * @param operator The regex operator for which to find the corresponding char.
     * @return The char corresponding to the given regex operator.
     * @throws IllegalArgumentException
     */
    public static char getCharFromOperator(RegexOperator operator) throws IllegalArgumentException{
        if (operator == RegexOperator.STAR){
            return starOperatorChar;
        }
        else if (operator == RegexOperator.CONCATENATION){
            return concatenationOperatorChar;
        }
        else if (operator == RegexOperator.UNION){
            return unionOperatorChar;
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
    public static RegexOperator getOperatorFromChar(char operatorChar) throws IllegalArgumentException{
        if (operatorChar == starOperatorChar){
            return RegexOperator.STAR;
        }
        else if (operatorChar == concatenationOperatorChar){
            return RegexOperator.CONCATENATION;
        }
        else if (operatorChar == unionOperatorChar){
            return RegexOperator.UNION;
        }
        else{
            throw new IllegalArgumentException("The given char is not linked to any regex operator!");
        }
    }

    /**
     * @return The char associated with the STAR operator.
     */
    public static char getStarOperatorChar(){
        return starOperatorChar;
    }

    /**
     * @return The char associated with the CONCATENATION operator.
     */
    public static char getConcatenationOperatorChar(){
        return concatenationOperatorChar;
    }

    /**
     * @return The char associated with the UNION operator.
     */
    public static char getUnionOperatorChar(){
        return unionOperatorChar;
    }


}
