package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

/**
 * Created by Kevin O'Brien on 2/19/2018.
 */
public class LowGearCoastMode extends InstantCommand {
    public LowGearCoastMode() {
        requires(Robot.drivetrain);
    }

    public void initialize() {
        Robot.drivetrain.setCoastMode();
        Robot.drivetrain.shift(Drivetrain.Gear.LOW);
    }
}
