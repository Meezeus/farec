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

    public void setLeftOperand(RegularExpression leftOperand) {
        this.leftOperand = leftOperand;
    }

    public RegularExpression getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(RegularExpression rightOperand) {
        this.rightOperand = rightOperand;
    }

    public REOperators getOperator() {
        return operator;
    }

    public void setOperator(REOperators operator) {
        this.operator = operator;
    }

    @Override
    public String toString(){
        return ("(" + leftOperand + ")" + operator + "(" + rightOperand + ")");
    }
}
