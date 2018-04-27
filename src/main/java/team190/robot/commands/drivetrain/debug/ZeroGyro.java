package team190.robot.commands.drivetrain.debug;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team190.robot.Robot;

public class ZeroGyro extends InstantCommand {
    public ZeroGyro() {
        requires(Robot.drivetrain);
    }

    public void initialize() {
        Robot.drivetrain.resetNavx();
    }
}
