package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {

    // CAN Channels
	private static final int ELEVATOR_SRX_MAIN = 4,
                             ELEVATOR_SRX_FOLLOWER = 5;
	
	// TODO: change exact values
	public final static double GROUND = 0;
	public final static double REST = 20;
	public final static double FIVEFT = 60;
	public final static double SIXFT = 72;
	public final static double SEVENFT = 84;
	public final static double MAX = 96;

	// TODO: change channels
	private TalonSRX mainMotor, followerMotor;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public Elevator() {
		 mainMotor = new TalonSRX(ELEVATOR_SRX_MAIN);
		 mainMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0);

		 // TODO more srx setup here

		 followerMotor = new TalonSRX(ELEVATOR_SRX_FOLLOWER);
		 followerMotor.follow(mainMotor);
	}
	
	public void moveElevator(double height) {
		mainMotor.set(ControlMode.Position, height);
	}
	
	public void manualMove(double percent) {
        mainMotor.set(ControlMode.PercentOutput, percent);
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
