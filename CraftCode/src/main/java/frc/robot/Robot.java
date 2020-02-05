/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.osgi.OpenCVNativeLoader;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private AutonManager _auton = new AutonManager();
  private Driver _driver = new Driver();
  private Vision _vision = new Vision();
  private final Joystick _joystick = new Joystick(0);
  private final Joystick _controller = new Joystick(1);
  private final DifferentialDrive _drive  = new DifferentialDrive(new PWMVictorSPX(8), new PWMVictorSPX(9));
  private final SendableChooser<String> _chooser = new SendableChooser<>();
  public UsbCamera camera;

  

  @Override
  public void robotInit() {
    _auton = new AutonManager();
    _auton.Initialize();
    _chooser.setDefaultOption("Xbox", Controller.Xbox.toString());
    _chooser.addOption("Joystick", Controller.Joystick.toString());
    SmartDashboard.putData("Control Choices", _chooser);
    
    camera = CameraServer.getInstance().startAutomaticCapture(0);
    
    _vision.SetupVision();
    
  }
  int timer = 0;
  @Override
  public void robotPeriodic() {
    if (timer > 100) {
      _vision.UpdateVision();
    }
    timer++;
  }

  @Override
  public void autonomousInit() {
    _auton.Activate();
  }

  @Override
  public void autonomousPeriodic() {
    _auton.Ping();
  }

  @Override
  public void teleopInit() {
    _driver.controlType = Controller.valueOf(_chooser.getSelected());
    
  }

  @Override
  public void teleopPeriodic() {
    //_driver.Control(_drive, _joystick, _controller);
    //_drive.stopMotor();
    _vision.UpdateVision();

    if (_controller.getRawButton(1)) {

      _vision.MoveRobotByColor(_drive);
    }
    else {
      _drive.stopMotor();
    }
    
  }


  @Override
  public void disabledInit() {
  }
}
