package frc.robot.input.driver;

public interface DriverInput {
	// Drive
	public double[] getDriveThrots();

	public boolean driveSlow();

	public boolean toggleBrakeCoast();

	public boolean lineUpShot();

	public boolean adjustWithLimelight();

	// ColorSpinner
	public boolean startRotationControl();

	public boolean startPositionControl();

	// Lift
	public double hookLiftSpeed();

	public boolean loosenWinch();

	public boolean tightenWinch();

	public boolean resetHookEncoder();

	public boolean useCamera1();

	public boolean useCamera2();
}