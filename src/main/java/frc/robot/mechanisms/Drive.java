package frc.robot.mechanisms;

import frc.robot.Mechanism;
import frc.robot.input.ControllerInput;
import frc.robot.PlannedMotion;
import frc.robot.AutoPhase;
import frc.robot.Functions;
import frc.robot.Limelight;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax.IdleMode;

public class Drive implements Mechanism {
	private ControllerInput m_controller;
	private AutoPhase m_auto;
	private CANSparkMax m_leftWheel = new CANSparkMax(3, MotorType.kBrushless),
			m_rightWheel = new CANSparkMax(2, MotorType.kBrushless);
	private Limelight m_limelight = new Limelight(m_controller);
	private Timer m_timer = new Timer();

	private final double DRIVE_SCALE = 1, SLOW_SCALE = 0.4, LERP_TIME_DELTA = 0.2, // <- needs debug before use other
																					// than 1
			LINE_UP_SPEED = 0.1, LINE_UP_TIME_OUT = 3, AUTO_TIME_OUT = 20;

	private CANSparkMax[] m_wheels = { m_leftWheel, m_rightWheel };
	private PlannedMotion m_lineUpShot = new PlannedMotion(m_wheels, LINE_UP_TIME_OUT, new double[][] { { -6, -6 }, // back
																													// up
			{ -1.5, 1.5 } // turn to align
	}), m_autoSequence = new PlannedMotion(m_wheels, AUTO_TIME_OUT, new double[][] { { -50, -50 } });

	public Drive(ControllerInput controller, AutoPhase auto) {
		m_controller = controller;
		m_auto = auto;

		for (int i = 0; i < m_wheels.length; i++) {
			CANPIDController pid = m_wheels[i].getPIDController();

			pid.setP(0.1);
			pid.setOutputRange(-LINE_UP_SPEED, LINE_UP_SPEED);

			m_wheels[i].setIdleMode(IdleMode.kCoast);
		}

		m_rightWheel.setInverted(true);

		m_autoSequence.setConstantSpeed(0.3);
		m_lineUpShot.setConstantSpeed(0.3);
	}

	@Override
	public void teleopInit() {
		m_leftWheel.set(0);
		m_rightWheel.set(0);
		setIdleMode(IdleMode.kBrake);
		m_lineUpShot.resolveExecution();
		m_autoSequence.resolveExecution();
	}

	@Override
	public void teleopPeriodic() {
		m_limelight.run();
		if (m_controller.driver().lineUpShot()) {
			m_lineUpShot.startExecution();
		}

		if (!m_lineUpShot.isExecuting()) {
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
		} else {
			m_lineUpShot.executionLoop();
		}

		// hold line up
		if (m_controller.driver().adjustWithLimelight()) {
			lineUpWithLimelight();
		}
		SmartDashboard.putNumber("rightWheelAngle", m_rightWheel.getEncoder().getPosition());
	}

	@Override
	public void autonomousInit() {
		m_autoSequence.resolveExecution();
		m_timer.reset();
	}

	@Override
	public void autonomousPeriodic() {
		if (m_auto.getPhase().equals("driveBack")) {
			if (m_auto.startUp()) {
				// start auto sequence
				m_autoSequence.startExecution();
				m_auto.clearStartUp();
			} else {
				if (m_autoSequence.isExecuting()) {
					// run auto backup sequence
					m_autoSequence.executionLoop();
				} else {
					// line up with limelight
					if (m_timer.get() == 0) {
						m_timer.start();
					}
					if (m_timer.get() < 3) {
						lineUpWithLimelight();
					} else {
						// auto sequence finished
						m_timer.stop();
						m_auto.setPhase("spinUpShooter");
					}
				}
			}
		}
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