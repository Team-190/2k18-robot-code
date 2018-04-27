package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

/**
 * Created by Kevin O'Brien on 3/27/2018.
 */
public class CollectorCarriageManualMove extends Command {

    private final Collector.IntakeMode collectorMode;
    private final Carriage.CarriageMode carriageMode;

    public CollectorCarriageManualMove(Collector.IntakeMode collectorMode, Carriage.CarriageMode carriageMode) {
        this.collectorMode = collectorMode;
        this.carriageMode = carriageMode;
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.collector.move(collectorMode);
        Robot.carriage.move(carriageMode);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.collector.move(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }
}
