package team190.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.TimedCommand;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain.Gear;

public class Shift extends TimedCommand {
    private Gear gear;

    public Shift(Gear gear) {
        super(0.1);
        this.gear = gear;
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.shift(gear);
    }
}
