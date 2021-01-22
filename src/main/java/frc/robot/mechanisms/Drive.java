package frc.robot.mechanisms;

import frc.robot.Mechanism;
import frc.robot.input.ControllerInput;
import frc.robot.Functions;
import frc.robot.Limelight;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax.IdleMode;

public class Drive implements Mechanism {
	private ControllerInput m_controller;
	private CANSparkMax m_leftWheel = new CANSparkMax(3, MotorType.kBrushless),
			m_rightWheel = new CANSparkMax(2, MotorType.kBrushless);
	private Limelight m_limelight = new Limelight(m_controller);

	private final double DRIVE_SCALE = 1, SLOW_SCALE = 0.4, LERP_TIME_DELTA = 0.2, LINE_UP_SPEED = 0.1;

	private CANSparkMax[] m_wheels = { m_leftWheel, m_rightWheel };

	public Drive(ControllerInput controller) {
		m_controller = controller;

		for (int i = 0; i < m_wheels.length; i++) {
			CANPIDController pid = m_wheels[i].getPIDController();

			pid.setP(0.1);
			pid.setOutputRange(-LINE_UP_SPEED, LINE_UP_SPEED);

			m_wheels[i].setIdleMode(IdleMode.kCoast);
		}

		m_rightWheel.setInverted(true);
	}

	@Override
	public void teleopInit() {
		m_leftWheel.set(0);
		m_rightWheel.set(0);
		setIdleMode(IdleMode.kBrake);
	}

	@Override
	public void teleopPeriodic() {
		// normal drive
		if (m_controller.driver().toggleBrakeCoast()) {
			if (m_leftWheel.getIdleMode() == IdleMode.kBrake) {
				setIdleMode(IdleMode.kCoast);
			} else {
				setIdleMode(IdleMode.kBrake);
			}
		}

		double[] throts = m_controller.driver().getDriveThrots();

		double throtLY = throts[0];
		double throtRX = throts[1];
		// throts -> left right speeds
		double speedL = throtLY + throtRX;
		double speedR = throtLY - throtRX;
		// -> normal scale down
		speedL *= DRIVE_SCALE;
		speedR *= DRIVE_SCALE;
		// -> optional additional scale down
		if (!m_controller.driver().driveSlow()) {
			speedL *= SLOW_SCALE;
			speedR *= SLOW_SCALE;
		}
		// -> scale to fit both within -1 and 1
		double fitScale = getScaleToFitRange(speedL, speedR);
		speedL *= fitScale;
		speedR *= fitScale;
		// -> apply lerp
		speedL = Functions.lerp(speedL, m_leftWheel.get(), LERP_TIME_DELTA);
		speedR = Functions.lerp(speedR, m_rightWheel.get(), LERP_TIME_DELTA);

		m_leftWheel.set(speedL);
		m_rightWheel.set(speedR);

		// hold line up
		if (m_controller.driver().adjustWithLimelight())

		{
			lineUpWithLimelight();
		}
		SmartDashboard.putNumber("rightWheelAngle", m_rightWheel.getEncoder().getPosition());
	}

	private void setIdleMode(IdleMode mode) {
		m_leftWheel.setIdleMode(mode);
		m_rightWheel.setIdleMode(mode);
	}

	private double getScaleToFitRange(double left, double right) {
		if (!inRange(left) || !inRange(right)) {
			return 1 / Math.max(Math.abs(left), Math.abs(right));
		}
		return 1;
	}

	private boolean inRange(double val) {
		return val >= -1 && val <= 1;
	}

	private void lineUpWithLimelight() {
		double angleShift = m_limelight.getShift();
		System.out.println(angleShift);
		m_leftWheel.set(angleShift);
		m_rightWheel.set(-angleShift);
	}
}