package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionHigh extends Command {

    public ElevatorPositionHigh() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.POS_HI);
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.inPosition();
    }
}
