package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Collector;

/** Only call from a Button so the Command is properly canceled.
 *
 */
public class CollectorManualMove extends Command {

    private final Collector.IntakeMode mode;

    public CollectorManualMove(Collector.IntakeMode mode) {
        this.mode = mode;
        requires(Robot.collector);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.collector.move(mode);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.collector.move(Collector.IntakeMode.Stop);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
