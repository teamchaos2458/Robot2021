package frc.robot.input.driver;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickDriver implements DriverInput {
	private Joystick m_driveJoy;

	public JoystickDriver(int port) {
		m_driveJoy = new Joystick(port);
	}

	// Drive
	public double[] getDriveThrots() {
		return new double[] { -m_driveJoy.getY(), m_driveJoy.getTwist() };
	}

	public boolean driveSlow() {
		return m_driveJoy.getTrigger();
	}

	public boolean toggleBrakeCoast() {
		return false;
	}

	public boolean lineUpShot() {
		return false;
	}

	public boolean adjustWithLimelight() {
		return false;
	};

	// ColorSpinner
	public boolean startRotationControl() {
		return false;
	}

	public boolean startPositionControl() {
		return false;
	}

	// Lift
	public double hookLiftSpeed() {
		return 0;
	}

	public boolean loosenWinch() {
		return m_driveJoy.getPOV() == 180;
	}

	public boolean tightenWinch() {
		return m_driveJoy.getPOV() == 0;
	}

	public boolean resetHookEncoder() {
		return false;
	}

	public boolean useCamera1() {
		return false;
	}

	public boolean useCamera2() {
		return false;
	}
}