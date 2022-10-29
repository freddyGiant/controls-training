// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ExampleSubsystem extends SubsystemBase {

  // private final CANSparkMax m1 = new CANSparkMax(17, MotorType.kBrushless);
  // private final CANSparkMax m2= new CANSparkMax(1, MotorType.kBrushless);
  // private final XboxController c = new XboxController(0);
  // private final AnalogPotentiometer dist = new AnalogPotentiometer(0);
  // private final Timer timer = new Timer();
  // private boolean running;

  /** Creates a new ExampleSubsystem. */
  public ExampleSubsystem() {
    // m1.restoreFactoryDefaults(); m2.restoreFactoryDefaults();
    // m2.follow(m1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // if(c.getAButtonPressed() && !running)
    // {
    //   timer.reset();
    //   timer.start();
    //   running = true;
    // }

    // if(running)
    // {
    //   m1.set(phasedSpeed(timer.get()));
    //   if(timer.get() > 10)
    //   {
    //     timer.stop();
    //     m1.set(0);
    //     running = false;
    //   }
    // }

    // 0 furthest, .65 closest
    
    // m1.set(1 - Math.min(1, dist.get() * (1 / .65)));
  }

  // private double phasedSpeed(double time)
  // {
  //   if(time < 4)
  //     return time / 4;
  //   if(time < 6)
  //     return 1;
  //   if(time < 10)
  //     return 1 - ((time - 6) / 4);
  //   else
  //     return 0;
  // }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
