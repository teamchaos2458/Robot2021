package frc.robot.input.operator;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickOperator implements OperatorInput {
    private Joystick m_shooterJoy, m_intakeJoy;

    public JoystickOperator(int shooterPort, int intakePort) {
        m_shooterJoy = new Joystick(shooterPort);
        m_intakeJoy = new Joystick(intakePort);
    }

    // Magazine

    public boolean doBallIntakeIn() {
        return m_intakeJoy.getTrigger();
    }

    public boolean doBallIntakeOut() {
        return m_intakeJoy.getRawButton(5);
    }

    public boolean doMagazineStageUp(int stage) {
        if (stage == 1)
            return m_intakeJoy.getRawButton(3);
        else if (stage == 2)
            return m_shooterJoy.getRawButton(3);
        return false;
    }

    public boolean doMagazineStageDown(int stage) {
        if (stage == 1)
            return m_intakeJoy.getRawButton(2);
        else if (stage == 2)
            return m_shooterJoy.getRawButton(2);
        return false;
    }

    public double getMagazineStageAnalog(int stage) {
        if (stage == 1)
            return useAnalogShooter() ? 0 : -m_intakeJoy.getY();
        else if (stage == 2)
            return useAnalogShooter() ? 0 : -m_shooterJoy.getY();
        return 0;
    }

    public boolean magazineStepUp() {
        return m_intakeJoy.getRawButtonPressed(11);
    }

    public boolean magazineStepDown() {
        return m_intakeJoy.getRawButtonPressed(10);
    }

    // BallShooter

    public boolean fireShooter() {
        return m_shooterJoy.getTrigger();
    }

    public boolean useShooterSpeed(int button) {
        return m_shooterJoy.getRawButtonPressed(button);
    }

    public boolean useAnalogShooter() {
        return m_shooterJoy.getRawButton(10);
    }

    public double getAnalogShooterSpeed() {
        return -m_shooterJoy.getY();
    }
}