package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

public class CollectorTransferCarriage extends Command {

    public CollectorTransferCarriage() {
        // requires
        requires(Robot.collector);
        requires(Robot.carriage);

        setTimeout(3);
    }

    @Override
    protected void execute() {
        Robot.collector.move(Collector.IntakeMode.Transfer);
        Robot.carriage.move(Carriage.CarriageMode.Transfer);
    }

    @Override
    protected boolean isFinished() {
        return Robot.carriage.hasCube() || isTimedOut();
    }

    @Override
    protected void end() {
        Robot.collector.move(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

    protected void interrupted() {
        end();
    }
}
