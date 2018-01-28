package team190.robot.commands;

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

    private AutoSequence sequence;

    public FollowSequence(AutoSequence seq) {
        requires(Robot.drivetrain);
        sequence = seq;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.prepareMotionProfiling();

        SrxNotifier.startPeriodic(Drivetrain.DOWNLOAD_PERIOD_SEC);

        PathfinderTranslator path = new PathfinderTranslator(sequence, Drivetrain.HIGH_GEAR_PROFILE);

        Robot.drivetrain.fillMotionProfilingBuffer(path.getLeftTrajectoryPoints(), path.getRightTrajectoryPoints());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        SetValueMotionProfile motionProfileValue = Robot.drivetrain.getMotionProfileValue();
        Robot.drivetrain.drive(ControlMode.MotionProfile, motionProfileValue.value, motionProfileValue.value);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.drivetrain.getMotionProfileValue() == SetValueMotionProfile.Hold;
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