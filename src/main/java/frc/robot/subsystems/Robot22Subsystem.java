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

import util.*;

public class Robot22Subsystem extends SubsystemBase 
{
  private static final double 
    IR_MAX = 0.65,
    SENSOR_TOL = 0.9, // guess, untuned
    ROLLER_SPEED = 0.5; // guess, untuned

  private static final int
    COLOR_MAX = 2047;

  private static final AnalogPotentiometer 
    entrance = new AnalogPotentiometer(0); // ir0 on the diagram

  private static final ColorSensorV3
    slot0 = new ColorSensorV3(I2C.Port.kMXP); // "slot" used to mean a position where a ball can be stored

  private static final TalonSRX
    lowerRoller = new TalonSRX(13),
    upperRoller = new TalonSRX(14);

  public Robot22Subsystem() 
  {
    // just in case
    lowerRoller.configFactoryDefault();
    upperRoller.configFactoryDefault();
  }

  private enum Robot22State implements LinearState
  {
    EMPTY,

    // first ball has tripped entrance sensor, begin storing it
    START_LOADING_FIRST_BALL(true, false) 
    {
      @Override
      public boolean transitionCond() { return ballIsAt(entrance); }
    },

    // ball tripped slot sensor, stored
    ONE_BALL_LOADED
    {
      @Override
      public boolean transitionCond() { return ballIsAt(slot0); }
    },

    // new ball tripped entrance sensor while ball already stored, begin moving both
    START_LOADING_SECOND_BALL(true, true) 
    {
      @Override
      public boolean transitionCond() { return ballIsAt(entrance); }
    },

    // make sure the old ball is no longer tripping the storage sensor
    SECOND_BALL_IN_MOTION(true, true) 
    {
      @Override
      public boolean transitionCond() { return !ballIsAt(slot0); }
    },

    // if a ball trips the sensor, we know it's the new one. both balls are stored
    TWO_BALLS_LOADED 
    {
      @Override
      public boolean transitionCond() { return ballIsAt(slot0); }
    };

    private boolean runLowerRoller, runUpperRoller;
    private Robot22State nextState;
    private Robot22State() {}
    private Robot22State(boolean runLowerRoller, boolean runUpperRoller)
    {
      this.runLowerRoller = runLowerRoller;
      this.runUpperRoller = runUpperRoller;
    }

    public Robot22State getNextState() 
    { 
      if(this.ordinal() == Robot22State.values().length - 1) return null; // if on last state
      if(nextState == null) nextState = Robot22State.values()[this.ordinal() + 1]; // memoize next state
      return nextState;
    }

    public Robot22State updateState() { return getNextState() != null && getNextState().transitionCond() ? getNextState() : this; }

    public void run()
    {
      lowerRoller.set(ControlMode.PercentOutput, runLowerRoller ? ROLLER_SPEED : 0);
      upperRoller.set(ControlMode.PercentOutput, runUpperRoller ? ROLLER_SPEED : 0);
    }

    @Override 
    public boolean transitionCond() { return false; } // overridden by enum-specific definitions
  }

  /**
   * the current state of internals, mainly where the ball(s) is and whether it is moving
   * 
   * grammar
   */
  private static Robot22State state = Robot22State.EMPTY;

  @Override
  public void periodic() 
  {
    state = state.updateState();
    state.run();

    System.out.println(state.toString());
  }

  private static boolean ballIsAt(AnalogPotentiometer p)
  {
    System.out.print(p.get());
    return p.get() > (SENSOR_TOL * IR_MAX); 
  }
  private static boolean ballIsAt(ColorSensorV3 c) 
  {
    return c.getProximity() > (SENSOR_TOL * COLOR_MAX); 
  } 

  @Override
  public void simulationPeriodic() 
  {
    // This method will be called once per scheduler run during simulation
  }
}
