package dudzinski.kacper.farec;

public class SimpleRegularExpression extends RegularExpression {

    private char symbol;

    public SimpleRegularExpression(char symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return String.valueOf(symbol);
    }
}
