package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.input.ControllerInput;

public class Limelight {
	// private ControllerInput m_controller;
	private final double ANGLE_OFFSET = -1.5;

	public Limelight(ControllerInput controller) {
		// m_controller = controller;
	}

	public double getShift() {
		NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
		double xAngle = table.getEntry("tx").getDouble(0); // -27 to 27 degrees
		xAngle += ANGLE_OFFSET;

		xAngle /= 70;

		return xAngle;
	}
}