package team190.robot.subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import jaci.pathfinder.Trajectory;
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
	
	public static final int DEFAULT_TIMEOUT_MS = 0;
    public static final int DEFAULT_PIDX = 0;

	public static final int LOW_GEAR_PROFILE = 0;
	public static final int HIGH_GEAR_PROFILE = 1;
	
	public PairedTalonSRX leftPair = new PairedTalonSRX(0, 1);
	public PairedTalonSRX rightPair = new PairedTalonSRX(2, 3);

	// Motion Profiling
    public static final int kMinPointsInTalon = 5;
    private SetValueMotionProfile motionProfileValue = SetValueMotionProfile.Disable;
    private MotionProfileStatus rightStatus = new MotionProfileStatus();
    private MotionProfileStatus leftStatus = new MotionProfileStatus();

	public Drivetrain() {
		this.leftPair.setInverted(false);
		this.leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
		this.leftPair.setSensorPhase(false);
		
		this.rightPair.setInverted(true);
		this.rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
		this.rightPair.setSensorPhase(true);

		this.leftPair.setNeutralMode(NeutralMode.Coast);
		this.rightPair.setNeutralMode(NeutralMode.Coast);

		// TODO: put in the actual PIDF values
        // this.configPIDF(LOW_GEAR_PROFILE, 0, 0, 0, 0);
		// this.configPIDF(HIGH_GEAR_PROFILE, 0, 0, 0, 0);

		this.setPositionZero();
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
	    return this.leftPair.getSelectedSensorPosition(DEFAULT_PIDX);
    }

    public double getRightPosition() {
	    return this.rightPair.getSelectedSensorPosition(DEFAULT_PIDX);
    }

    public double getLeftVelocity() {
	    return this.leftPair.getSelectedSensorVelocity(DEFAULT_PIDX);
    }

    public double getRightVelocity() {
	    return this.rightPair.getSelectedSensorVelocity(DEFAULT_PIDX);
    }

    public void setPositionZero() {
	    this.leftPair.setSelectedSensorPosition(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
	    this.rightPair.setSelectedSensorPosition(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
    }


    // MOTION PROFILING METHODS


    // called before entering motion profiling
	public void prepareMotionProfiling() {
		// TODO: shift high gear
        motionProfileValue = SetValueMotionProfile.Disable;
        leftPair.setNeutralMode(NeutralMode.Brake);
        rightPair.setNeutralMode(NeutralMode.Brake);
        leftPair.clearMotionProfileTrajectories();
        rightPair.clearMotionProfileTrajectories();
        leftPair.changeMotionControlFramePeriod(5);
        rightPair.changeMotionControlFramePeriod(5);
        drive(ControlMode.MotionProfile, motionProfileValue.value, motionProfileValue.value);
	}

	// take your trajectory and stream it the srx's
    public void fillMotionProfilingBuffer(Trajectory left, Trajectory right) {
        processTrajectory(leftPair, HIGH_GEAR_PROFILE, left);
        processTrajectory(rightPair, HIGH_GEAR_PROFILE, right);
    }

    private void processTrajectory(PairedTalonSRX pair, int pidfSlot, Trajectory trajectory) {
        for (int i = 0; i < trajectory.length(); i++) {
            boolean zeroPos = (i == 0);
            boolean isLastPoint = ((i+i) == trajectory.length());
            TrajectoryPoint point = processSegment(trajectory.get(i), pidfSlot, zeroPos, isLastPoint);
            pair.pushMotionProfileTrajectory(point);
        }
    }

    private TrajectoryPoint processSegment(Trajectory.Segment seg, int pidfSlot, boolean zeroPos, boolean isLastPoint) {
        TrajectoryPoint point = new TrajectoryPoint();
        point.position = seg.position;
        point.velocity = seg.velocity;
        point.headingDeg = seg.heading;
        point.profileSlotSelect0 = pidfSlot;
        point.profileSlotSelect1 = pidfSlot;
        point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_10ms;
        point.zeroPos = zeroPos;
        point.isLastPoint = isLastPoint;

        return point;
    }

    public SetValueMotionProfile getMotionProfileValue() {
	    leftPair.getMotionProfileStatus(leftStatus);
	    rightPair.getMotionProfileStatus(rightStatus);
	    if (leftStatus.isUnderrun || rightStatus.isUnderrun)
	        return SetValueMotionProfile.Disable;
	    else if (leftStatus.btmBufferCnt > kMinPointsInTalon && rightStatus.btmBufferCnt > kMinPointsInTalon)
	        return SetValueMotionProfile.Enable;
	    else if (leftStatus.activePointValid && leftStatus.isLast && rightStatus.activePointValid && rightStatus.isLast)
	        return SetValueMotionProfile.Hold;
	    else
	        return SetValueMotionProfile.Disable;
    }

    public void processMotionProfilingBuffer() {
	    leftPair.processMotionProfileBuffer();
	    rightPair.processMotionProfileBuffer();
    }

	// clean up all the motion profiling things
	public void endMotionProfiling() {
	    // TODO: Shift low gear
        leftPair.clearMotionProfileTrajectories();
        rightPair.clearMotionProfileTrajectories();
        leftPair.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        rightPair.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        leftPair.set(ControlMode.PercentOutput, 0);
        rightPair.set(ControlMode.PercentOutput, 0);
        leftPair.setNeutralMode(NeutralMode.Coast);
        rightPair.setNeutralMode(NeutralMode.Coast);
    }



}

