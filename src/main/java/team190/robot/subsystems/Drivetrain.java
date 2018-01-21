package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import team190.models.PairedTalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Subsystem;
import team190.robot.commands.ControllerDriveCommand;

/**
 *
 */
public class Drivetrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public static final int DEFAULT_TIMEOUT_MS = 10;
	
	public static final int LOW_GEAR_PROFILE = 0;
	public static final int HIGH_GEAR_PROFILE = 1;
	
	public PairedTalonSRX leftPair = new PairedTalonSRX(0, 1);
	public PairedTalonSRX rightPair = new PairedTalonSRX(2, 3);

	public Drivetrain() {
		this.leftPair.setInverted(false);
		this.leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
		this.leftPair.setSensorPhase(false);
		
		this.rightPair.setInverted(true);
		this.rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
		this.rightPair.setSensorPhase(true);

		this.leftPair.setNeutralMode(NeutralMode.Coast);
		this.rightPair.setNeutralMode(NeutralMode.Coast);

		// TODO: put in the actual PID values
		// this.configPIDF(HIGH_GEAR_PROFILE, 0, 0, 0, 0);
	}
	
	public void drive(ControlMode controlMode, double left, double right) {
		this.leftPair.set(controlMode, left);
		this.rightPair.set(controlMode, right);
	}

    public void initDefaultCommand() {
        setDefaultCommand(new ControllerDriveCommand());
    }

    public void configPIDF(int profile, double P, double I, double D, double F) {
	    this.leftPair.configPIDF(profile, DEFAULT_TIMEOUT_MS, P, I, D, F);
	    this.rightPair.configPIDF(profile, DEFAULT_TIMEOUT_MS, P, I, D, F);

    }

	public double getLeftPosition() {
	    return this.leftPair.getSelectedSensorPosition(LOW_GEAR_PROFILE);
    }

    public double getRightPosition() {
	    return this.rightPair.getSelectedSensorPosition(LOW_GEAR_PROFILE);
    }

    public double getLeftVelocity() {
	    return this.leftPair.getSelectedSensorVelocity(LOW_GEAR_PROFILE);
    }

    public double getRightVelocity() {
	    return this.rightPair.getSelectedSensorVelocity(LOW_GEAR_PROFILE);
    }

    public void setPositionZero() {
	    this.leftPair.setSelectedSensorPosition(0, LOW_GEAR_PROFILE, DEFAULT_TIMEOUT_MS);
	    this.rightPair.setSelectedSensorPosition(0, LOW_GEAR_PROFILE, DEFAULT_TIMEOUT_MS);
    }
	

}

