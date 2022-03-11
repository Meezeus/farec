package dudzinski.kacper.farec.regex;

/**
 * This class represents a complex regular expression object. A complex regular expression is a regular expression made
 * up of a regex operator applied to one or two regular expressions, which themselves may also be complex.
 */
public class ComplexRegularExpression extends RegularExpression {

    private final RegularExpression leftOperand;
    private final RegexOperator operator;
    private final RegularExpression rightOperand;

    /**
     * Create the complex regular expression.
     *
     * @param leftOperand  The left operand of the complex regular expression.
     * @param operator     The operator of the complex regular expression.
     * @param rightOperand The right operand of the complex regular expression. May be null.
     */
    public ComplexRegularExpression(RegularExpression leftOperand, RegexOperator operator, RegularExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;

        if (operator == RegexOperator.STAR && rightOperand != null) {
            throw new IllegalArgumentException("STAR operator cannot have a right operand!");
        }
    }

    /**
     * @return The left operand of the complex regular expression.
     */
    public RegularExpression getLeftOperand() {
        return leftOperand;
    }

    /**
     * @return The operator of the complex regular expression.
     */
    public RegexOperator getOperator() {
        return operator;
    }

    /**
     * @return The right operand of the complex regular expression.
     */
    public RegularExpression getRightOperand() {
        return rightOperand;
    }

    /**
     * @return The depth of the complex regular expression.
     */
    public int getDepth() {
        if (operator == RegexOperator.STAR) {
            return leftOperand.getDepth() + 1;
        }
        else {
            return Math.max(leftOperand.getDepth(), rightOperand.getDepth()) + 1;
        }
    }

    /**
     * @return The string representation of the complex regular expression.
     */
    @Override
    public String toString() {
        String regexString = "(" + leftOperand + ")" + RegexOperatorChars.getCharFromOperator(operator);
        if (rightOperand != null) {
            regexString += "(" + rightOperand + ")";
        }
        return regexString;
    }
}
