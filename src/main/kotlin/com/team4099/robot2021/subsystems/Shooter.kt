package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.units.*
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Shooter : SubsystemBase() {
  private val shooterMotor = TalonFX(Constants.Shooter.SHOOTER_MOTOR_ID)
  private val shooterSensor = ctreAngularMechanismSensor(shooterMotor,2048,2.0)

  fun setVelocity(velocity: AngularVelocity){
    shooterMotor.set(ControlMode.Velocity,shooterSensor.velocityToRawUnits(velocity))
  }

  fun getVelocity():AngularVelocity{
    return shooterSensor.velocity
  }

}
