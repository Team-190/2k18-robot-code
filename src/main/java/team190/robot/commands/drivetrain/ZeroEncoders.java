package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

/**
 * Created by Kevin O'Brien on 1/28/2018.
 */
public class ZeroEncoders extends Command {

    public ZeroEncoders() {
        requires(Robot.drivetrain);

    }

    public void initialize() {
        Robot.drivetrain.setPositionZero();
    }

    public boolean isFinished() {
        return true;
    }
}
