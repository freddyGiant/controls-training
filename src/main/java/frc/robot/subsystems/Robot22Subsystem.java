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
  
  
  /**
   * The current state of internals, mainly where the ball(s) is and whether it is moving
   */
  private enum Robot22State implements LinearState
  {
    /**
     * When there are no balls in the internals and there is nothing to do
     */
    EMPTY,

    /**
     * When the first ball has tripped entrance sensor, and we should begin storing it
     */
    START_LOADING_FIRST_BALL(true, false) 
    {
      @Override
      public boolean transitionCond() { return ballIsAt(entrance); }
    },

    /** 
     * When the ball has tripped the slot sensor, and we know it is stored
     */ 
    ONE_BALL_LOADED
    {
      @Override
      public boolean transitionCond() { return ballIsAt(slot0); }
    },

    /**
     * When a new ball trips the entrance sensor, and we should begin moving both
     */
    START_LOADING_SECOND_BALL(true, true) 
    {
      @Override
      public boolean transitionCond() { return ballIsAt(entrance); }
    },

    /** 
     * When the old ball has moved out of the way of the slot sensor
     */ 
    SECOND_BALL_IN_MOTION(true, true) 
    {
      @Override
      public boolean transitionCond() { return !ballIsAt(slot0); }
    },

    /**
     * When the first ball then trips the slot sensor, and we know both balls are stored
     */
    TWO_BALLS_LOADED 
    {
      @Override
      public boolean transitionCond() { return ballIsAt(slot0); }
    };

    private boolean runLowerRoller, runUpperRoller;
    private Robot22State() {}
    private Robot22State(boolean runLowerRoller, boolean runUpperRoller)
    {
      this.runLowerRoller = runLowerRoller;
      this.runUpperRoller = runUpperRoller;
    }

    /**
     * Run the rollers as configured for this state
     */
    public void run()
    {
      lowerRoller.set(ControlMode.PercentOutput, runLowerRoller ? ROLLER_SPEED : 0);
      upperRoller.set(ControlMode.PercentOutput, runUpperRoller ? ROLLER_SPEED : 0);
    }

    @Override 
    public boolean transitionCond() { return false; } // overridden by enum-specific definitions
  }

  private static PeriodicLinearFSM<Robot22State> stateMachine = new PeriodicLinearFSM<Robot22State>(Robot22State.EMPTY, Robot22State.values());

  @Override
  public void periodic() 
  {
    stateMachine.updateState();
    stateMachine.getState().run();

    System.out.println(stateMachine.getState().toString());
  }

  private static boolean ballIsAt(AnalogPotentiometer p)
  {
    System.out.print(p.get() + " ");
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
