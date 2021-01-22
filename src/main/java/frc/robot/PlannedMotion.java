package frc.robot;

import edu.wpi.first.wpilibj.Timer;

import java.util.concurrent.TimeUnit;

import com.revrobotics.CANSparkMax;

public class PlannedMotion {
	private CANSparkMax[] m_motors;
	private Timer m_timeOutTimer = new Timer();

	private boolean v_executing;
	private double[] v_speeds;

	private final double TIME_OUT;

	public PlannedMotion(CANSparkMax[] motors, double timeOut) {
		m_motors = motors;
		v_speeds = new double[motors.length];
		TIME_OUT = timeOut;
		finish();
	}

	public void moveTo(double... goalRotations) {
		v_executing = true;
		m_timeOutTimer.start();

		boolean atDestination = false; // true when all motors at goal

		while (!atDestination) {
			if (m_timeOutTimer.get() > TIME_OUT) {
				System.out.println("Timed Out!");
				finish();
				return;
			}
			// for each motor, move towards destination
			for (int i = 0; i < m_motors.length; i++) {
				CANSparkMax motor = m_motors[i];
				double encoderPos = motor.getEncoder().getPosition();
				double goal = goalRotations[i];

				// get distance to go and make adjustments
				double distanceToGoal = goal - encoderPos;
				if (goal > 0 != distanceToGoal < 0) {
					// keep looking for goal
					if (distanceToGoal > 0)
						motor.set(v_speeds[i]);
					else
						motor.set(-v_speeds[i]);
				} else {
					// this motor has reached goal
					atDestination = true;
					motor.set(0);
				}
			}
			// wait a bit before next loop iteration
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		finish();
	}

	public void asyncMoveTo(double... goalRotations) {
		v_executing = true; // set early in case of race condition

		new Thread(() -> {
			moveTo(goalRotations);
		}).start();
	}

	public void setSpeeds(double speed) {
		for (int i = 0; i < v_speeds.length; i++)
			v_speeds[i] = Math.abs(speed);
	}

	public void setSpeeds(double... speeds) {
		if (speeds.length != v_speeds.length)
			throw new Error("Number of speeds does not match!");
		for (int i = 0; i < v_speeds.length; i++)
			v_speeds[i] = Math.abs(speeds[i]);
	}

	public void finish() {
		v_executing = false;
		m_timeOutTimer.stop();
		m_timeOutTimer.reset();

		for (CANSparkMax c : m_motors)
			c.getEncoder().setPosition(0); // reset encoders to 0
	}

	public boolean isExecuting() {
		return v_executing;
	}
}