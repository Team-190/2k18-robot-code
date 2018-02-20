package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionLow extends Command {

    public ElevatorPositionLow() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.POS_LO);
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.inPosition();
    }
}
