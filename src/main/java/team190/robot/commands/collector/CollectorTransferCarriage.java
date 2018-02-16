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
    }

    @Override
    protected void execute() {
        Robot.collector.intake(Collector.IntakeMode.Transfer);
        Robot.carriage.move(Carriage.CarriageMode.Transfer);
    }

    @Override
    protected boolean isFinished() {
        return Robot.carriage.hasCube();
    }

    @Override
    protected void end() {
        Robot.collector.intake(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

    protected void interrupted() {
        end();
    }
}
