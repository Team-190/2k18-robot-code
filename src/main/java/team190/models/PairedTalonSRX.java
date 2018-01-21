package team190.models;

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

}
