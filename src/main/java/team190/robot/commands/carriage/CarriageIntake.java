package team190.robot.commands.carriage;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;

/**
 * Created by Kevin O'Brien on 2/19/2018.
 */
public class CarriageIntake extends Command {

    public CarriageIntake() {
        requires(Robot.carriage);
    }

    @Override
    public void initialize() {
        setTimeout(4);
    }

    @Override
    public void execute() {
        Robot.carriage.move(Carriage.CarriageMode.Intake);
    }

    @Override
    protected boolean isFinished() {
        return Robot.carriage.hasCube() || isTimedOut();
    }

    @Override
    protected void end() {
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

    @Override
    protected void interrupted() {
        end();
    }

}

