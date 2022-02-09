package dudzinski.kacper.farec;

import dudzinski.kacper.farec.Parser.REOperators;

public class ComplexRegularExpression extends RegularExpression{

    private RegularExpression leftOperand;
    private RegularExpression rightOperand;
    private REOperators operator;

    public ComplexRegularExpression(){
        leftOperand = null;
        rightOperand = null;
        operator = null;
    }

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
