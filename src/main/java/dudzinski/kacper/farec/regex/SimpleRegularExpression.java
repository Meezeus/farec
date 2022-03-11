package dudzinski.kacper.farec.regex;

/**
 * This class represents a simple regular expression object. A simple regular expression is a regular expression made up
 * of a single symbol only.
 */
public class SimpleRegularExpression extends RegularExpression {

    private final char symbol;

    /**
     * Create the simple regular expression.
     *
     * @param symbol The symbol of the simple regular expression.
     */
    public SimpleRegularExpression(char symbol) {
        this.symbol = symbol;
    }

    /**
     * @return The symbol of the simple regular expression.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * @return The depth of the simple regular expression.
     */
    public int getDepth() {
        return 0;
    }

    /**
     * @return The string representation of the simple regular expression.
     */
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

}
