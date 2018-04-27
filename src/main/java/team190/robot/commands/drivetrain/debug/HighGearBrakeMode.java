package team190.robot.commands.drivetrain.debug;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

/**
 * Created by Kevin O'Brien on 2/19/2018.
 */
public class HighGearBrakeMode extends InstantCommand {
    public HighGearBrakeMode() {
        requires(Robot.drivetrain);
    }

    public void initialize() {
        Robot.drivetrain.setBrakeMode();
        Robot.drivetrain.shift(Drivetrain.Gear.HIGH);
    }
}
