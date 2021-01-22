package frc.robot.input.driver;

public interface DriverInput {
	// Drive
	public double[] getDriveThrots();

	public boolean driveSlow();

	public boolean toggleBrakeCoast();

	public boolean lineUpShot();

	public boolean adjustWithLimelight();

	// Cameras

	public boolean useCamera1();

	public boolean useCamera2();
}