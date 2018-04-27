package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;

/**
 * Only call from a Button so the Command is properly canceled.
 */
public class CarriageManualMove extends Command {

    private final Carriage.CarriageMode mode;

    public CarriageManualMove(Carriage.CarriageMode mode) {
        this.mode = mode;
        requires(Robot.carriage);
    }

    @Override
    protected void execute() {
        Robot.carriage.move(mode);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

}
