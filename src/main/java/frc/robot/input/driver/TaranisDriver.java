package frc.robot.input.driver;

import edu.wpi.first.wpilibj.Joystick;

public class TaranisDriver implements DriverInput {
	private Joystick m_taranis;

	public TaranisDriver(int port) {
		m_taranis = new Joystick(port);
	}

	// Drive
	public double[] getDriveThrots() {
		return new double[] { m_taranis.getRawAxis(0), m_taranis.getRawAxis(1) };
	}

	public boolean driveSlow() {
		return switch2Pos(3);
	}

	public boolean toggleBrakeCoast() {
		return false;
	}

	public boolean lineUpShot() {
		return switch2Pos(2);
	}

	public boolean adjustWithLimelight() {
		return false;
	}

	// ColorSpinner
	public boolean startRotationControl() {
		return false;
	}

	public boolean startPositionControl() {
		return false;
	}

	// Lift
	public double hookLiftSpeed() {
		return -switch3(4);
	}

	public boolean loosenWinch() {
		return switch3(5) == 1;
	}

	public boolean tightenWinch() {
		return switch3(5) == -1;
	}

	public boolean resetHookEncoder() {
		return false;
	}

	public boolean useCamera1() {
		return switch3(6) == 1;
	}

	public boolean useCamera2() {
		return switch3(6) == -1;
	}

	private boolean switch2Pos(int channel) {
		return m_taranis.getRawAxis(channel) > 0;
	}

	private int switch3(int channel) {
		return (int) m_taranis.getRawAxis(channel);
	}
}