package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

public class ZeroGyro extends Command {
    public ZeroGyro() {
        requires(Robot.drivetrain);
    }
    @Override
    protected boolean isFinished() {
        Robot.drivetrain.resetNavx();
        return true;
    }
}
