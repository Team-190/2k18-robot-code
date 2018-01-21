package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Carriage extends Subsystem {

	public static enum CarriageMode {
		Place, Stop, Transfer
	}

	// TODO: change channels
	private TalonSRX left = new TalonSRX(6);
	private TalonSRX right = new TalonSRX(7);

	public Carriage() {
		left.setInverted(false);
		right.setInverted(true);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void move(CarriageMode mode) {
		double speed = 0;
		if (mode == CarriageMode.Place) {
			speed = 1;
		} else if (mode == CarriageMode.Stop) {
			speed = 0;
		} else if (mode == CarriageMode.Transfer) {
			speed = -0.5;
		}
		
		left.set(ControlMode.PercentOutput, speed);
		right.set(ControlMode.PercentOutput, speed);
	}
	
	
	//TODO: implement
	public boolean inCarriage() {
		return true;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
