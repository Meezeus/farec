package dudzinski.kacper.farec;

import dudzinski.kacper.farec.regex.RegexOperator;
import dudzinski.kacper.farec.regex.RegexOperatorChars;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegexOperatorCharsTest {

    @Nested
    @DisplayName("Setting an operator char returns")
    class SetOperatorCharTest {
        @Nested
        @DisplayName("true when setting")
        class SetOperatorCharPositiveTest {
            @Test
            @DisplayName("STAR as ^")
            void test1() {
                boolean result = RegexOperatorChars.setOperatorChar(RegexOperator.STAR, '^');
                assertTrue(result);
            }

            @Test
            @DisplayName("CONCATENATION as ,")
            void test2() {
                boolean result = RegexOperatorChars.setOperatorChar(RegexOperator.CONCATENATION, ',');
                assertTrue(result);
            }

            @Test
            @DisplayName("UNION as ?")
            void test3() {
                boolean result = RegexOperatorChars.setOperatorChar(RegexOperator.UNION, '?');
                assertTrue(result);
            }
        }

        @Nested
        @DisplayName("false when setting")
        class SetOperatorCharNegativeTest {
            @Test
            @DisplayName("CONCATENATION as /")
            void test1() {
                boolean result = RegexOperatorChars.setOperatorChar(RegexOperator.CONCATENATION, '/');
                assertFalse(result);
            }
            @Test
            @DisplayName("CONCATENATION as ! when ! is already linked to STAR")
            void test2() {
                RegexOperatorChars.setOperatorChar(RegexOperator.STAR, '!');
                boolean result = RegexOperatorChars.setOperatorChar(RegexOperator.CONCATENATION, '!');
                assertFalse(result);
            }
            @Test
            @DisplayName("CONCATENATION as ! when ! is already linked to CONCATENATION")
            void test3() {
                RegexOperatorChars.setOperatorChar(RegexOperator.CONCATENATION, '!');
                boolean result = RegexOperatorChars.setOperatorChar(RegexOperator.CONCATENATION, '!');
                assertFalse(result);
            }
        }
    }

    @Nested
    @DisplayName("Getting the char from an operator returns")
    class getCharFromOperatorTest {
        @Nested
        @DisplayName("the corresponding char when the operator is")
        class getCharFromOperatorPositiveTest {
            @Test
            @DisplayName("STAR")
            void test1() {
                RegexOperator regexOperator = RegexOperator.STAR;
                char regexOperatorChar = RegexOperatorChars.getCharFromOperator(regexOperator);
                assertEquals('*', regexOperatorChar);
            }

            @Test
            @DisplayName("CONCATENATION")
            void test2() {
                RegexOperator regexOperator = RegexOperator.CONCATENATION;
                char regexOperatorChar = RegexOperatorChars.getCharFromOperator(regexOperator);
                assertEquals('|', regexOperatorChar);
            }

            @Test
            @DisplayName("UNION")
            void test3() {
                RegexOperator regexOperator = RegexOperator.UNION;
                char regexOperatorChar = RegexOperatorChars.getCharFromOperator(regexOperator);
                assertEquals('+', regexOperatorChar);
            }
        }
    }

    @Nested
    @DisplayName("Getting an operator from a char")
    class getOperatorFromCharTest {
        @Nested
        @DisplayName("returns the correct operator when the char is")
        class getOperatorFromCharPositiveTest {
            @Test
            @DisplayName("*")
            void test1() {
                char regexOperatorChar = '*';
                RegexOperator regexOperator = RegexOperatorChars.getOperatorFromChar(regexOperatorChar);
                assertEquals(RegexOperator.STAR, regexOperator);
            }

            @Test
            @DisplayName("|")
            void test2() {
                char regexOperatorChar = '|';
                RegexOperator regexOperator = RegexOperatorChars.getOperatorFromChar(regexOperatorChar);
                assertEquals(RegexOperator.CONCATENATION, regexOperator);
            }

            @Test
            @DisplayName("+")
            void test3() {
                char regexOperatorChar = '+';
                RegexOperator regexOperator = RegexOperatorChars.getOperatorFromChar(regexOperatorChar);
                assertEquals(RegexOperator.UNION, regexOperator);
            }
        }

        @Nested
        @DisplayName("throws an error when the char is")
        class getOperatorFromCharNegativeTest {
            @Test
            @DisplayName("not linked to any operator")
            void test1() {
                char regexOperatorChar = ',';
                assertThrows(IllegalArgumentException.class, () -> RegexOperatorChars.getOperatorFromChar(regexOperatorChar));
            }
        }
    }

    @AfterAll
    static void resetOperatorChars() {
        RegexOperatorChars.setOperatorChar(RegexOperator.STAR, '*');
        RegexOperatorChars.setOperatorChar(RegexOperator.UNION, '+');
        RegexOperatorChars.setOperatorChar(RegexOperator.CONCATENATION, '|');
    }

}
