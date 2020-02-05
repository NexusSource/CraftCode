package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Driver {
    public Controller controlType = Controller.Xbox;
    public boolean tankMode = false;
    public void Control(DifferentialDrive drive, Joystick joystick, Joystick xbox) {
        if (controlType == Controller.Xbox) {
            //XBOX IS DRIVING
            
            if (xbox.getRawButtonPressed(4) == true)
                tankMode = !tankMode;

            double activate = xbox.getRawAxis(3);
            double fSpeed = xbox.getRawAxis(1);
            double rSpeed = xbox.getRawAxis(0);
            if (activate > .1) {
                if (!tankMode)
                    drive.arcadeDrive(fSpeed * activate, rSpeed * activate, true);
                else
                    drive.tankDrive(xbox.getRawAxis(5), fSpeed);
            }
            else
                UpdateState(joystick);
        }
        else if (controlType == Controller.Joystick) {
            //JOYSTICK IS DRIVING

            boolean activate = joystick.getRawButton(1);
            double fSpeed = joystick.getRawAxis(1);
            double rSpeed = joystick.getRawAxis(2);
            if (activate) {
                drive.arcadeDrive(fSpeed, rSpeed, true);
            }
            else
                UpdateState(joystick);
        }
    }

    private void UpdateState(Joystick j) {
        double axis = j.getRawAxis(3);
        if (controlType == Controller.Joystick && axis > .5)
            controlType = Controller.Xbox;
        else if (controlType == Controller.Xbox && axis < -.5)
            controlType = Controller.Joystick;
    }
}
