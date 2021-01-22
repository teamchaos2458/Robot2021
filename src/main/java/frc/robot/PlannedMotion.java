package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.CANSparkMax;

public class PlannedMotion {
	private CANSparkMax[] m_motors;
	private double[][] m_plan;
	private Timer m_timeOutTimer = new Timer();

	private int v_currentStage;
	private boolean v_executing;
	private double v_constantSpeed;

	private final double TIME_OUT;

	public PlannedMotion(CANSparkMax[] motors, double timeOut, double[][] plan) {
		m_motors = motors;
		m_plan = plan;

		TIME_OUT = timeOut;

		// m_plan format

		// double[][] {
		// {-1, -1} <- first position goal (rotations)
		// {-0.5, 0.5} <- second position goal (rotations)
		// ...

		// .......^ second motor
		// ..^ first motor
		// }

	}

	public PlannedMotion(CANSparkMax[] motors, double timeOut, double[] plan) {
		// single stage motion
		this(motors, timeOut, new double[][] { plan });
	}

	public void executionLoop() {
		if (!v_executing)
			return;

		if (m_timeOutTimer.get() > TIME_OUT) {
			resolveExecution();
			return;
		}

		double[] motorGoals = m_plan[v_currentStage]; // get position goal for array of motors
		boolean stageComplete = true; // remains true unless at least one motor is not ready for next stage

		// for each motor goal pair
		for (int i = 0; i < m_motors.length; i++) {
			CANSparkMax motor = m_motors[i];
			double encoderPos = motor.getEncoder().getPosition();
			double goal = motorGoals[i];

			// get distance to go and make adjustments
			double distanceToGoal = goal - encoderPos;
			if (goal > 0 ^ distanceToGoal < 0) {
				// keep looking for goal
				if (distanceToGoal > 0)
					motor.set(v_constantSpeed);
				else
					motor.set(-v_constantSpeed);
				stageComplete = false;
			} else {
				// this motor has reached goal
				motor.set(0);
			}
		}

		if (stageComplete) {
			// all motors have completed stage
			moveToStage(v_currentStage + 1);

			if (v_currentStage >= m_plan.length) {
				// finished entire planned motion
				resolveExecution();
			}
		}
	}

	public void startExecution() {
		if (v_executing)
			return; // prevent re-execution during active execution
		moveToStage(0);
		v_executing = true;
	}

	public boolean isExecuting() {
		return v_executing;
	}

	public void setConstantSpeed(double speed) {
		v_constantSpeed = Math.abs(speed);
	}

	public void resolveExecution() {
		v_executing = false;
		m_timeOutTimer.stop();
		m_timeOutTimer.reset();
	}

	private void moveToStage(int stage) {
		v_currentStage = stage;
		m_timeOutTimer.reset();
		for (CANSparkMax c : m_motors)
			c.getEncoder().setPosition(0); // reset encoders to 0
	}
}