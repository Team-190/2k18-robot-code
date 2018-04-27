package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

/**
 * Created by Kevin O'Brien on 3/23/2018.
 */
public class CollectorExtakeGround extends Command {
    public CollectorExtakeGround() {
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        setTimeout(3);
    }

    @Override
    protected void execute() {
        if (!isFinished()) {
            Robot.collector.move(Collector.IntakeMode.ExtakeGround);
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
