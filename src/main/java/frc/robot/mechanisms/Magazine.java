package frc.robot.mechanisms;

import frc.robot.Mechanism;
import frc.robot.PlannedMotion;
import frc.robot.input.ControllerInput;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Magazine implements Mechanism {
	private ControllerInput m_controller;
	private WPI_TalonSRX m_intake = new WPI_TalonSRX(10);
	private CANSparkMax m_lowerSpool = new CANSparkMax(1, MotorType.kBrushless),
			m_upperSpool = new CANSparkMax(4, MotorType.kBrushless);

	private final double INTAKE_SPEED = 0.3, MAG_SPEED = 0.3, STEP_MAG_SPEED = 0.2, STEP_GAP_SPACING = 2,
			STEP_TIME_OUT = 2;

	private CANSparkMax[] m_spools = { m_lowerSpool, m_upperSpool };
	private PlannedMotion m_step = new PlannedMotion(m_spools, STEP_TIME_OUT);

	public Magazine(ControllerInput controller) {
		m_controller = controller;

		m_lowerSpool.setIdleMode(IdleMode.kBrake);
		m_upperSpool.setIdleMode(IdleMode.kBrake);
		m_upperSpool.setInverted(true);
		m_lowerSpool.setInverted(false);

		m_step.setSpeeds(STEP_MAG_SPEED);
	}

	@Override
	public void teleopInit() {
		m_intake.set(0);
		m_upperSpool.set(0);
		m_lowerSpool.set(0);
		m_step.finish();
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
			m_step.asyncMoveTo(STEP_GAP_SPACING, STEP_GAP_SPACING);
		} else if (m_controller.operator().magazineStepDown()) {
			m_step.asyncMoveTo(-STEP_GAP_SPACING, -STEP_GAP_SPACING);
		}

		if (!m_step.isExecuting()) {
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
		}
	}
}