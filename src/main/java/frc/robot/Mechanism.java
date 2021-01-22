package frc.robot;

/**
 * All robot control classes should extend this class
 */
public interface Mechanism {
	abstract void autonomousInit();

	abstract void autonomousPeriodic();

	abstract void teleopInit();

	abstract void teleopPeriodic();
}
