package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team190.robot.Robot;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionHigh extends Command {

    public ElevatorPositionHigh() {
        requires(Robot.elevator);
        setTimeout(6);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.POS_HI);
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.inPosition() || isTimedOut();
    }

    @Override
    protected void end() {
        //Robot.elevator.stop();
    }
}
