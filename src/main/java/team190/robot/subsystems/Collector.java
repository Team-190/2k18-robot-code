package org.usfirst.frc.team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Collector extends Subsystem {

	public static enum IntakeMode {
		Stop, Intake, Extake, Transfer
	}
	
	//TODO: change channels
	private TalonSRX left = new TalonSRX(5);
	private TalonSRX right = new TalonSRX(6);
	
	private DigitalInput sensor = new DigitalInput(1);

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	//TODO: switch?
	public Collector() {
		left.setInverted(false);
		right.setInverted(true);
	}

	//TODO: change to speed mode?
	//TODO: change actual speeds
	public void intake(IntakeMode mode) {
		double speed = 0;
		if (mode == IntakeMode.Stop) {
			speed = 0.0;
		} else if (mode == IntakeMode.Intake) {
			speed = 0.5;
		} else if (mode == IntakeMode.Extake) {
			speed = -0.2;
		} else if (mode == IntakeMode.Transfer) {
			speed = 0.1;
		}

		left.set(ControlMode.PercentOutput, speed);
		right.set(ControlMode.PercentOutput, speed);
	}

	public boolean cubeGrabbed() {
		return sensor.get();
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
