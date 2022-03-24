package dudzinski.kacper.farec.finiteautomata;

import dudzinski.kacper.farec.controllers.CreateFAScreenController;
import dudzinski.kacper.farec.finiteautomata.smart.SmartEdge;
import dudzinski.kacper.farec.finiteautomata.smart.SmartFiniteAutomaton;
import dudzinski.kacper.farec.finiteautomata.smart.SmartFiniteAutomatonBuilder;
import dudzinski.kacper.farec.finiteautomata.smart.SmartState;
import dudzinski.kacper.farec.regex.StartJavaFX;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the {@link SmartFiniteAutomaton} class and its methods.
 */
public class SmartFiniteAutomatonTest {

    /**
     * Because these tests involve JavaFX elements, the JavaFX runtime has to be
     * started first.
     */
    @BeforeAll
    static void startJavaFX() {
        StartJavaFX.startJavaFX();
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#isValid()} method.
     */
    @Nested
    @DisplayName("Checking if a finite automaton is valid returns")
    public class IsValidTest {
        @Nested
        @DisplayName("true when the finite automaton is")
        public class IsValidPositiveTest {
            @Test
            @DisplayName("valid")
            public void test1() {
                CreateFAScreenController createFAScreenController =
                        new CreateFAScreenController();

                SmartFiniteAutomaton finiteAutomaton =
                        new SmartFiniteAutomaton(createFAScreenController);

                SmartState initialState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(initialState);
                finiteAutomaton.setInitialState(initialState);

                SmartState finalState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(finalState);
                finiteAutomaton.setFinalState(finalState);

                SmartEdge initialToFinal =
                        SmartFiniteAutomatonBuilder.createEdge("",
                                                               initialState,
                                                               finalState);
                finiteAutomaton.addEdge(initialToFinal);

                assertTrue(finiteAutomaton.isValid());
            }
        }

        @Nested
        @DisplayName("false when he finite automaton is not valid because of")
        public class IsValidNegativeTest {
            @Test
            @DisplayName("a missing initial state")
            public void test1() {
                // Create controller.
                CreateFAScreenController createFAScreenController =
                        new CreateFAScreenController();

                SmartFiniteAutomaton finiteAutomaton =
                        new SmartFiniteAutomaton(createFAScreenController);

                SmartState initialState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(initialState);
                //finiteAutomaton.setInitialState(initialState);

                SmartState finalState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(finalState);
                finiteAutomaton.setFinalState(finalState);

                SmartEdge initialToFinal =
                        SmartFiniteAutomatonBuilder.createEdge("",
                                                               initialState,
                                                               finalState);
                finiteAutomaton.addEdge(initialToFinal);

                assertFalse(finiteAutomaton.isValid());
            }

            @Test
            @DisplayName("a missing final state")
            public void test2() {
                CreateFAScreenController createFAScreenController =
                        new CreateFAScreenController();

                SmartFiniteAutomaton finiteAutomaton =
                        new SmartFiniteAutomaton(createFAScreenController);

                SmartState initialState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(initialState);
                finiteAutomaton.setInitialState(initialState);

                SmartState finalState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(finalState);
                //finiteAutomaton.setFinalState(finalState);

                SmartEdge initialToFinal =
                        SmartFiniteAutomatonBuilder.createEdge("",
                                                               initialState,
                                                               finalState);
                finiteAutomaton.addEdge(initialToFinal);

                assertFalse(finiteAutomaton.isValid());
            }

            @Test
            @DisplayName("a state that is not connected")
            public void test3() {
                CreateFAScreenController createFAScreenController =
                        new CreateFAScreenController();

                SmartFiniteAutomaton finiteAutomaton =
                        new SmartFiniteAutomaton(createFAScreenController);

                SmartState initialState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(initialState);
                finiteAutomaton.setInitialState(initialState);

                SmartState finalState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(finalState);
                finiteAutomaton.setFinalState(finalState);

                SmartState unreachableState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(unreachableState);

                SmartEdge initialToFinal =
                        SmartFiniteAutomatonBuilder.createEdge("",
                                                               initialState,
                                                               finalState);
                finiteAutomaton.addEdge(initialToFinal);
                assertFalse(finiteAutomaton.isValid());
            }

            @Test
            @DisplayName("a state that is not reachable from the initial state")
            public void test4() {
                CreateFAScreenController createFAScreenController =
                        new CreateFAScreenController();

                SmartFiniteAutomaton finiteAutomaton =
                        new SmartFiniteAutomaton(createFAScreenController);

                SmartState initialState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(initialState);
                finiteAutomaton.setInitialState(initialState);

                SmartState finalState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(finalState);
                finiteAutomaton.setFinalState(finalState);

                SmartState unreachableState =
                        SmartFiniteAutomatonBuilder.createState("");
                finiteAutomaton.addState(unreachableState);

                SmartEdge initialToFinal =
                        SmartFiniteAutomatonBuilder.createEdge("",
                                                               initialState,
                                                               finalState);
                finiteAutomaton.addEdge(initialToFinal);

                SmartEdge unreachableToFinal =
                        SmartFiniteAutomatonBuilder.createEdge("",
                                                               unreachableState,
                                                               finalState);
                finiteAutomaton.addEdge(unreachableToFinal);

                assertFalse(finiteAutomaton.isValid());
            }
        }
    }

}
