
package team190.robot.commands;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import team190.robot.Robot;
import team190.util.PathfinderTranslator;

import java.io.File;


public class FollowTrajectoryJaci extends Command {

    // periodically tells the SRXs to do the thing
    private class PeriodicRunnable implements Runnable {
        public void run() {
            Robot.drivetrain.processMotionProfilingBuffer();
        }
    }

    // Runs the runnable
    private Notifier SrxNotifier = new Notifier(new PeriodicRunnable());

    public FollowTrajectoryJaci() {
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.prepareMotionProfiling();

        SrxNotifier.startPeriodic(.005);

        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(10, 0, 0)
        };

        PathfinderTranslator path = new PathfinderTranslator(points, Robot.drivetrain.HIGH_GEAR_PROFILE);

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


}