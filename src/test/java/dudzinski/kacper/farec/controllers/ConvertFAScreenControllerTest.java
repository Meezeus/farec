package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.regex.RegexOperator;
import dudzinski.kacper.farec.regex.RegularExpressionSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the {@link ConvertFAScreenController} class and its methods.
 */
class ConvertFAScreenControllerTest {

    /**
     * Make sure the operators are set to their default symbols before each
     * test.
     */
    @BeforeEach
    void resetOperatorChars() {
        RegularExpressionSettings
                .setOperatorChar(RegexOperator.STAR, '*');
        RegularExpressionSettings
                .setOperatorChar(RegexOperator.UNION, '+');
        RegularExpressionSettings
                .setOperatorChar(RegexOperator.CONCATENATION, '|');
    }

    /**
     * Test class for the {@link ConvertFAScreenController#simplifyLabel(String)}
     * method.
     */
    @Nested
    @DisplayName("Simplifying a label returns the simplified label when the" +
            " label is")
    class SimplifyLabelTest {
        @Test
        @DisplayName("A+B|C*|D")
        void test1() {
            String testLabel = "A+B|C*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+B|C*|D", simplifiedLabel);
        }


        @Test
        @DisplayName("A+ε|C*|D")
        void test2() {
            String testLabel = "A+ε|C*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+C*|D", simplifiedLabel);
        }

        @Test
        @DisplayName("A+B|ø*|D")
        void test3() {
            String testLabel = "A+B|ø*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+B|D", simplifiedLabel);
        }

        @Test
        @DisplayName("A+B|ε*|D")
        void test4() {
            String testLabel = "A+B|ε*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+B|D", simplifiedLabel);
        }

        @Test
        @DisplayName("A+B|C*|ε")
        void test5() {
            String testLabel = "A+B|C*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+B|C*", simplifiedLabel);
        }

        @Test
        @DisplayName("A+ε|ø*|D")
        void test6() {
            String testLabel = "A+ε|ø*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+D", simplifiedLabel);
        }

        @Test
        @DisplayName("A+ε|ε*|D")
        void test7() {
            String testLabel = "A+ε|ε*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+D", simplifiedLabel);
        }

        @Test
        @DisplayName("A+B|ø*|ε")
        void test8() {
            String testLabel = "A+B|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+B", simplifiedLabel);
        }

        @Test
        @DisplayName("A+B|ε*|ε")
        void test9() {
            String testLabel = "A+B|ε*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+B", simplifiedLabel);
        }

        @Test
        @DisplayName("A+ε|C*|ε")
        void test10() {
            String testLabel = "A+ε|C*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+C*", simplifiedLabel);
        }

        @Test
        @DisplayName("A+ε|ø*|ε")
        void test11() {
            String testLabel = "A+ε|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+ε", simplifiedLabel);
        }

        @Test
        @DisplayName("A+ε|ε*|ε")
        void test12() {
            String testLabel = "A+ε|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("A+ε", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+B|C*|D")
        void test13() {
            String testLabel = "ε+B|C*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+B|C*|D", simplifiedLabel);
        }


        @Test
        @DisplayName("ε+ε|C*|D")
        void test14() {
            String testLabel = "ε+ε|C*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+C*|D", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+B|ø*|D")
        void test15() {
            String testLabel = "ε+B|ø*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+B|D", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+B|ε*|D")
        void test16() {
            String testLabel = "ε+B|ε*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+B|D", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+B|C*|ε")
        void test17() {
            String testLabel = "ε+B|C*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+B|C*", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+ε|ø*|D")
        void test18() {
            String testLabel = "ε+ε|ø*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+D", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+ε|ε*|D")
        void test19() {
            String testLabel = "ε+ε|ε*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+D", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+B|ø*|ε")
        void test20() {
            String testLabel = "ε+B|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+B", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+B|ε*|ε")
        void test21() {
            String testLabel = "ε+B|ε*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+B", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+ε|C*|ε")
        void test22() {
            String testLabel = "ε+ε|C*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε+C*", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+ε|ø*|ε")
        void test23() {
            String testLabel = "ε+ε|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε", simplifiedLabel);
        }

        @Test
        @DisplayName("ε+ε|ε*|ε")
        void test24() {
            String testLabel = "ε+ε|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+B|C*|D")
        void test25() {
            String testLabel = "ø+B|C*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("B|C*|D", simplifiedLabel);
        }


        @Test
        @DisplayName("ø+ε|C*|D")
        void test26() {
            String testLabel = "ø+ε|C*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("C*|D", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+B|ø*|D")
        void test27() {
            String testLabel = "ø+B|ø*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("B|D", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+B|ε*|D")
        void test28() {
            String testLabel = "ø+B|ε*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("B|D", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+B|C*|ε")
        void test29() {
            String testLabel = "ø+B|C*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("B|C*", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+ε|ø*|D")
        void test30() {
            String testLabel = "ø+ε|ø*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("D", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+ε|ε*|D")
        void test31() {
            String testLabel = "ø+ε|ε*|D";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("D", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+B|ø*|ε")
        void test32() {
            String testLabel = "ø+B|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("B", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+B|ε*|ε")
        void test33() {
            String testLabel = "ø+B|ε*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("B", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+ε|C*|ε")
        void test34() {
            String testLabel = "ø+ε|C*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("C*", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+ε|ø*|ε")
        void test35() {
            String testLabel = "ø+ε|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε", simplifiedLabel);
        }

        @Test
        @DisplayName("ø+ε|ε*|ε")
        void test36() {
            String testLabel = "ø+ε|ø*|ε";
            String simplifiedLabel =
                    ConvertFAScreenController.simplifyLabel(testLabel);
            assertEquals("ε", simplifiedLabel);
        }
    }
}