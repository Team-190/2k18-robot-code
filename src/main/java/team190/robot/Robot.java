/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team190.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team190.models.AutoSequence;
import team190.robot.commands.autonomous.StartRightScaleLeft;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.drivetrain.ZeroEncoders;
import team190.robot.commands.drivetrain.ZeroGyro;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
import team190.robot.subsystems.Drivetrain;
import team190.robot.subsystems.Elevator;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

    public static Drivetrain drivetrain;
    public static Collector collector;
    public static Elevator elevator;
    public static Carriage carriage;
    public static OI m_oi;
    public static AHRS navx;

    private Command m_autonomousCommand;
    private SendableChooser<Command> m_chooser = new SendableChooser<>();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        collector = new Collector();
        elevator = new Elevator();
        carriage = new Carriage();
        m_oi = new OI();
        navx = new AHRS(SPI.Port.kMXP);

        SmartDashboard.putData("Drivetrain", drivetrain);
        drivetrain.shift(Drivetrain.Gear.HIGH);

        m_chooser.addDefault("Default Auto", null);
        m_chooser.addObject("Auto Scale", new StartRightScaleLeft());

        SmartDashboard.putData("Auto mode", m_chooser);

        SmartDashboard.putData("Zero Encoders", new ZeroEncoders());
        SmartDashboard.putData("Zero Gyro", new ZeroGyro());

        SmartDashboard.putData("Start Right Scale", new DriveSequence(AutoSequence.StartRightScaleLeft));
        SmartDashboard.putData("Get First Cube", new DriveSequence(AutoSequence.ScaleLeftCollectCubeOne));
        SmartDashboard.putData("Place First Cube", new DriveSequence(AutoSequence.ScaleLeftPlaceCubeOne));
        SmartDashboard.putData("Left Scale Sequence", new StartRightScaleLeft());
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     * <p>
     * <p>You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {
        drivetrain.setBrakeMode();
        m_autonomousCommand = m_chooser.getSelected();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
        drivetrain.shift(Drivetrain.Gear.LOW);
        drivetrain.setCoastMode();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
