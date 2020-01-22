package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonManager {
    private static final String kNoMovement = "No Movement";
    private static final String kOffTheLine = "Off The Line";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    public void Initialize() {
        m_chooser.setDefaultOption("No Movement", kNoMovement);
        m_chooser.addOption("Off The Line", kOffTheLine);
        SmartDashboard.putData("Auto choices", m_chooser);
    }

    public void Activate() {
        m_autoSelected = m_chooser.getSelected();
    }

    public void Ping() {
        switch (m_autoSelected) {
            case kOffTheLine:
              
              break;
            case kNoMovement:
            default:
              
              break;
          }
    }
}