package dudzinski.kacper.farec.regex;

/**
 * This enum represents the available regex operators. There are three regex
 * operators:<br>
 * <ul>
 *     <li>STAR(x):             zero or more x in a sequence</li>
 *     <li>CONCATENATION(x, y): x followed by y</li>
 *     <li>UNION(x y):          x xor y</li>
 * </ul>
 *
 * @see RegexOperatorChars
 * @see RegularExpression
 */
public enum RegexOperator {
    STAR, CONCATENATION, UNION
}
