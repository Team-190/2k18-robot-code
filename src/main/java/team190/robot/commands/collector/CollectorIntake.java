package team190.robot.commands.collector;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.Robot;
import team190.robot.subsystems.Collector;

/**
 *
 */

public class CollectorIntake extends CommandGroup {
    public CollectorIntake() {
        addSequential(new InitialIntake());
        addSequential(new IntakeAfterSensor());
        //addSequential(new QuickReverse());
    }

    private class InitialIntake extends Command {

        public InitialIntake() {
            // Use requires() here to declare subsystem dependencies
            // eg. requires(chassis);
            requires(Robot.collector);

        }

        @Override
        protected void initialize() {
        }

        @Override
        protected void execute() {
            Robot.collector.move(Collector.IntakeMode.Intake);
        }

        @Override
        protected boolean isFinished() {
            return Robot.collector.hasCube() || Robot.carriage.hasCube();
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

    private class IntakeAfterSensor extends Command {

        public IntakeAfterSensor() {
            // Use requires() here to declare subsystem dependencies
            // eg. requires(chassis);
            requires(Robot.collector);
            setTimeout(0.3);

        }

        @Override
        protected void initialize() {
        }

        @Override
        protected void execute() {
            Robot.collector.move(Collector.IntakeMode.Intake);
        }

        @Override
        protected boolean isFinished() {
            return isTimedOut();
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

}
