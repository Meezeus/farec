package dudzinski.kacper.farec.finiteautomata;

import dudzinski.kacper.farec.controllers.CreateFAScreenController;
import dudzinski.kacper.farec.finiteautomata.smart.*;
import dudzinski.kacper.farec.regex.StartJavaFX;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link SmartFiniteAutomaton} class and its methods.
 */
public class SmartFiniteAutomatonTest {

    private final CreateFAScreenController createFAScreenController =
            new CreateFAScreenController();

    private SmartFiniteAutomaton finiteAutomaton;

    /**
     * Because these tests involve JavaFX elements, the JavaFX runtime has to be
     * started first.
     */
    @BeforeAll
    static void startJavaFX() {
        StartJavaFX.startJavaFX();
    }

    /**
     * Create a new finite automaton for each test.
     */
    @BeforeEach
    public void setup() {
        finiteAutomaton = new SmartFiniteAutomaton(createFAScreenController);
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#addState(SmartState)}
     * method.
     */
    @Nested
    @DisplayName("Adding a state to a finite automaton")
    public class AddStateTest {
        @Test
        @DisplayName("adds it to the list of states")
        public void test1() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);

            assertTrue(finiteAutomaton.getStates().contains(state));
        }

        @Test
        @DisplayName("adds its container to the container of the finite" +
                " automaton")
        public void test2() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);

            assertTrue(finiteAutomaton.getContainer().getChildren()
                                      .contains(state.getContainer()));
        }
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#addEdge(SmartEdgeComponent)}
     * method.
     */
    @Nested
    @DisplayName("Adding an edge to a finite automaton")
    public class AddEdgeTest {
        @Test
        @DisplayName("adds it to the list of edges")
        public void test1() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);

            assertTrue(finiteAutomaton.getEdges().contains(edge));
        }

        @Test
        @DisplayName("adds its container to the container of the finite" +
                " automaton")
        public void test2() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);

            assertTrue(finiteAutomaton.getContainer().getChildren()
                                      .contains(edge.getContainer()));
        }

        @Test
        @DisplayName("replaces an existing, equivalent edge")
        public void test3() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge existingEdge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(existingEdge);

            SmartEdge newEdge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(newEdge);

            assertTrue(finiteAutomaton.getEdges().contains(newEdge));
            assertFalse(finiteAutomaton.getEdges().contains(existingEdge));
        }

        @Test
        @DisplayName("updates the states attached to the edge")
        public void test4() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);

            assertTrue(state1.getOutgoingEdges().contains(edge));
            assertTrue(state2.getIncomingEdges().contains(edge));
        }
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#removeEdge(SmartEdgeComponent)}
     * method.
     */
    @Nested
    @DisplayName("Removing an edge from a finite automaton")
    public class RemoveEdgeTest {
        @Test
        @DisplayName("removes it from the list of edges")
        public void test1() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);
            finiteAutomaton.removeEdge(edge);

            assertFalse(finiteAutomaton.getEdges().contains(edge));
        }

        @Test
        @DisplayName("removes its container from the container of the finite" +
                " automaton")
        public void test2() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);
            finiteAutomaton.removeEdge(edge);

            assertFalse(finiteAutomaton.getContainer().getChildren()
                                       .contains(edge.getContainer()));
        }

        @Test
        @DisplayName("updates the states attached to the edge")
        public void test3() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);

            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);
            finiteAutomaton.removeEdge(edge);

            assertFalse(state1.getOutgoingEdges().contains(edge));
            assertFalse(state2.getIncomingEdges().contains(edge));
        }
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#removeState(SmartState)}
     * method.
     */
    @Nested
    @DisplayName("Removing a state from a finite automaton")
    public class RemoveStateTest {
        @Test
        @DisplayName("removes it from the list of states")
        public void test1() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.removeState(state);

            assertFalse(finiteAutomaton.getStates().contains(state));
        }

        @Test
        @DisplayName("removes its container from the container of the finite" +
                " automaton")
        public void test2() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.removeState(state);

            assertFalse(finiteAutomaton.getContainer().getChildren()
                                       .contains(state.getContainer()));
        }

        @Test
        @DisplayName("resets the initial state of the finite automaton" +
                " accordingly")
        public void test3() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setInitialState(state);
            finiteAutomaton.removeState(state);

            assertNull(finiteAutomaton.getInitialState());
        }

        @Test
        @DisplayName("resets the initial state of the finite automaton" +
                " accordingly")
        public void test4() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setFinalState(state);
            finiteAutomaton.removeState(state);

            assertNull(finiteAutomaton.getFinalState());
        }

        @Test
        @DisplayName("removes all edges connected to the state")
        public void test5() {
            SmartState state1 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state1);
            SmartState state2 =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state2);

            SmartEdge edge =
                    SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                   state1,
                                                                   state2);
            finiteAutomaton.addEdge(edge);

            SmartLoopEdge loopEdge =
                    SmartFiniteAutomatonBuilder.createLoopEdge("",
                                                               state1);
            finiteAutomaton.addEdge(loopEdge);

            finiteAutomaton.removeState(state1);

            assertFalse(finiteAutomaton.getEdges().contains(edge));
            assertFalse(finiteAutomaton.getEdges().contains(loopEdge));
        }
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#setInitialState(SmartState)}
     * method.
     */
    @Nested
    @DisplayName("Setting a state as the initial state")
    public class SetInitialStateTest {
        @Test
        @DisplayName("sets it as the initial state of the finite automaton")
        public void test1() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setInitialState(state);

            assertEquals(state, finiteAutomaton.getInitialState());
        }

        @Test
        @DisplayName("sets it as an initial state")
        public void test2() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setInitialState(state);

            assertTrue(state.isInitial());
        }

        @Test
        @DisplayName("replaces the previous initial state")
        public void test3() {
            SmartState previousInitialState =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(previousInitialState);
            finiteAutomaton.setInitialState(previousInitialState);

            SmartState newInitialState =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(newInitialState);
            finiteAutomaton.setInitialState(newInitialState);

            assertFalse(previousInitialState.isInitial());
            assertTrue(newInitialState.isInitial());
            assertEquals(newInitialState, finiteAutomaton.getInitialState());
        }

        @Test
        @DisplayName("removes it as the final state")
        public void test4() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setFinalState(state);
            finiteAutomaton.setInitialState(state);

            assertFalse(state.isFinal());
            assertNotEquals(state, finiteAutomaton.getFinalState());
            assertEquals(state, finiteAutomaton.getInitialState());
        }
    }

    /**
     * Test class for the {@link SmartFiniteAutomaton#setFinalState(SmartState)}
     * method.
     */
    @Nested
    @DisplayName("Setting a state as the final state")
    public class SetFinalStateTest {
        @Test
        @DisplayName("sets it as the final state of the finite automaton")
        public void test1() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setFinalState(state);

            assertEquals(state, finiteAutomaton.getFinalState());
        }

        @Test
        @DisplayName("sets it as a final state")
        public void test2() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setFinalState(state);

            assertTrue(state.isFinal());
        }

        @Test
        @DisplayName("replaces the previous final state")
        public void test3() {
            SmartState previousFinalState =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(previousFinalState);
            finiteAutomaton.setFinalState(previousFinalState);

            SmartState newFinalState =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(newFinalState);
            finiteAutomaton.setFinalState(newFinalState);

            assertFalse(previousFinalState.isFinal());
            assertTrue(newFinalState.isFinal());
            assertEquals(newFinalState, finiteAutomaton.getFinalState());
        }

        @Test
        @DisplayName("removes it as the initial state")
        public void test4() {
            SmartState state =
                    SmartFiniteAutomatonBuilder.createState("");
            finiteAutomaton.addState(state);
            finiteAutomaton.setInitialState(state);
            finiteAutomaton.setFinalState(state);

            assertFalse(state.isInitial());
            assertNotEquals(state, finiteAutomaton.getInitialState());
            assertEquals(state, finiteAutomaton.getFinalState());
        }
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
                        SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                       initialState,
                                                                       finalState);
                finiteAutomaton.addEdge(initialToFinal);

                assertTrue(finiteAutomaton.isValid());
            }
        }

        @Nested
        @DisplayName("false when the finite automaton is not valid because of")
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
                        SmartFiniteAutomatonBuilder.createStraightEdge("",
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
                        SmartFiniteAutomatonBuilder.createStraightEdge("",
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
                        SmartFiniteAutomatonBuilder.createStraightEdge("",
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
                        SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                       initialState,
                                                                       finalState);
                finiteAutomaton.addEdge(initialToFinal);

                SmartEdge unreachableToFinal =
                        SmartFiniteAutomatonBuilder.createStraightEdge("",
                                                                       unreachableState,
                                                                       finalState);
                finiteAutomaton.addEdge(unreachableToFinal);

                assertFalse(finiteAutomaton.isValid());
            }
        }
    }

}
