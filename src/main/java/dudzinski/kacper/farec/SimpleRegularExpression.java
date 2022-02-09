package dudzinski.kacper.farec;

public class SimpleRegularExpression extends RegularExpression {

    private char symbol;

    public SimpleRegularExpression(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol(){
        return symbol;
    }

    public int getDepth(){
        return 0;
    }

    @Override
    public String toString(){
        return String.valueOf(symbol);
    }
}
