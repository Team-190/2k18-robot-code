package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

/**
 * Created by Kevin O'Brien on 2/14/2018.
 */
public class ZeroGyro extends Command {
    @Override
    protected boolean isFinished() {
        Robot.navx.reset();
        return true;
    }
}
