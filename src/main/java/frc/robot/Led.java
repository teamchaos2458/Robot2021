package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;

public class Led {
	private SerialPort m_serial;

	public Led() {
	}

	public void robotInit() {
		try {
			m_serial = new SerialPort(9600, SerialPort.Port.kMXP);
			// Will wait a max of half a second.
			m_serial.setTimeout(0.8);
		} catch (Exception e) {
			System.out.println("Cannot connect to arduino");
		}
	}

	public void setOff() {
		m_serial.writeString("0");
	}

	public void setSlow() {
		m_serial.writeString("1");
	}

	public void setMedium() {
		m_serial.writeString("2");
	}

	public void setFast() {
		m_serial.writeString("3");
	}

	public void setChase() {
		m_serial.writeString("4");
	}
}