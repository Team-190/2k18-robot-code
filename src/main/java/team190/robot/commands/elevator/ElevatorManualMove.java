package team190.robot.commands.elevator;


import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

/**
 * Only call from a Button so the Command is properly canceled.
 */
public class ElevatorManualMove extends Command {

    private double speed;

    public ElevatorManualMove(double speed) {
        requires(Robot.elevator);
        this.speed = speed;
    }

    @Override
    protected void execute() {
        Robot.elevator.manualMove(speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.elevator.stop();
    }

}
