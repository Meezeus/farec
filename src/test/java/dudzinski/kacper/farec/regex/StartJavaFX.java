package dudzinski.kacper.farec.regex;

import javafx.application.Platform;

/**
 * This class ensures the JavaFX runtime is only started once during testing.
 */
public abstract class StartJavaFX {

    private static boolean started = false;

    /**
     * Starts the JavaFX runtime, if it's not already running.
     */
    public static void startJavaFX() {
        if (!started) {
            started = true;
            Platform.startup(() -> {
            });
        }
    }

}
