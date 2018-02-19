package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain.Gear;

public class Shift extends InstantCommand {
    private Gear gear;

    public Shift(Gear gear) {
        this.gear = gear;
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.shift(gear);
    }
}
