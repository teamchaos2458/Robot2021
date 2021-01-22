package frc.robot.input;

import frc.robot.input.driver.DriverInput;
import frc.robot.input.operator.OperatorInput;

public class ControllerInput {
	private DriverInput m_driver;
	private OperatorInput m_operator;

	public ControllerInput(DriverInput driver, OperatorInput operator) {
		m_driver = driver;
		m_operator = operator;
	}

	public OperatorInput operator() {
		return m_operator;
	}

	public DriverInput driver() {
		return m_driver;
	}
}