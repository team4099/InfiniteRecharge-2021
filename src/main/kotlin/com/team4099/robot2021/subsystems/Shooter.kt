package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.units.ctreAngularMechanismSensor
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Shooter : SubsystemBase() {
  private val shooterMotor = TalonFX(Constants.Shooter.SHOOTER_MOTOR_ID)
  private val shooterSensor = ctreAngularMechanismSensor(shooterMotor,2048,2.0)

  init {
    shooterMotor.config_kP(0,Constants.Shooter.SHOOTER_KP,0)
    shooterMotor.config_kI(0,Constants.Shooter.SHOOTER_KI,0)
    shooterMotor.config_kD(0,Constants.Shooter.SHOOTER_KD,0)
  }

  val currentVelocity
    get() = shooterSensor.velocity

  // velocity setpoint
  private var _targetVelocity = 0.rotations.perMinute
  var targetVelocity
    set(velocity) {
      _targetVelocity = velocity
      shooterMotor.set(
        ControlMode.Velocity,
        shooterSensor.velocityToRawUnits(velocity),
        DemandType.ArbitraryFeedForward,
        (Constants.Shooter.SHOOTER_KS +
          Constants.Shooter.SHOOTER_KV * velocity.value)
      )
    }
    get() = _targetVelocity

  fun setOpenLoopPower(power : Double){
    _targetVelocity = 0.rotations.perMinute
    shooterMotor.set(ControlMode.PercentOutput,power)
  }
}
