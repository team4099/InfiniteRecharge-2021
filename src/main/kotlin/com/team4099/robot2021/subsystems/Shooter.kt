package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.units.AngularVelocity
import com.team4099.lib.units.ctreAngularMechanismSensor
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Shooter : SubsystemBase() {
  private val shooterMotor = TalonFX(Constants.Shooter.SHOOTER_MOTOR_ID)
  private val shooterSensor = ctreAngularMechanismSensor(shooterMotor,2048,2.0)

  var currentVelocity = 0.rotations.perMinute
    get() { return shooterSensor.velocity }

  // velocity setpoint
  var targetVelocity = 0.rotations.perMinute
    set(velocity) = shooterMotor.set(ControlMode.Velocity,shooterSensor.velocityToRawUnits(velocity))

  fun setOpenLoopPower(power : Double){
    shooterMotor.set(ControlMode.PercentOutput,power)
  }

}
