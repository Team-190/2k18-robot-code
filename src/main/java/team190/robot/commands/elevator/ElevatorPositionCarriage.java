package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPositionCarriage extends Command {

    public ElevatorPositionCarriage() {
        // requires elevator (control), intake & carriage (to ensure they don't spin while running)
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false; // TODO return true when elevator is at position
    }

    @Override
    public synchronized void cancel() {

    }

    @Override
    protected void end() {
        
    }
}
