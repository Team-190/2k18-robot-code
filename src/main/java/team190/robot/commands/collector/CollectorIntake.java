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
    }

    private class InitialIntake extends Command {

        InitialIntake() {
            // Use requires() here to declare subsystem dependencies
            // eg. requires(chassis);
            requires(Robot.collector);
        }

        @Override
        protected void execute() {
            if (!Robot.collector.isJammed()) {
                Robot.collector.move(Collector.IntakeMode.Intake);
            } else {
                Robot.collector.antiJam();
            }
        }

        @Override
        protected boolean isFinished() {
            return Robot.collector.hasCube() && !Robot.collector.isJammed();
        }

        @Override
        protected void end() {
            Robot.collector.move(Collector.IntakeMode.Stop);
        }

    }

    private class IntakeAfterSensor extends Command {

        IntakeAfterSensor() {
            // Use requires() here to declare subsystem dependencies
            // eg. requires(chassis);
            requires(Robot.collector);
            setTimeout(0.3);
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

    }

}
