package team190.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Waypoint;
import team190.models.AutoSequence;
import team190.models.PairedTrajectoryPoints;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;
import team190.util.PathfinderTranslator;


public class FollowSequence extends Command {

    // Runs the runnable
    private Notifier SrxNotifier = new Notifier(new PeriodicRunnable());


    private MotionProfileStatus rightStatus = new MotionProfileStatus();
    private MotionProfileStatus leftStatus = new MotionProfileStatus();

    private AutoSequence sequence;

    private int _state;

    private SetValueMotionProfile _setValue;



    public FollowSequence(AutoSequence seq) {
        requires(Robot.drivetrain);
        sequence = seq;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        _state = 1;
        _setValue = SetValueMotionProfile.Disable;
        Robot.drivetrain.prepareMotionProfiling();

        SrxNotifier.startPeriodic(Drivetrain.DOWNLOAD_PERIOD_SEC);

       /* Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(10, 0, 0)
        };*/


        //PathfinderTranslator path = new PathfinderTranslator(sequence, Drivetrain.HIGH_GEAR_PROFILE);

        //PathfinderTranslator path = new PathfinderTranslator(points, Drivetrain.HIGH_GEAR_PROFILE);

        //PathfinderTranslator path = new PathfinderTranslator(sequence, Drivetrain.HIGH_GEAR_PROFILE);

        //Robot.drivetrain.fillMotionProfilingBuffer(path.getLeftTrajectoryPoints(), path.getRightTrajectoryPoints());
        PairedTrajectoryPoints pairPoints = sequence.getPairedTrajectoryPoints();

        Robot.drivetrain.fillMotionProfilingBuffer(pairPoints.left, pairPoints.right);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        switch (_state) {
            case 1: /*
						 * wait for MP to stream to Talon, really just the first few
						 * points
						 */
					/* do we have a minimum numberof points in Talon */
                if (leftStatus.btmBufferCnt > 5.0 && rightStatus.btmBufferCnt > 5.0) {
						/* start (once) the motion profile */
                    _setValue = SetValueMotionProfile.Enable;
						/* MP will start once the control frame gets scheduled */
                    _state = 2;
                }
                break;
            case 2: /* check the status of the MP */
					/*
					 * if talon is reporting things are good, keep adding to our
					 * timeout. Really this is so that you can unplug your talon in
					 * the middle of an MP and react to it.
					 */
					/*
					 * If we are executing an MP and the MP finished, start loading
					 * another. We will go into hold state so robot servo's
					 * position.
					 */
                if (leftStatus.activePointValid && leftStatus.isLast && rightStatus.activePointValid && rightStatus.isLast) {
						/*
						 * because we set the last point's isLast to true, we will
						 * get here when the MP is done
						 */
                    _setValue = SetValueMotionProfile.Hold;
                    _state = 0;
                }
                break;
        }

			/* Get the motion profile status every loop */
        Robot.drivetrain.leftPair.getMotionProfileStatus(leftStatus);
        Robot.drivetrain.rightPair.getMotionProfileStatus(rightStatus);

        Robot.drivetrain.drive(ControlMode.MotionProfile, _setValue.value, _setValue.value);

        /*

        if (rightStatus.isUnderrun || leftStatus.isUnderrun)
        {
            // if either MP has underrun, stop both
            System.out.println("Motion profile has underrun!");
            setValue = SetValueMotionProfile.Disable;
        }
        else if (rightStatus.btmBufferCnt > 5.0 && leftStatus.btmBufferCnt > 5.0)
        {
            // if we have enough points in the talon, go.
            setValue = SetValueMotionProfile.Enable;
        }
        else if (leftStatus.topBufferCnt == 0 && rightStatus.topBufferCnt == 0 && leftStatus.btmBufferCnt == 0 && rightStatus.btmBufferCnt == 0)
        {
            finished = true;
            DriverStation.reportWarning("We have reached hold status", false);
            // if both profiles are at their last points, hold the last point
            setValue = SetValueMotionProfile.Hold;
        }
        System.out.println("Left Top Buffer: " + leftStatus.topBufferCnt);
        System.out.println("Right Top Buffer: " + rightStatus.topBufferCnt);
        System.out.println("Left Bottom Buffer: " + leftStatus.btmBufferCnt);
        System.out.println("Right Bottom Buffer: " + rightStatus.btmBufferCnt);
        System.out.println("Left velocity of active: " + Robot.drivetrain.leftPair.getActiveTrajectoryVelocity());
        System.out.println("Right velocity of active: " + Robot.drivetrain.rightPair.getActiveTrajectoryVelocity());
        System.out.println("Left active: " + leftStatus.activePointValid);
        System.out.println("Right active: " + rightStatus.activePointValid);
        Robot.drivetrain.drive(ControlMode.MotionProfile, setValue.value, setValue.value);*/
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        /*
        System.out.println("leftStatus = " + leftStatus.isLast);
        System.out.println("rightStatus = " + rightStatus.isLast);
        System.out.println("leftStatus valid = " + leftStatus.activePointValid);
        System.out.println("rightStatus valid = " + rightStatus.activePointValid);
        boolean leftComplete = leftStatus.activePointValid && leftStatus.isLast;
        boolean rightComplete = rightStatus.activePointValid && rightStatus.isLast;
        boolean trajectoryComplete = leftComplete && rightComplete;
        return trajectoryComplete;
        */
        return _state == 0;
    }

    // Called once after isFinished returns true
    protected void end() {
        SrxNotifier.stop();
        Robot.drivetrain.endMotionProfiling();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

    // periodically tells the SRXs to do the thing
    private class PeriodicRunnable implements Runnable {
        public void run() {
            Robot.drivetrain.processMotionProfilingBuffer();
        }
    }


}