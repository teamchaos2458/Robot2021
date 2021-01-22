package frc.robot.mechanisms;

import frc.robot.AutoPhase;
import frc.robot.Mechanism;
import frc.robot.input.ControllerInput;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Timer;

public class BallShooter implements Mechanism {
	private ControllerInput m_controller;
	private AutoPhase m_auto;
	private CANSparkMax m_shooterLeft, m_shooterRight;
	private Timer m_timer = new Timer();

	private double v_shooterSpeedLeft, v_shooterSpeedRight;

	private final double[] SHOOTER_SPEEDS = { 0.4, 0.6, 0.8 };

	public BallShooter(ControllerInput controller, AutoPhase auto, CANSparkMax[] wheels) {
		m_controller = controller;
		m_auto = auto;

		m_shooterLeft = wheels[0];
		m_shooterRight = wheels[1];
	}

	@Override
	public void teleopInit() {
		m_shooterLeft.set(0);
		m_shooterRight.set(0);
		v_shooterSpeedLeft = SHOOTER_SPEEDS[2];
		v_shooterSpeedRight = SHOOTER_SPEEDS[2];
	}

	@Override
	public void teleopPeriodic() {
		if (m_controller.operator().useAnalogShooter()) {
			double analogSpeed = m_controller.operator().getAnalogShooterSpeed();
			m_shooterLeft.set(-analogSpeed);
			m_shooterRight.set(analogSpeed);
		} else {
			// select shooter speeds from button press
			selectShooterSpeeds(7, SHOOTER_SPEEDS[0]);
			selectShooterSpeeds(6, SHOOTER_SPEEDS[1]);
			selectShooterSpeeds(11, SHOOTER_SPEEDS[2]);

			if (m_controller.operator().fireShooter()) {
				m_shooterLeft.set(-v_shooterSpeedLeft);
				m_shooterRight.set(v_shooterSpeedRight);
			} else {
				m_shooterLeft.set(0);
				m_shooterRight.set(0);
			}
		}
	}

	@Override
	public void autonomousInit() {
		m_timer.reset();
		m_shooterLeft.set(0);
		m_shooterRight.set(0);
	}

	@Override
	public void autonomousPeriodic() {
		String phase = m_auto.getPhase();
		if (phase.equals("spinUpShooter")) {
			if (m_auto.startUp()) {
				// start auto sequence
				m_shooterLeft.set(-SHOOTER_SPEEDS[2]);
				m_shooterRight.set(SHOOTER_SPEEDS[2]);
				m_timer.start();
				m_auto.clearStartUp();
			} else {
				// run auto sequence
				m_auto.setPhase("loadBalls");
			}
		} else if (phase.equals("done")) {
			m_shooterLeft.set(0);
			m_shooterRight.set(0);
		}
	}

	private void selectShooterSpeeds(int button, double speed) {
		if (m_controller.operator().useShooterSpeed(button)) {
			// button pressed to select speeds
			v_shooterSpeedLeft = speed;
			v_shooterSpeedRight = speed;
		}
	}
}