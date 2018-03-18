package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionClimb extends Command {

    public ElevatorPositionClimb() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.POS_MAX);
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.inPosition();
    }

    @Override
    protected void end() {
        Robot.elevator.stop();
    }
}
