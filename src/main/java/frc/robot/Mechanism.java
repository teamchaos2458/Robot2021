package frc.robot;

/**
 * All robot control classes should extend this class
 */
public interface Mechanism {
	abstract void teleopInit();

	abstract void teleopPeriodic();
}
