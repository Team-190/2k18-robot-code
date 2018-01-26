package team190.robot.commands;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain.Gear;
import edu.wpi.first.wpilibj.command.Command;

public class Shift extends Command{
	Gear gear;
	public Shift(Gear gear) {
		gear = this.gear;
		requires(Robot.drivetrain);
	}
	
	@Override
	protected void execute () {
		Robot.drivetrain.shift(gear);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
