package dudzinski.kacper.farec.regex;

/**
 * This enum represents the available regex operators. There are three regex
 * operators:<br>
 * STAR(x):             zero or more x in a sequence
 * CONCATENATION(x, y): x followed by y
 * UNION(x y):          x xor y
 *
 * @see RegexOperatorChars
 * @see RegularExpression
 */
public enum RegexOperator {
    STAR, CONCATENATION, UNION
}
