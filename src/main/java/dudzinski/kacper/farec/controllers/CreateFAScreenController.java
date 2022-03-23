package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings;
import dudzinski.kacper.farec.finiteautomata.smart.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static dudzinski.kacper.farec.finiteautomata.FiniteAutomatonSettings.*;

/**
 * This is the controller for the view used to create finite automata. This view
 * is displayed when the user wants to create a finite automaton, so that it can
 * be converted into a regular expression. It allows the user to build a finite
 * automaton by creating states and adding edges between them.
 *
 * @see FiniteAutomatonSettings
 */
public class CreateFAScreenController implements Initializable {

    /**
     * This enum represents the available work modes. The work mode changes how
     * the user can interact with the finite automaton. There are three work
     * modes:<br>
     * <ul>
     *     <li>MOVE:  allows the user to move existing states</li>
     *     <li>STATE: allows the user to create new states</li>
     *     <li>EDGE:  allows the user to create new edges</li>
     * </ul>>
     */
    enum WorkMode {
        MOVE, STATE, EDGE
    }

    private FXMLLoader fxmlLoader;
    public ScrollPane scrollPane;
    public Button moveButton;
    public Button stateButton;
    public Button edgeButton;
    public Label infoLabel;
    public String infoLabelText;
    public Button convertButton;

    private WorkMode workMode = WorkMode.MOVE;
    private Map<Button, WorkMode> buttonToWorkMode;
    private Map<WorkMode, Button> workModeToButton;
    private SmartComponent currentlySelected;
    private final Color HIGHLIGHT_COLOR = Color.RED;
    private final ContextMenu stateContextMenu = createStateContextMenu();
    private final ContextMenu edgeContextMenu = createEdgeContextMenu();
    private final ContextMenu loopContextMenu = createLoopContextMenu();
    private SmartState edgeStartState;
    private SmartState edgeEndState;

    private final SmartFiniteAutomaton finiteAutomaton =
            new SmartFiniteAutomaton(this);

    /**
     * Sets the info label. Sets the key-press behaviour of the scene. Sets the
     * finite automaton container as the contents of the scroll pane. Creates
     * the bidirectional mappings between work modes and buttons.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the info label.
        infoLabelText = "Create a finite automaton to get started!";
        infoLabel.setText(infoLabelText);

        // Set the key-press behaviour for the scene.
        scrollPane.sceneProperty()
                  .addListener((observableScene, oldScene, newScene) -> {
                      if (oldScene == null && newScene != null) {
                          newScene.setOnKeyPressed(this::keyPressed);
                      }
                  });

        // Set the finite automaton container as the content of the scroll pane.
        scrollPane.setContent(finiteAutomaton.getContainer());

        // Create the mapping from buttons to work modes.
        buttonToWorkMode = Map.of(
                moveButton, WorkMode.MOVE,
                stateButton, WorkMode.STATE,
                edgeButton, WorkMode.EDGE
        );

        // Create the mapping from work modes to buttons.
        workModeToButton = Map.of(
                WorkMode.MOVE, moveButton,
                WorkMode.STATE, stateButton,
                WorkMode.EDGE, edgeButton
        );
    }

    /**
     * Unselects the currently selected component and removes its highlighting.
     */
    private void unselectCurrentlySelected() {
        if (currentlySelected instanceof SmartState) {
            currentlySelected.setStroke(STATE_STROKE_COLOR);
        }
        else if (currentlySelected instanceof SmartEdge) {
            currentlySelected.setStroke(EDGE_STROKE_COLOR);
        }
        currentlySelected = null;
    }

    /**
     * Selects and highlights the clicked component.
     *
     * @param event the clicked object
     */
    private void selectComponent(Event event) {
        // Remove the currently selected component and its highlighting.
        unselectCurrentlySelected();

        // Find the container of the clicked component.
        Node target = (Node) event.getTarget();
        while ((target != null)
                && (!Objects.equals(target.getId(), "selectable"))) {
            target = target.getParent();
        }

        // If a container was found, find what component it belongs to. Select
        // and highlight that component.
        if (target instanceof Group container) {
            // Get the list of components.
            ArrayList<SmartComponent> components = new ArrayList<>();
            components.addAll(finiteAutomaton.getStates());
            components.addAll(finiteAutomaton.getEdges());

            // Find the component with a matching container to the one found.
            for (SmartComponent component : components) {
                if (component.getContainer() == container) {
                    currentlySelected = component;
                    component.setStroke(HIGHLIGHT_COLOR);
                }
            }
        }
    }

    /**
     * Sets the current work mode to the given work mode. Highlights the work
     * mode button and unselects the currently selected component. Hides any
     * active context menus.
     *
     * @param newWorkMode the new work mode
     */
    private void setWorkMode(WorkMode newWorkMode) {
        // Unselect the currently selected component.
        unselectCurrentlySelected();

        // Set the new work mode.
        workMode = newWorkMode;

        // Highlight the appropriate button.
        moveButton.setBorder(null);
        stateButton.setBorder(null);
        edgeButton.setBorder(null);
        workModeToButton
                .get(newWorkMode)
                .setBorder(
                        new Border(
                                new BorderStroke(HIGHLIGHT_COLOR,
                                                 BorderStrokeStyle.SOLID,
                                                 new CornerRadii(3),
                                                 BorderStroke.DEFAULT_WIDTHS)));

        // Hide context menus.
        stateContextMenu.hide();
        edgeContextMenu.hide();
        loopContextMenu.hide();
    }

    /**
     * Sets the work mode to the work mode of the button pressed. This method is
     * called when one of the work mode buttons is pressed.
     *
     * @param actionEvent one of the work mode buttons being pressed
     */
    public void workModeButtonPressed(ActionEvent actionEvent) {
        setWorkMode(buttonToWorkMode.get((Button) actionEvent.getSource()));
    }

    /**
     * Sets the work mode based on what key was pressed. This method is called
     * when a key is pressed. What happens next depends on the key.
     * <ol>
     *     <li>1: MOVE mode is activated.</li>
     *     <li>2: STATE mode is activated.</li>
     *     <li>3: EDGE mode is activated.</li>
     * </ol>
     *
     * @param keyEvent a key being pressed
     */
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DIGIT1) {
            setWorkMode(WorkMode.MOVE);
        }
        else if (keyEvent.getCode() == KeyCode.DIGIT2) {
            setWorkMode(WorkMode.STATE);
        }
        else if (keyEvent.getCode() == KeyCode.DIGIT3) {
            setWorkMode(WorkMode.EDGE);
        }
    }

    /**
     * Opens a small window that allows the user to enter the text for a state
     * label. The window will block events to other applications windows. This
     * method is called when the user wants to rename a state.
     *
     * @return the new text for the state label, or null if the user cancelled
     * @throws IOException if the view fxml file cannot be found
     */
    private String getNewStateLabel() throws IOException {
        // Create the window.
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Rename State");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
                "new_state_label_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 200, 150);
        window.setScene(scene);
        window.showAndWait();

        // Get the new label text.
        NewStateLabelWindowController newStateLabelWindowController =
                fxmlLoader.getController();
        return newStateLabelWindowController.getNewStateLabelText();
    }

    /**
     * Opens a small window that allows the user to enter the text for an edge
     * label. The window will block events to other applications windows. This
     * method is called when the user wants to rename an edge.
     *
     * @return the new text for the edge label, or null if the user cancelled
     * @throws IOException if the view fxml file cannot be found
     */
    private String getNewEdgeLabel() throws IOException {
        // Create the window.
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Rename Edge");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
                "new_edge_label_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 200, 150);
        window.setScene(scene);
        window.showAndWait();

        // Get the new label text.
        NewEdgeLabelWindowController newEdgeLabelWindowController =
                fxmlLoader.getController();
        return newEdgeLabelWindowController.getNewEdgeLabelText();
    }

    /**
     * Creates the context menu for a state. The context menu for a state allows
     * the user to rename the state, set the state as initial, set the state as
     * final, and to delete the state.
     *
     * @return the context menu for the state
     */
    private ContextMenu createStateContextMenu() {
        ContextMenu stateContextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(event -> {
            try {
                String newLabel = getNewStateLabel();
                if (newLabel != null) {
                    SmartState state = (SmartState) currentlySelected;
                    state.setLabelText(newLabel);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem setInitial = new MenuItem("Set as initial");
        setInitial.setOnAction(event -> {
            SmartState state = (SmartState) currentlySelected;
            finiteAutomaton.setInitialState(state);
        });

        MenuItem setFinal = new MenuItem("Set as final");
        setFinal.setOnAction(event -> {
            SmartState state = (SmartState) currentlySelected;
            finiteAutomaton.setFinalState(state);
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> {
            SmartState state = (SmartState) currentlySelected;
            finiteAutomaton.removeState(state);
            currentlySelected = null;
        });

        stateContextMenu.getItems()
                        .addAll(rename, setInitial, setFinal, delete);
        return stateContextMenu;
    }

    /**
     * Creates the context menu for an edge. The context menu for an edge allows
     * the user to rename the edge and to delete the edge.
     *
     * @return the context menu for the edge
     */
    private ContextMenu createEdgeContextMenu() {
        ContextMenu edgeContextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(event -> {
            try {
                String newLabel = getNewEdgeLabel();
                if (newLabel != null) {
                    SmartEdge edge = (SmartEdge) currentlySelected;
                    edge.setLabelText(newLabel);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> {
            SmartEdge edge = (SmartEdge) currentlySelected;
            finiteAutomaton.removeEdge(edge, true);
            currentlySelected = null;
        });

        edgeContextMenu.getItems().addAll(rename, delete);
        return edgeContextMenu;
    }

    /**
     * Creates the context menu for a loop edge. The context menu for a loop
     * edge allows the user to rename the edge, flip the edge, and to delete the
     * edge.
     *
     * @return the context menu for the loop edge
     */
    private ContextMenu createLoopContextMenu() {
        ContextMenu loopContextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(event -> {
            try {
                String newLabel = getNewEdgeLabel();
                if (newLabel != null) {
                    SmartEdge edge = (SmartEdge) currentlySelected;
                    edge.setLabelText(newLabel);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem flip = new MenuItem("Flip");
        flip.setOnAction(event -> {
            SmartLoopEdge loop = (SmartLoopEdge) currentlySelected;
            loop.flip();
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> {
            SmartEdge edge = (SmartEdge) currentlySelected;
            finiteAutomaton.removeEdge(edge, true);
            currentlySelected = null;
        });

        loopContextMenu.getItems().addAll(rename, flip, delete);
        return loopContextMenu;
    }

    /**
     * Defines the behaviour for interacting with a state using a mouse.
     *
     * @param state the state for which to define mouse control behaviour
     */
    public void setStateMouseControl(SmartState state) {
        // Get the state container.
        Group container = state.getContainer();

        // Create the offset for dragging.
        class Offset {
            double x, y;
        }
        Offset offset = new Offset();

        // Clicking on the state
        container.setOnMousePressed(event -> {
            // hides the context menu.
            stateContextMenu.hide();

            // in MOVE mode will select it and set the offset.
            if ((workMode == WorkMode.MOVE)
                    && (event.getButton().equals(MouseButton.PRIMARY))) {
                selectComponent(event);
                offset.x = container.getTranslateX() - event.getSceneX();
                offset.y = container.getTranslateY() - event.getSceneY();
                event.consume();
            }
            // in EDGE mode will select it as starting state of the edge.
            if ((workMode == WorkMode.EDGE)
                    && (event.getButton().equals(MouseButton.PRIMARY))) {
                selectComponent(event);
                edgeStartState = (SmartState) currentlySelected;
                event.setDragDetect(true);
                event.consume();
            }
        });

        // Start full drag. Used when creating edges.
        container.setOnDragDetected(event -> container.startFullDrag());

        // Dragging the mouse on the state
        container.setOnMouseDragged(event -> {
            // in MOVE mode will move it along with the mouse.
            if ((workMode == WorkMode.MOVE)
                    && (event.getButton().equals(MouseButton.PRIMARY))) {
                // Don't allow the state to be moved past the left boundary.
                if (event.getSceneX() + offset.x >
                        STATE_RADIUS + STATE_STROKE_RADIUS) {
                    container.setTranslateX(event.getSceneX() + offset.x);
                }
                // Don't allow the state to be moved past the top boundary.
                if (event.getSceneY() + offset.y >
                        STATE_RADIUS + STATE_STROKE_RADIUS) {
                    container.setTranslateY(event.getSceneY() + offset.y);
                }
                // Update the size of the finite automaton container.
                finiteAutomaton.updateContainerSize();
            }
            event.consume();
        });

        // Releasing the mouse click
        container.setOnMouseDragReleased(event -> {
            // in EDGE mode will select it as the end state of the edge and create the edge.
            if ((workMode == WorkMode.EDGE)
                    && (event.getButton().equals(MouseButton.PRIMARY))) {
                selectComponent(event);
                edgeEndState = (SmartState) currentlySelected;
                // If the start state and end state are not the same state,
                // create an edge between them.
                SmartEdge edge;
                if (edgeStartState != edgeEndState) {
                    edge = SmartFiniteAutomatonBuilder
                            .createEdge(EMPTY_STRING, edgeStartState, edgeEndState);
                }
                // If the start state and end state are the same state, create a
                // loop edge.
                else {
                    edge = SmartFiniteAutomatonBuilder
                            .createLoopEdge(EMPTY_STRING, edgeStartState);
                }
                finiteAutomaton.addEdge(edge, true);
                event.consume();
            }
        });

        // Requesting the context menu for the state will select it and show the
        // context menu, regardless of work mode.
        container.setOnContextMenuRequested(event -> {
            selectComponent(event);
            stateContextMenu.show(container,
                                  event.getScreenX(),
                                  event.getScreenY());
            event.consume();
        });
    }

    /**
     * Defines the behaviour for interacting with the edge using a mouse.
     *
     * @param edge the edge for which to define mouse control behaviour
     */
    public void setEdgeMouseControl(SmartEdge edge) {
        // Get the edge container.
        Group container = edge.getContainer();

        // Clicking on the edge
        container.setOnMousePressed(event -> {
            // in MOVE mode will select it.
            if ((workMode == WorkMode.MOVE)
                    && (event.getButton().equals(MouseButton.PRIMARY))) {
                selectComponent(event);
                event.consume();
            }
        });

        // Requesting the context menu for the edge will select it and show the
        // context menu, regardless of work mode.
        container.setOnContextMenuRequested(event -> {
            selectComponent(event);
            if (edge instanceof SmartLoopEdge) {
                loopContextMenu.show(container,
                                     event.getScreenX(),
                                     event.getScreenY());
            }
            else {
                edgeContextMenu.show(container,
                                     event.getScreenX(),
                                     event.getScreenY());
            }
            event.consume();
        });
    }

    /**
     * Defines the behaviour for interacting with the finite automaton container
     * using a mouse.
     */
    public void setFAContainerMouseControl(
            SmartFiniteAutomaton finiteAutomaton) {
        // Get the finite automaton container.
        Pane container = finiteAutomaton.getContainer();

        // Clicking on the container
        container.setOnMousePressed(event -> {
            // will unselect the currently selected component.
            unselectCurrentlySelected();

            // in STATE mode will create a new state.
            if ((workMode == WorkMode.STATE)
                    && (event.getButton().equals(MouseButton.PRIMARY))) {
                SmartState state = SmartFiniteAutomatonBuilder.createState("");
                state.getContainer().setTranslateX(event.getX());
                state.getContainer().setTranslateY(event.getY());
                finiteAutomaton.addState(state);
            }
        });
    }

    /**
     * Opens a small window with help information. The window will block events
     * to other application windows. This method is called when the help button
     * is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openHelpWindow() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Help: Finite Automata");
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "fa_help_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Checks if the finite automaton is ready for conversion. If the finite
     * automaton is ready, changes the view to the screen for converting a
     * finite automaton into a regular expression and passes the finite
     * automaton to the controller of the new view. If the finite automaton is
     * not ready, displays an error message in the info label for a short time.
     * This method is called when the convert button is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void convert() throws IOException {
        // Unselect the currently selected component.
        unselectCurrentlySelected();

        // If the finite automaton is valid, change views and pass the finite
        // automaton to the new controller.
        if (finiteAutomaton.isValid()) {
            fxmlLoader = new FXMLLoader(App.class.getResource(
                    "convert_fa_screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),
                                    convertButton.getScene().getWidth(),
                                    convertButton.getScene().getHeight());
            Stage stage = (Stage) convertButton.getScene().getWindow();
            stage.setScene(scene);

            // Get the controller for the new view and pass it the finite
            // automaton.
            ConvertFAScreenController convertFAScreenController =
                    fxmlLoader.getController();
            convertFAScreenController.setFiniteAutomaton(finiteAutomaton);
        }
        // If the finite automaton is not valid, show an error message in the
        // info label for a short time.
        else {
            infoLabel.setText("The finite automaton is not valid!");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> infoLabel.setText(infoLabelText));
                }
            }, 5000);
        }
    }

}
