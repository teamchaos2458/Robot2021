package frc.robot.input.operator;

public interface OperatorInput {
	// Magazine
	public boolean doBallIntakeIn();

	public boolean doBallIntakeOut();

	public boolean doMagazineStageUp(int stage);

	public boolean doMagazineStageDown(int stage);

	public double getMagazineStageAnalog(int stage);

	public boolean magazineStepUp();

	public boolean magazineStepDown();

	// BallShooter
	public boolean fireShooter();

	public boolean useShooterSpeed(int button);

	public boolean useAnalogShooter();

	public double getAnalogShooterSpeed();
}