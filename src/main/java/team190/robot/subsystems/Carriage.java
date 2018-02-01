package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Carriage extends Subsystem {

	public enum CarriageMode {
        Extake, Stop, Transfer
	}

	private static final int CARRIAGE_SRX = 4;

	private TalonSRX left, right;

	public Carriage() {
		left = new TalonSRX(CARRIAGE_SRX);
		left.setInverted(false);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void move(CarriageMode mode) {
		double speed = 0;
		if (mode == CarriageMode.Extake) {
			speed = 1;
		} else if (mode == CarriageMode.Stop) {
			speed = 0;
		} else if (mode == CarriageMode.Transfer) { // TODO the transfer mode might be more complicated
			speed = -0.5;
		}
		
		left.set(ControlMode.PercentOutput, speed);
		right.set(ControlMode.PercentOutput, speed);
	}

	//TODO: implement
	public boolean hasCube() {
		return true;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
