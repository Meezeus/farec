package dudzinski.kacper.farec.regex;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link RegularExpressionSettings} class and its methods.
 */
public class RegularExpressionSettingsTest {

    /**
     * Make sure the operators are set to their default symbols before each
     * test.
     */
    @BeforeEach
    void resetOperatorCharsBefore() {
        RegularExpressionSettings.setOperatorChar(RegexOperator.STAR, '*');
        RegularExpressionSettings.setOperatorChar(RegexOperator.UNION, '+');
        RegularExpressionSettings.setOperatorChar(RegexOperator.CONCATENATION,
                                                  '|');
    }

    /**
     * Make sure the operators are set to their default symbols after each
     * test.
     */
    @AfterEach
    void resetOperatorCharsAfter() {
        RegularExpressionSettings.setOperatorChar(RegexOperator.STAR, '*');
        RegularExpressionSettings.setOperatorChar(RegexOperator.UNION, '+');
        RegularExpressionSettings.setOperatorChar(RegexOperator.CONCATENATION,
                                                  '|');
    }

    /**
     * Test class for the
     * {@link RegularExpressionSettings#setOperatorChar(RegexOperator, char)}
     * method.
     */
    @Nested
    @DisplayName("Setting an operator char returns")
    class SetOperatorCharTest {
        @Nested
        @DisplayName("true when setting")
        class SetOperatorCharPositiveTest {
            @Test
            @DisplayName("STAR as ^")
            void test1() {
                boolean result = RegularExpressionSettings.setOperatorChar(
                        RegexOperator.STAR, '^');
                assertTrue(result);
            }

            @Test
            @DisplayName("CONCATENATION as ;")
            void test2() {
                boolean result = RegularExpressionSettings.setOperatorChar(
                        RegexOperator.CONCATENATION, ';');
                assertTrue(result);
            }

            @Test
            @DisplayName("UNION as %")
            void test3() {
                boolean result = RegularExpressionSettings.setOperatorChar(
                        RegexOperator.UNION, '%');
                assertTrue(result);
            }
        }

        @Nested
        @DisplayName("false when setting")
        class SetOperatorCharNegativeTest {
            @Test
            @DisplayName("CONCATENATION as /")
            void test1() {
                boolean result = RegularExpressionSettings.setOperatorChar(
                        RegexOperator.CONCATENATION, '/');
                assertFalse(result);
            }

            @Test
            @DisplayName("CONCATENATION as ! when ! is already linked to STAR")
            void test2() {
                RegularExpressionSettings.setOperatorChar(RegexOperator.STAR,
                                                          '!');
                boolean result = RegularExpressionSettings.setOperatorChar(
                        RegexOperator.CONCATENATION, '!');
                assertFalse(result);
            }

            @Test
            @DisplayName("CONCATENATION as ! when ! is already linked to " +
                         "CONCATENATION")
            void test3() {
                RegularExpressionSettings.setOperatorChar(
                        RegexOperator.CONCATENATION, '!');
                boolean result = RegularExpressionSettings.setOperatorChar(
                        RegexOperator.CONCATENATION, '!');
                assertFalse(result);
            }
        }
    }

    /**
     * Test class for the
     * {@link RegularExpressionSettings#getCharFromOperator(RegexOperator)}
     * method.
     */
    @Nested
    @DisplayName(
            "Getting the char from an operator returns the corresponding " +
            "char when the operator is")
    class GetCharFromOperatorTest {
        @Test
        @DisplayName("STAR")
        void test1() {
            RegexOperator regexOperator = RegexOperator.STAR;
            char regexOperatorChar =
                    RegularExpressionSettings.getCharFromOperator(
                            regexOperator);
            assertEquals('*', regexOperatorChar);
        }

        @Test
        @DisplayName("CONCATENATION")
        void test2() {
            RegexOperator regexOperator = RegexOperator.CONCATENATION;
            char regexOperatorChar =
                    RegularExpressionSettings.getCharFromOperator(
                            regexOperator);
            assertEquals('|', regexOperatorChar);
        }

        @Test
        @DisplayName("UNION")
        void test3() {
            RegexOperator regexOperator = RegexOperator.UNION;
            char regexOperatorChar =
                    RegularExpressionSettings.getCharFromOperator(
                            regexOperator);
            assertEquals('+', regexOperatorChar);
        }
    }

    /**
     * Test class for the
     * {@link RegularExpressionSettings#getOperatorFromChar(char)} method.
     */
    @Nested
    @DisplayName("Getting an operator from a char")
    class GetOperatorFromCharTest {
        @Nested
        @DisplayName("returns the correct operator when the char is")
        class GetOperatorFromCharPositiveTest {
            @Test
            @DisplayName("*")
            void test1() {
                char regexOperatorChar = '*';
                RegexOperator regexOperator =
                        RegularExpressionSettings.getOperatorFromChar(
                                regexOperatorChar);
                assertEquals(RegexOperator.STAR, regexOperator);
            }

            @Test
            @DisplayName("|")
            void test2() {
                char regexOperatorChar = '|';
                RegexOperator regexOperator =
                        RegularExpressionSettings.getOperatorFromChar(
                                regexOperatorChar);
                assertEquals(RegexOperator.CONCATENATION, regexOperator);
            }

            @Test
            @DisplayName("+")
            void test3() {
                char regexOperatorChar = '+';
                RegexOperator regexOperator =
                        RegularExpressionSettings.getOperatorFromChar(
                                regexOperatorChar);
                assertEquals(RegexOperator.UNION, regexOperator);
            }
        }

        @Nested
        @DisplayName("throws an error when the char is")
        class GetOperatorFromCharNegativeTest {
            @Test
            @DisplayName("not linked to any operator")
            void test1() {
                char regexOperatorChar = ',';
                assertThrows(IllegalArgumentException.class,
                             () -> RegularExpressionSettings.getOperatorFromChar(
                                     regexOperatorChar));
            }
        }
    }

}
