package frc.robot.mechanisms;

import frc.robot.Mechanism;
import frc.robot.input.ControllerInput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class BallShooter implements Mechanism {
	private ControllerInput m_controller;
	private CANSparkMax m_shooterLeft = new CANSparkMax(6, MotorType.kBrushless),
			m_shooterRight = new CANSparkMax(5, MotorType.kBrushless);

	private double v_shooterSpeedLeft, v_shooterSpeedRight;

	private final double[] SHOOTER_SPEEDS = { 0.4, 0.6, 0.8 };

	public BallShooter(ControllerInput controller) {
		m_controller = controller;

		m_shooterLeft.setInverted(true);
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
			m_shooterLeft.set(analogSpeed);
			m_shooterRight.set(analogSpeed);
		} else {
			// select shooter speeds from button press
			selectShooterSpeeds(7, SHOOTER_SPEEDS[0]);
			selectShooterSpeeds(6, SHOOTER_SPEEDS[1]);
			selectShooterSpeeds(11, SHOOTER_SPEEDS[2]);

			if (m_controller.operator().fireShooter()) {
				m_shooterLeft.set(v_shooterSpeedLeft);
				m_shooterRight.set(v_shooterSpeedRight);
			} else {
				m_shooterLeft.set(0);
				m_shooterRight.set(0);
			}
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