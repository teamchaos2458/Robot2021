package frc.robot;

public class AutoPhase {
	private String v_phase;
	private boolean v_startUp;

	public String getPhase() {
		return v_phase;
	}

	public void clearStartUp() {
		v_startUp = false;
	}

	public boolean startUp() {
		return v_startUp;
	}

	public void setPhase(String phase) {
		v_phase = phase;
		v_startUp = true;
		System.out.println("Moved To: " + phase);
	}
}