package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.input.ControllerInput;
import frc.robot.input.driver.*;
import frc.robot.input.operator.JoystickOperator;
import frc.robot.mechanisms.*;

public class Robot extends TimedRobot {
	private ControllerInput m_controller = new ControllerInput(new JoystickDriver(0),
			// new XboxDriver(0),
			// new TaranisDriver(4),
			new JoystickOperator(1, 2));
	private Mechanism[] mechs;
	private AutoNav auto;

	public Robot() {
		super();
		Drive drive = new Drive(m_controller);
		Magazine magazine = new Magazine(m_controller);
		BallShooter ballShooter = new BallShooter(m_controller);
		Cameras cameras = new Cameras(m_controller);

		mechs = new Mechanism[] { drive, magazine, ballShooter, cameras };

		auto = new AutoNav(drive);
	}

	@Override
	public void robotInit() {
		super.robotInit();
	}

	@Override
	public void teleopInit() {
		for (Mechanism mech : mechs)
			mech.teleopInit();
	}

	@Override
	public void teleopPeriodic() {
		for (Mechanism mech : mechs)
			mech.teleopPeriodic();
	}

	@Override
	public void autonomousInit() {
		auto.runTrackBarrelRacing();
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void disabledInit() {
	}
}