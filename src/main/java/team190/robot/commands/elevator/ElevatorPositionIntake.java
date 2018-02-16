package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionIntake extends Command {

    public ElevatorPositionIntake() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.POS_INTAKE);
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.inPosition();
    }
}
