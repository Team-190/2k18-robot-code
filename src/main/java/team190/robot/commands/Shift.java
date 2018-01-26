package team190.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain.Gear;

public class Shift extends Command {
    Gear gear;

    public Shift(Gear gear) {
        gear = this.gear;
        requires(Robot.drivetrain);
    }


    @Override
    protected void initialize() {
        Robot.drivetrain.shift(gear);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

}
