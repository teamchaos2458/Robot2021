package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
	private AutoPhase m_auto = new AutoPhase();

	private CANSparkMax m_leftShooterWheel = new CANSparkMax(6, MotorType.kBrushless),
			m_rightShooterWheel = new CANSparkMax(5, MotorType.kBrushless);
	private CANSparkMax[] m_shooterWheels = { m_leftShooterWheel, m_rightShooterWheel };
	private Led m_led = new Led();

	public Robot() {
		super();
		mechs = new Mechanism[] { new Drive(m_controller, m_auto), new Magazine(m_controller, m_auto),
				new BallShooter(m_controller, m_auto, m_shooterWheels), new Cameras(m_controller) };
	}

	@Override
	public void robotInit() {
		super.robotInit();
		m_led.robotInit();
	}

	@Override
	public void teleopInit() {
		for (Mechanism mech : mechs)
			mech.teleopInit();
		m_led.setOff();
		m_led.setChase();
	}

	@Override
	public void teleopPeriodic() {
		for (Mechanism mech : mechs)
			mech.teleopPeriodic();
	}

	@Override
	public void autonomousInit() {
		m_auto.setPhase("driveBack");
		for (Mechanism mech : mechs)
			mech.autonomousInit();
	}

	@Override
	public void autonomousPeriodic() {
		for (Mechanism mech : mechs)
			mech.autonomousPeriodic();
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void disabledInit() {
	}
}