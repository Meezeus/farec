package dudzinski.kacper.farec.regex;

/**
 * This class represents a complex regular expression. A complex regular
 * expression is a regular expression consisting of a regex operator applied to
 * one or more regular expressions.
 *
 * @see RegularExpression
 * @see ComplexRegularExpression
 */
public final class ComplexRegularExpression extends RegularExpression {

    private final RegularExpression leftOperand;
    private final RegexOperator operator;
    private final RegularExpression rightOperand;

    /**
     * Creates a complex regular expression.
     *
     * @param leftOperand  the left operand of the regular expression
     * @param operator     the operator of the regular expression
     * @param rightOperand the right operand of the regular expression (might
     *                     be
     *                     <code>null</code>)
     * @throws IllegalArgumentException if the operator is STAR and there is a
     *                                  right operand
     */
    public ComplexRegularExpression(RegularExpression leftOperand,
                                    RegexOperator operator,
                                    RegularExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;

        if (operator == RegexOperator.STAR && rightOperand != null) {
            throw new IllegalArgumentException(
                    "STAR operator cannot have a right operand!");
        }
    }

    /**
     * Returns the string representation of this regular expression. The string
     * representation of a complex regular expression is the left operand in
     * brackets, followed by the operator symbol, followed by the right operand
     * in brackets. If the right operand is null, it is not included.
     *
     * @return the string representation of this regular expression
     */
    @Override
    public String toString() {
        String regexString = "(" + leftOperand + ")"
                + RegularExpressionSettings.getCharFromOperator(operator);
        if (rightOperand != null) {
            regexString += "(" + rightOperand + ")";
        }
        return regexString;
    }

    /**
     * Returns the depth of this regular expression. The depth of a complex
     * regular expression is the max of the depth of it's two operands plus 1.
     *
     * @return the depth of this regular expression
     */
    @Override
    public int getDepth() {
        if (operator == RegexOperator.STAR) {
            return leftOperand.getDepth() + 1;
        }
        else {
            return Math.max(
                    leftOperand.getDepth(),
                    rightOperand.getDepth()) + 1;
        }
    }

    /**
     * Returns the left operand of this regular expression.
     *
     * @return the left operand of this regular expression
     */
    public RegularExpression getLeftOperand() {
        return leftOperand;
    }

    /**
     * Returns the operator of this regular expression.
     *
     * @return the operator of this regular expression
     */
    public RegexOperator getOperator() {
        return operator;
    }

    /**
     * Returns the right operand of this regular expression.
     *
     * @return the right operand of this regular expression
     */
    public RegularExpression getRightOperand() {
        return rightOperand;
    }

}
