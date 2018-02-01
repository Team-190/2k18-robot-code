package team190.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Waypoint;
import team190.models.AutoSequence;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;
import team190.util.PathfinderTranslator;


public class FollowSequence extends Command {

    // Runs the runnable
    private Notifier SrxNotifier = new Notifier(new PeriodicRunnable());


    private MotionProfileStatus rightStatus = new MotionProfileStatus();
    private MotionProfileStatus leftStatus = new MotionProfileStatus();

    private AutoSequence sequence;

    public FollowSequence(AutoSequence seq) {
        requires(Robot.drivetrain);
        sequence = seq;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.prepareMotionProfiling();

        SrxNotifier.startPeriodic(Drivetrain.DOWNLOAD_PERIOD_SEC);

       /* Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(10, 0, 0)
        };*/


        //PathfinderTranslator path = new PathfinderTranslator(sequence, Drivetrain.HIGH_GEAR_PROFILE);

        //PathfinderTranslator path = new PathfinderTranslator(points, Drivetrain.HIGH_GEAR_PROFILE);

        PathfinderTranslator path = new PathfinderTranslator(sequence, Drivetrain.HIGH_GEAR_PROFILE);

        Robot.drivetrain.fillMotionProfilingBuffer(path.getLeftTrajectoryPoints(), path.getRightTrajectoryPoints());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.drivetrain.leftPair.getMotionProfileStatus(leftStatus);
        Robot.drivetrain.rightPair.getMotionProfileStatus(rightStatus);

        SetValueMotionProfile setValue = SetValueMotionProfile.Disable;

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
        else if (rightStatus.activePointValid && rightStatus.isLast && leftStatus.activePointValid
                && leftStatus.isLast)
        {
            // if both profiles are at their last points, hold the last point
            setValue = SetValueMotionProfile.Hold;
        }

        Robot.drivetrain.drive(ControlMode.MotionProfile, setValue.value, setValue.value);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        System.out.println("leftStatus = " + leftStatus.isLast);
        System.out.println("rightStatus = " + rightStatus.isLast);
        boolean leftComplete = leftStatus.activePointValid && leftStatus.isLast;
        boolean rightComplete = rightStatus.activePointValid && rightStatus.isLast;
        boolean trajectoryComplete = leftComplete && rightComplete;
        return trajectoryComplete;

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