package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

public class CollectorExtakeFront extends Command {

    public CollectorExtakeFront() {
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        setTimeout(3); // TODO find actual timeout for extake front
    }

    @Override
    protected void execute() {
        if (!isFinished()) {
            Robot.collector.move(Collector.IntakeMode.Extake);
            Robot.carriage.move(Carriage.CarriageMode.Intake);
        }
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void end() {
        Robot.collector.move(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }
}
