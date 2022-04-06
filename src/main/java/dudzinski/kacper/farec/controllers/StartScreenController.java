package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.App;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This is the controller for the start screen view. The start screen is
 * displayed to the user when the application is first launched. It contains a
 * changelog as well as buttons to take the user to the screens for creating
 * finite automata and regular expressions.
 */
public final class StartScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    public Label welcomeLabel;
    public ScrollPane scrollPane;
    public Label changelogLabel;
    public Button convertFAButton;
    public Button convertREButton;

    /**
     * Sets the welcome label. Reads the changelog file and sets the changelog
     * label to the latest entry. Sets the background color of the changelog
     * label and binds its height to the height of the scroll pane. Sets the
     * background and border color of the scroll pane.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Set the welcome label.
        welcomeLabel.setText(
                """
                FAREC (Finite Automata and Regular Expression Converter) is an \
                educational tool designed to demonstrate the algorithms used \
                to convert between finite automata and regular expressions.
                                
                FAREC allows you to create a finite automaton/regular \
                expression and then convert it into an equivalent regular \
                expression/finite automaton. Each step of the conversion \
                process is shown.
                                             
                To convert finite automata into regular expressions, FAREC \
                uses the State Elimination with GNFAs algorithm. To convert \
                regular expressions into finite automata, FAREC uses the \
                McNaughton–Yamada–Thompson algorithm (also known as Thompson's \
                construction algorithm).
                                             
                To begin, press one of the buttons on the right.
                """
        );

        // Load the changelog.
        try {
            // Get and read the changelog file.
            URL changelogURL = App.class.getResource("changelog.txt");
            if (changelogURL != null) {
                List<String> changelogLines =
                        Files.readAllLines(Paths.get(changelogURL.toURI()));

                // Get the latest entry in the changelog and set the label.
                boolean readingLatestChanges = false;
                StringBuilder changelogEntry = new StringBuilder();
                for (String line : changelogLines) {
                    // If the line is ## when not yet reading the latest
                    // changes, the first entry has been reached.
                    if (!readingLatestChanges && line.matches("##\\s.*")) {
                        readingLatestChanges = true;
                    }
                    // If the line is ## when reading the latest changes, the
                    // second entry has been reached.
                    else if (readingLatestChanges && line.matches("##\\s.*")) {
                        break;
                    }
                    // Add the line to the changelogEntry.
                    changelogEntry.append(line).append("\n");
                }
                // Set the label.
                changelogLabel.setText(changelogEntry.toString().trim());

                // Set the background color of the label.
                changelogLabel.setBackground(
                        new Background(
                                new BackgroundFill(Color.WHITE,
                                                   CornerRadii.EMPTY,
                                                   Insets.EMPTY
                                )));

                // Bind the height of the label to the height of the scroll
                // pane. Subtract 5 so the scroll bar is not always visible.
                changelogLabel.minHeightProperty()
                              .bind(scrollPane.heightProperty().subtract(5));

                // Set the background color of the scroll pane.
                scrollPane.setBackground(
                        new Background(
                                new BackgroundFill(Color.WHITE,
                                                   CornerRadii.EMPTY,
                                                   Insets.EMPTY
                                )));

                // Set the border color of the scroll pane.
                scrollPane.setBorder(
                        new Border(
                                new BorderStroke(Color.BLACK,
                                                 BorderStrokeStyle.SOLID,
                                                 CornerRadii.EMPTY,
                                                 BorderWidths.DEFAULT
                                )
                        ));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the view to the screen for creating finite automata. This method
     * is called when the convertFAButton button is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openCreateFAScreen() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "create_fa_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                convertFAButton.getScene().getWidth(),
                                convertFAButton.getScene().getHeight());
        Stage stage = (Stage) convertFAButton.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Changes the view to the screen for creating regular expressions. This
     * method is called when the convertREButton button is pressed.
     *
     * @throws IOException if the view fxml file cannot be found
     */
    public void openCreateREScreen() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(
                "create_re_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                                convertREButton.getScene().getWidth(),
                                convertREButton.getScene().getHeight());
        Stage stage = (Stage) convertREButton.getScene().getWindow();
        stage.setScene(scene);
    }

}
