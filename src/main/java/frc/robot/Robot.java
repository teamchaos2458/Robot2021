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
			new JoystickOperator(1, 2, 3));
	private Mechanism[] mechs;

	public Robot() {
		super();
		mechs = new Mechanism[] { new Drive(m_controller), new Magazine(m_controller), new BallShooter(m_controller),
				new Cameras(m_controller) };
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