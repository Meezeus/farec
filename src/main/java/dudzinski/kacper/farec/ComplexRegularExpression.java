package dudzinski.kacper.farec;

import dudzinski.kacper.farec.Parser.REOperators;

/**
 * This class represents a complex regular expression object. A complex regular expression is a regular expression made
 * up of a regex operator applied to one or two regular expressions, which themselves may also be complex.
 */
public class ComplexRegularExpression extends RegularExpression{

    private RegularExpression leftOperand;
    private REOperators operator;
    private RegularExpression rightOperand;

    public ComplexRegularExpression(RegularExpression leftOperand, REOperators operator, RegularExpression rightOperand){
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public RegularExpression getLeftOperand() {
        return leftOperand;
    }

    public REOperators getOperator() {
        return operator;
    }

    public RegularExpression getRightOperand() {
        return rightOperand;
    }

    public int getDepth(){
        if (operator == REOperators.STAR){
            return leftOperand.getDepth() + 1;
        }
        else {
            return Math.max(leftOperand.getDepth(), rightOperand.getDepth()) + 1;
        }
    }

    @Override
    public String toString(){
        return ("(" + leftOperand + ")" + operator + "(" + rightOperand + ")");
    }
}
