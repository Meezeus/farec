package dudzinski.kacper.farec;

/**
 * This class represents the Command design pattern. A command represents some
 * action that can be taken. However, the command will also save any information
 * it needs to undo the action.
 */
public abstract class Command {

    /**
     * Executes the action of this command.
     */
    public abstract void execute();

    /**
     * Undoes the action of this command.
     */
    public abstract void undo();

}
