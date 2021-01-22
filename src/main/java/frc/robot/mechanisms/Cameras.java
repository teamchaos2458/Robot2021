package frc.robot.mechanisms;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.Mechanism;
import frc.robot.input.ControllerInput;

public class Cameras implements Mechanism {
    private ControllerInput m_controller;
    private CameraServer s = CameraServer.getInstance();
    private UsbCamera m_cam1 = s.startAutomaticCapture(0);
    // m_cam2 = s.startAutomaticCapture(1);
    private VideoSink m_server = s.getServer();

    public Cameras(ControllerInput controller) {
        m_controller = controller;

        m_cam1.setFPS(30);
        // m_cam2.setFPS(30);
        m_cam1.setResolution(640 / 2, 360 / 2);
        // m_cam2.setResolution(640 / 4, 360 / 4);
        m_server.setSource(m_cam1);
    }

    @Override
    public void teleopInit() {
        m_server.setSource(m_cam1);
    }

    @Override
    public void teleopPeriodic() {
        if (m_controller.driver().useCamera1()) {
            m_server.setSource(m_cam1);
        } else if (m_controller.driver().useCamera2()) {
            // m_server.setSource(m_cam2);
        }
    }
}