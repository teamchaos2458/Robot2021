package frc.robot;

import com.revrobotics.CANSparkMax;

import frc.robot.mechanisms.Drive;

public class AutoNav {
    private CANSparkMax[] m_wheels;

    double TIME_OUT = 0.2;

    public AutoNav(Drive drive) {
        m_wheels = drive.getWheels();
    }

    public void runTrackBarrelRacing() {
        // TODO auto code
        PlannedMotion path = new PlannedMotion(m_wheels, TIME_OUT);
        path.setSpeeds(0.1, 0.2);
        path.moveTo(1, 2);
    }

    public void runTrackSlalomPath() {
        // TODO auto code
    }

    public void runTrackBouncePath() {
        // TODO auto code
    }
}