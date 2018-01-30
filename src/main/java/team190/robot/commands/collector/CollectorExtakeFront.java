package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;

public class CollectorExtakeFront extends Command {

    private boolean canExtake = true;

    public CollectorExtakeFront() {
        // TODO add requires to extake front
    }

    @Override
    protected void initialize() {
        /* pseudo code:
        canExtake = (if cube is loaded)
         */

        setTimeout(1); // TODO find actual timeout for extake front
    }

    @Override
    protected void execute() {
        // TODO implement extake front execute function
    }

    @Override
    protected boolean isFinished() {
        return !canExtake || isTimedOut();
    }

    @Override
    public synchronized void cancel() {

    }

    @Override
    protected void end() {

    }
}
