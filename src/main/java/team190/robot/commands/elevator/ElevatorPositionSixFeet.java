package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionSixFeet extends Command {

    public ElevatorPositionSixFeet() {
        // requires elevator (control), intake & carriage (to ensure they don't spin while running)
        requires(Robot.elevator);
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.SIXFT);
    }

    @Override
    protected void execute() {
        Robot.collector.intake(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.inPosition();
    }
}
