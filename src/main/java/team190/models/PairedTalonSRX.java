package team190.models;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class PairedTalonSRX extends TalonSRX {
	
	private final TalonSRX follower;

	public PairedTalonSRX(int leaderDeviceNumber, int followerDeviceNumber) {
		super(leaderDeviceNumber);
		
		follower = new TalonSRX(followerDeviceNumber);
		follower.follow(this);
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

	public ErrorCode configPIDF(int slotIdx, int timeout, double P, double I, double D, double F) {
	    ErrorCode errorCode = ErrorCode.OK;

	    errorCode = config_kP(slotIdx, P, timeout);
	    if (errorCode != ErrorCode.OK) { return errorCode; }

	    errorCode = config_kI(slotIdx, I, timeout);
	    if (errorCode != ErrorCode.OK) { return errorCode; }

	    errorCode = config_kD(slotIdx, D, timeout);
	    if (errorCode != ErrorCode.OK) { return errorCode; }

	    errorCode = config_kF(slotIdx, F, timeout);
	    if (errorCode != ErrorCode.OK) { return errorCode; }

	    return errorCode;

    }




}
