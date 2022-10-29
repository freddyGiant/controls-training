// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Robot22Subsystem extends SubsystemBase 
{
  private enum Robot22State
  {
    EMPTY,
    START_LOADING_FIRST_BALL,
    ONE_BALL_LOADED,
    START_LOADING_SECOND_BALL,
    SECOND_BALL_IN_MOTION,
    TWO_BALLS_LOADED;
  }

  private final AnalogPotentiometer 
    entrance = new AnalogPotentiometer(0); // ir0 on the diagram

  private final ColorSensorV3
    slot0 = new ColorSensorV3(I2C.Port.kMXP); // "slot" used to mean a position where a ball can be stored

  private final TalonSRX
    lowerRoller = new TalonSRX(13),
    upperRoller = new TalonSRX(14);

  private static final double ROLLER_SPEED = 0.5; // guess, untuned
  private double
    lowerRollerSpeed,
    upperRollerSpeed;
  /**
   * the current state of internals, mainly where the ball(s) is and whether it is moving
   * 
   * grammar
   */
  private Robot22State state = Robot22State.EMPTY;

  public Robot22Subsystem() 
  {
    // just in case
    lowerRoller.configFactoryDefault();
    upperRoller.configFactoryDefault();
  }

  @Override
  public void periodic() 
  {
    // dont run the motors by default
    lowerRollerSpeed = 0;
    upperRollerSpeed = 0;

    switch(state)
    {
      case EMPTY:
        if(ballIsAt(entrance)) // ball entered
          state = Robot22State.START_LOADING_FIRST_BALL;
        break;

      case START_LOADING_FIRST_BALL:
        lowerRollerSpeed = ROLLER_SPEED; // run lower roller
        if(ballIsAt(slot0)) // 1st ball in place
          state = Robot22State.ONE_BALL_LOADED;
        break;

      case ONE_BALL_LOADED:
        if(ballIsAt(entrance)) // new ball entered
          state = Robot22State.START_LOADING_SECOND_BALL;
        break;

      case START_LOADING_SECOND_BALL:
        lowerRollerSpeed = ROLLER_SPEED; // run both rollers
        upperRollerSpeed = ROLLER_SPEED;
        if(!ballIsAt(slot0)) // once we know second ball is out of the way...
          state = Robot22State.SECOND_BALL_IN_MOTION;
        break;

      case SECOND_BALL_IN_MOTION:
        lowerRollerSpeed = ROLLER_SPEED; // continue
        upperRollerSpeed = ROLLER_SPEED;
        if(ballIsAt(slot0)) // ...we can safely say that, if a ball reaches this position again, it's the new one. 
          state = Robot22State.TWO_BALLS_LOADED;
        break;

      case TWO_BALLS_LOADED:
        break;
    }

    System.out.println(state.toString());

    lowerRoller.set(ControlMode.PercentOutput, lowerRollerSpeed);
    upperRoller.set(ControlMode.PercentOutput, upperRollerSpeed);
  }

  private boolean ballIsAt(AnalogPotentiometer p)
  {
    System.out.print(p.get());
    return p.get() > .55; // guess, untuned
    // return true;
  }
  private boolean ballIsAt(ColorSensorV3 c) 
  {
    return c.getProximity() > 2000; // guess, untuned
    // return true;
  } 

  @Override
  public void simulationPeriodic() 
  {
    // This method will be called once per scheduler run during simulation
  }
}
