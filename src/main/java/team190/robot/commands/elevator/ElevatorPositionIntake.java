package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
import team190.robot.subsystems.Elevator;

public class ElevatorPositionIntake extends Command {

    public ElevatorPositionIntake() {
        // requires elevator (control), intake & carriage (to ensure they don't spin while running)
        requires(Robot.elevator);
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        Robot.elevator.moveElevator(Elevator.FIVEFT);
        Robot.collector.intake(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

    /**
     * isFinished should return true in two cases
     * 1) when the elevator is in the intake position
     * 2) if the elevator has a cube loaded
     * <p>
     * this makes it so the operator cannot move the elevator
     * to the intake position if a cube is loaded
     *
     * @return
     */
    @Override
    protected boolean isFinished() {
        // TODO do we ever want to move to the intake position when we have a cube?
        return Robot.collector.hasCube() || Robot.carriage.hasCube() || Robot.elevator.inPosition();
    }
}
