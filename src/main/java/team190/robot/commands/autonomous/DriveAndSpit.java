package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.Robot;
import team190.robot.commands.collector.CollectorExtakeRear;

/**
 * Created by Kevin O'Brien on 3/17/2018.
 */
public class DriveAndSpit extends CommandGroup {
    public DriveAndSpit() {
        addSequential(new DriveForward(Robot.TIME_CROSS_LINE));
        addSequential(new CollectorExtakeRear());
    }
}
