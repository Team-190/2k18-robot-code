package team190.models;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Sendable;
import team190.robot.subsystems.Drivetrain;

public class PairedTalonSRX extends WPI_TalonSRX {

    private final TalonSRX follower;

    public PairedTalonSRX(int leaderDeviceNumber, int followerDeviceNumber) {
        super(leaderDeviceNumber);

        follower = new TalonSRX(followerDeviceNumber);
        follower.follow(this);

        configNominalOutputForward(0, Drivetrain.DEFAULT_TIMEOUT_MS);
        configNominalOutputReverse(0, Drivetrain.DEFAULT_TIMEOUT_MS);
    }

    @Override
    public void setNeutralMode(NeutralMode neutralMode) {
        super.setNeutralMode(neutralMode);
        follower.setNeutralMode(neutralMode);
    }

    @Override
    public void setInverted(boolean invert) {
        super.setInverted(invert);
        follower.setInverted(invert);
    }

    public void configPIDF(int slotIdx, int timeout, double P, double I, double D, double F) {
        config_kP(slotIdx, P, timeout);
        config_kI(slotIdx, I, timeout);
        config_kD(slotIdx, D, timeout);
        config_kF(slotIdx, F, timeout);
    }


}
