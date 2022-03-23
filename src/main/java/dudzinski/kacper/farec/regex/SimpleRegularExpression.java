package dudzinski.kacper.farec.regex;

/**
 * This class represents a simple regular expression. A simple regular
 * expression is a regular expression consisting of a single regex operand.
 *
 * @see RegularExpression
 * @see ComplexRegularExpression
 */
public class SimpleRegularExpression extends RegularExpression {

    private final char symbol;

    /**
     * Creates a simple regular expression.
     *
     * @param symbol the symbol of the simple regular expression
     */
    public SimpleRegularExpression(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the string representation of this regular expression. The string
     * representation of a simple regular expression is simply the symbol.
     *
     * @return the string representation of this regular expression
     */
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    /**
     * Returns the depth of this regular expression (0).
     *
     * @return the depth of this regular expression
     */
    @Override
    public int getDepth() {
        return 0;
    }

    /**
     * Returns the symbol of this regular expression.
     *
     * @return the symbol of this regular expression
     */
    public char getSymbol() {
        return symbol;
    }

}
