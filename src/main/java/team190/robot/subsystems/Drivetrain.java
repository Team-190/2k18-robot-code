package team190.robot.subsystems;

import team190.models.PairedTalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private static final int DEFAULT_TIMEOUT_MS = 10;
	
	public static final int LOW_GEAR_PROFILE = 0;
	public static final int HIGH_GEAR_PROFILE = 1;
	
	private PairedTalonSRX leftPair = new PairedTalonSRX(0, 1);
	private PairedTalonSRX rightPair = new PairedTalonSRX(2, 3);

	public Drivetrain() {
		this.leftPair.setInverted(false);
		this.leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
		this.leftPair.setSensorPhase(false);
		
		this.rightPair.setInverted(true);
		this.rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
		this.rightPair.setSensorPhase(true);
	}
	
	public void drive(ControlMode controlMode, double left, double right) {
		this.leftPair.set(controlMode, left);
		this.rightPair.set(controlMode, right);
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

