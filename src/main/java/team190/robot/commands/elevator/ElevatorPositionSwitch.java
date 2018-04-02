package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Elevator;

/**
 * Created by Kevin O'Brien on 3/27/2018.
 */
public class ElevatorPositionSwitch extends Command {

    public ElevatorPositionSwitch() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.POS_SWITCH);
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
