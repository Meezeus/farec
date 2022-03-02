package dudzinski.kacper.farec.regex;

/**
 * This class represents a simple regular expression object. A simple regular expression is a regular expression made up
 * of a single symbol only.
 */
public class SimpleRegularExpression extends RegularExpression {

    private char symbol;

    public SimpleRegularExpression(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getDepth() {
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

}
