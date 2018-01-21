package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {
	
	// TODO: change exact values
	public final static double GROUND = 0;
	public final static double REST = 20;
	public final static double FIVEFT = 60;
	public final static double SIXFT = 72;
	public final static double SEVENFT = 84;
	public final static double EIGHTFT = 96;

	// TODO: change channels
	private TalonSRX elevator = new TalonSRX(4);
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public Elevator() {
		//TODO: stuffz
	}
	
	public void moveElevator(double height) {
		elevator.set(ControlMode.Position, height);
	}
	
	//TODO: change to velocity?
	public void manualMove(double speed) {
		elevator.set(ControlMode.PercentOutput, speed);
	}
	
	//TODO: fix
	public boolean inPosition() {
		return false;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
