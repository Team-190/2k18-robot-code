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
    private Notifier SrxNotifier;


    private MotionProfileStatus rightStatus;
    private MotionProfileStatus leftStatus;

    private AutoSequence sequence;

    private int _state;

    private SetValueMotionProfile _setValue;



    public FollowSequence(AutoSequence seq) {
        requires(Robot.drivetrain);
        sequence = seq;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        SrxNotifier = new Notifier(new PeriodicRunnable());
        rightStatus = new MotionProfileStatus();
        leftStatus = new MotionProfileStatus();
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
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
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