package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

/**
 * Created by Kevin O'Brien on 1/31/2018.
 */
public class FullSteamAhead extends Command {
    public FullSteamAhead() {
        requires(Robot.drivetrain);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    protected void execute() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, 1, 1);
    }
}
