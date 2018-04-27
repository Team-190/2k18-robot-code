package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.Robot;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionIntake;
import team190.robot.subsystems.Collector;

/**
 * Created by Kevin O'Brien on 3/24/2018.
 */

public class AntiJam extends CommandGroup {
    public AntiJam() {
        addSequential(new ElevatorPositionIntake());
        addSequential(new AntiJamSegment());
        addSequential(new CollectorIntake());
        addSequential(new ElevatorPositionCarriage());
        addSequential(new CollectorTransferCarriage());

    }

    private class AntiJamSegment extends Command {
        AntiJamSegment() {
            requires(Robot.collector);
            requires(Robot.carriage);
            setTimeout(0.3);
        }

        protected void initialize() {

        }

        protected void execute() {
            Robot.collector.antiJam();
        }

        protected boolean isFinished() {
            return isTimedOut();
        }

        protected void end() {
            Robot.collector.move(Collector.IntakeMode.Stop);
        }
    }
}

