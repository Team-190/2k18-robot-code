package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team190.robot.Robot;

public class ZeroEncoders extends InstantCommand {
    public ZeroEncoders() {
        requires(Robot.drivetrain);
    }

    public void initialize() {
        Robot.drivetrain.setPositionZero();
    }
}
