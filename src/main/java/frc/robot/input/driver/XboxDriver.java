package frc.robot.input.driver;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class XboxDriver implements DriverInput {
	private XboxController m_xbox;

	public XboxDriver(int port) {
		m_xbox = new XboxController(port);
	}

	// Drive

	public double[] getDriveThrots() {
		return new double[] { -m_xbox.getY(Hand.kLeft), m_xbox.getX(Hand.kRight) };
	}

	public boolean driveSlow() {
		return m_xbox.getBumper(Hand.kRight);
	}

	public boolean toggleBrakeCoast() {
		return m_xbox.getStickButtonPressed(Hand.kRight);
	}

	public boolean lineUpShot() {
		return false;
	}

	public boolean adjustWithLimelight() {
		return m_xbox.getBumper(Hand.kLeft);
	}

	// Cameras

	public boolean useCamera1() {
		return m_xbox.getAButtonPressed();
	}

	public boolean useCamera2() {
		return m_xbox.getYButtonPressed();
	}
}