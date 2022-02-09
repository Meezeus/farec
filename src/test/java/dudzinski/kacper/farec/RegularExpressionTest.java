package dudzinski.kacper.farec;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class RegularExpressionTest {
    @Nested
    @DisplayName("The depth of a regular expression is returned correctly when the regex is")
    class GetDepthTest {
        @Test
        @DisplayName("a")
        void test1(){
            RegularExpression regex = new SimpleRegularExpression('a');
            int depth = regex.getDepth();
            assertEquals(0, depth);
        }
        @Test
        @DisplayName("a*")
        void test2(){
            RegularExpression regex = Parser.parse("a*");
            int depth = regex.getDepth();
            assertEquals(1, depth);
        }
        @Test
        @DisplayName("a+b")
        void test3(){
            RegularExpression regex = Parser.parse("a+b");
            int depth = regex.getDepth();
            assertEquals(1, depth);
        }
        @Test
        @DisplayName("a*+b")
        void test4(){
            RegularExpression regex = Parser.parse("a*+b");
            int depth = regex.getDepth();
            assertEquals(2, depth);
        }
        @Test
        @DisplayName("a+b+c")
        void test5(){
            RegularExpression regex = Parser.parse("a+b+c");
            int depth = regex.getDepth();
            assertEquals(2, depth);
        }
        @Test
        @DisplayName("(a+b)+(c+d)")
        void test6(){
            RegularExpression regex = Parser.parse("(a+b)+(c+d)");
            int depth = regex.getDepth();
            assertEquals(2, depth);
        }
        @Test
        @DisplayName("(a+b)*+(c+d)")
        void test7(){
            RegularExpression regex = Parser.parse("(a+b)*+(c+d)");
            int depth = regex.getDepth();
            assertEquals(3, depth);
        }
        @Test
        @DisplayName("((a*)*)*")
        void test8(){
            RegularExpression regex = Parser.parse("(a+b)*+(c+d)");
            int depth = regex.getDepth();
            assertEquals(3, depth);
        }
    }

}
