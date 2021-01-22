package frc.robot;

public class Functions {
	public static double lerp(double goal, double current, double time) {
		return current + (goal - current) * time;
	}
}