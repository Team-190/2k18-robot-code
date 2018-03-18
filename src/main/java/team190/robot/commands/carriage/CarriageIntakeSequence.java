package team190.robot.commands.carriage;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

/**
 * Created by Kevin O'Brien on 3/17/2018.
 */
public class CarriageIntakeSequence extends CommandGroup {
    public CarriageIntakeSequence() {
        addSequential(new CarriageIntake());
        addSequential(new CarriageTimedIntake());
    }
    private class CarriageTimedIntake extends Command {

        public CarriageTimedIntake() {
            setTimeout(0.1);
        }
        @Override
        protected boolean isFinished() {
            return isTimedOut();
        }
        @Override
        protected void execute() {
            Robot.carriage.move(Carriage.CarriageMode.Intake);
        }
        protected void end() {
            Robot.carriage.move(Carriage.CarriageMode.Stop);
        }

    }
}
