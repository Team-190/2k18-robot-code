/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team190.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;
import team190.robot.commands.DelayedCommand;
import team190.robot.commands.autonomous.*;
import team190.robot.commands.drivetrain.DriveForTimeAndSpeed;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.drivetrain.debug.HighGearBrakeMode;
import team190.robot.commands.drivetrain.debug.LowGearCoastMode;
import team190.robot.commands.drivetrain.debug.ZeroEncoders;
import team190.robot.commands.drivetrain.debug.ZeroGyro;
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

    public static final double TIME_CROSS_LINE = 3.5;
    public static Drivetrain drivetrain;
    public static Collector collector;
    public static Elevator elevator;
    public static Carriage carriage;
    public static OI m_oi;
    private Command m_autonomousCommand;
    private SendableChooser<AutoMode> m_autonomousChooser = new SendableChooser<>();
    private double m_autonomousDelay;
    private SendableChooser<Double> m_delayChooser = new SendableChooser<>();
    private MatchData.OwnedSide position;
    private SendableChooser<MatchData.OwnedSide> m_startSide = new SendableChooser<>();

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

        SmartDashboard.putData("Drivetrain", drivetrain);

        m_autonomousChooser.addDefault("Do Nothing", null);
        for (AutoMode auto : AutoMode.values()) {
            m_autonomousChooser.addObject(auto.prettyName, auto);
        }

        SmartDashboard.putData("Auto mode", m_autonomousChooser);

        // Allow for the delay of the start of autnonmous by 0 to 5 seconds
        m_delayChooser.addDefault("No Delay", 0.0);
        m_delayChooser.addObject("1 Sec", 1.0);
        m_delayChooser.addObject("2 Sec", 2.0);
        m_delayChooser.addObject("3 Sec", 3.0);
        m_delayChooser.addObject("4 Sec", 4.0);
        m_delayChooser.addObject("5 Sec", 5.0);
        SmartDashboard.putData("Auto delay", m_delayChooser);

        m_startSide.addDefault("Start Right", MatchData.OwnedSide.RIGHT);
        m_startSide.addObject("Start Left", MatchData.OwnedSide.LEFT);
        SmartDashboard.putData("Start Position", m_startSide);


        // Debug commands
        debugSmartDashboard();
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
        drivetrain.shift(Drivetrain.Gear.LOW);
        AutoMode autoMode = m_autonomousChooser.getSelected();
        m_autonomousDelay = m_delayChooser.getSelected();
        position = m_startSide.getSelected();

        // schedule the autonomous command (example)
        if (autoMode != null) {
            m_autonomousCommand = new DelayedCommand(m_autonomousDelay, autoMode.getCommand(position));
            m_autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

        // Print debug values to smart dashboard
        debugLogValues();
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

        // Print debug values to smart dashboard
        debugLogValues();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    private void debugLogValues() {
        drivetrain.log();
        elevator.log();
        carriage.log();
        collector.log();
    }

    /**
     * Commands written to the dashboard for debugging used in robotInit
     */
    private void debugSmartDashboard() {
        SmartDashboard.putData("Zero Encoders", new ZeroEncoders());
        SmartDashboard.putData("Zero Gyro", new ZeroGyro());
        SmartDashboard.putData("Start Right Scale", new DriveSequence(AutoSequence.StartRightScaleLeft));
        SmartDashboard.putData("Left Scale Sequence", new ScaleEitherPlate.ScaleCrossover());
        SmartDashboard.putData("HighGearBrake", new HighGearBrakeMode());
        SmartDashboard.putData("LowGearCoast", new LowGearCoastMode());
    }

    private enum AutoMode {
        DRIVE_FORWARD("Drive Forward"),
        SWITCH_OR_DRIVE("Switch or Drive Forward"),
        SWITCH_CENTER("Switch Center"),
        SCALE_OR_DRIVE("Scale, Switch, or Drive Forward"),
        SCALE_EITHER("Scale (Either Plate)"),
        //SCALE_LEFT_START_RIGHT("DONT USE (Probably) Scale Left"),
        //SCALE_RIGHT_START_RIGHT("DONT USE Scale Right"),
        SWITCH_SCORE("In Front of Switch Score");


        private final String prettyName;

        AutoMode(final String prettyName) {
            this.prettyName = prettyName;
        }

        public Command getCommand(MatchData.OwnedSide position) {
            switch (this) {
                case DRIVE_FORWARD:
                    return new DriveForTimeAndSpeed(TIME_CROSS_LINE);
                case SWITCH_OR_DRIVE:
                    return new SwitchOrDriveForward(position);
                case SWITCH_CENTER:
                    return new CenterSwitch();
                case SCALE_OR_DRIVE:
                    return new ScaleSwitchOrDriveForward(position);
                case SCALE_EITHER:
                    return new ScaleEitherPlate();
                case SWITCH_SCORE:
                    return new InFrontOfSwitch(position);
                /*case SCALE_LEFT_START_RIGHT:
                    return new ScaleEitherPlate.ScaleCrossover();
                case SCALE_RIGHT_START_RIGHT:
                    return new ScaleSwitchOrDriveForward.ScoreScale();*/
                default:
                    return new InstantCommand();
            }
        }
    }
}
