package frc.robot.mechanisms;

import frc.robot.AutoPhase;
import frc.robot.Mechanism;
import frc.robot.PlannedMotion;
import frc.robot.input.ControllerInput;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;

public class Magazine implements Mechanism {
	private ControllerInput m_controller;
	private AutoPhase m_auto;
	private WPI_TalonSRX m_intake = new WPI_TalonSRX(10);
	private CANSparkMax m_lowerSpool = new CANSparkMax(1, MotorType.kBrushless),
			m_upperSpool = new CANSparkMax(4, MotorType.kBrushless);
	private PlannedMotion m_autoSequence = new PlannedMotion(new CANSparkMax[] { m_lowerSpool, m_upperSpool }, 99,
			new double[][] { { 1.5, 1.5 } });
	private Timer m_timer = new Timer();
	private int v_ballsShot;

	private final double INTAKE_SPEED = 0.3, MAG_SPEED = 0.3, STEP_MAG_SPEED = 0.2, STEP_GAP_SPACING = 2,
			STEP_TIME_OUT = 2;

	private CANSparkMax[] m_spools = { m_lowerSpool, m_upperSpool };
	private PlannedMotion m_stepUp = new PlannedMotion(m_spools, STEP_TIME_OUT,
			new double[] { STEP_GAP_SPACING, STEP_GAP_SPACING }),
			m_stepDown = new PlannedMotion(m_spools, STEP_TIME_OUT,
					new double[] { -STEP_GAP_SPACING, -STEP_GAP_SPACING });

	public Magazine(ControllerInput controller, AutoPhase auto) {
		m_controller = controller;
		m_auto = auto;

		m_lowerSpool.setIdleMode(IdleMode.kBrake);
		m_upperSpool.setIdleMode(IdleMode.kBrake);
		m_upperSpool.setInverted(true);
		m_lowerSpool.setInverted(false);

		m_stepUp.setConstantSpeed(STEP_MAG_SPEED);
		m_stepDown.setConstantSpeed(STEP_MAG_SPEED);
		m_autoSequence.setConstantSpeed(0.5);
	}

	@Override
	public void teleopInit() {
		m_intake.set(0);
		m_upperSpool.set(0);
		m_lowerSpool.set(0);
		m_stepUp.resolveExecution();
		m_stepDown.resolveExecution();
	}

	@Override
	public void teleopPeriodic() {
		// intake
		double intakeSpeed = 0;
		if (m_controller.operator().doBallIntakeIn())
			intakeSpeed -= INTAKE_SPEED;
		if (m_controller.operator().doBallIntakeOut())
			intakeSpeed += INTAKE_SPEED;
		m_intake.set(intakeSpeed);

		// magazine
		if (m_controller.operator().magazineStepUp()) {
			m_stepUp.startExecution();
		} else if (m_controller.operator().magazineStepDown()) {
			m_stepDown.startExecution();
		}

		if (!m_stepUp.isExecuting() && !m_stepDown.isExecuting()) {
			// manual control
			for (int i = 0; i < m_spools.length; i++) {
				// for both spools
				int s = i + 1;
				double spoolSpeed = 0;
				if (m_controller.operator().doMagazineStageUp(s))
					spoolSpeed = MAG_SPEED;
				else if (m_controller.operator().doMagazineStageDown(s))
					spoolSpeed = -MAG_SPEED;
				else
					spoolSpeed = m_controller.operator().getMagazineStageAnalog(s);
				m_spools[i].set(spoolSpeed);
			}
		} else {
			// stepping. loops will do nothing if not stepping
			m_stepUp.executionLoop();
			m_stepDown.executionLoop();
		}
	}

	@Override
	public void autonomousInit() {
		v_ballsShot = 0;
		m_autoSequence.resolveExecution();
		m_timer.reset();
		m_upperSpool.set(0);
		m_lowerSpool.set(0);
	}

	@Override
	public void autonomousPeriodic() {
		if (m_auto.getPhase().equals("loadBalls")) {
			// System.out.println("Loading Balls! " + m_timer.get());
			if (m_auto.startUp()) {
				// start auto sequence
				m_auto.clearStartUp();
			}
			if (m_autoSequence.isExecuting()) {
				// run auto sequence
				m_autoSequence.executionLoop();
			} else {
				// check if ready for next shot
				if (m_timer.get() == 0) {
					m_timer.start();
				}
				if (m_timer.get() >= 1) {
					// time elapsed for new ball
					m_timer.stop();
					m_timer.reset();
					m_autoSequence.startExecution();
					v_ballsShot++;
				}
			}
			if (v_ballsShot >= 3) {
				// auto sequence finished
				m_auto.setPhase("done");
			}
		}
	}
}