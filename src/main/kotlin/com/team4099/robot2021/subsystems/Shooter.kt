package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.ctreAngularMechanismSensor
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.inRotationsPerMinute
import com.team4099.lib.units.inRotationsPerSecond
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Shooter : SubsystemBase() {
  private val shooterMotor = TalonFX(Constants.Shooter.SHOOTER_MOTOR_ID)
  private val shooterSensor = ctreAngularMechanismSensor(shooterMotor, 2048, 1.0)

  private val shooterFollower = TalonFX(Constants.Shooter.SHOOTER_FOLLOWER_ID)

  private val solenoid =
      DoubleSolenoid(
          Constants.Shooter.SOLENOID_FORWARD_CHANNEL, Constants.Shooter.SOLENOID_REVERSE_CHANNEL)

  enum class HoodPosition(val pos: DoubleSolenoid.Value) {
    EXTENDED(DoubleSolenoid.Value.kForward),
    RETRACTED(DoubleSolenoid.Value.kReverse)
  }

  var hoodState = HoodPosition.RETRACTED
    set(value) {
      field = value
      solenoid.set(hoodState.pos)
    }

  init {
    shooterMotor.configFactoryDefault()
    shooterFollower.configFactoryDefault()

    shooterFollower.setInverted(InvertType.OpposeMaster)
    shooterFollower.follow(shooterMotor)

    shooterMotor.enableVoltageCompensation(true)
    shooterFollower.enableVoltageCompensation(true)

    shooterMotor.config_kP(0, Constants.Shooter.SHOOTER_KP, 0)
    shooterMotor.config_kI(0, Constants.Shooter.SHOOTER_KI, 0)
    shooterMotor.config_kD(0, Constants.Shooter.SHOOTER_KD, 0)

    Logger.addSource("Shooter", "Shooter Current Velocity (rpm)") {
      currentVelocity.inRotationsPerMinute
    }

    Logger.addSource("Shooter", "Raw Shooter Velocity") {
      shooterMotor.selectedSensorVelocity / 2048
    }

    Logger.addSource("Shooter", "Shooter Target Velocity (rpm)") {
      targetVelocity.inRotationsPerMinute
    }

    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Power") {
      shooterMotor.motorOutputPercent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Stator Current") {
      shooterMotor.statorCurrent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Primary Supply Current") {
      shooterMotor.supplyCurrent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Follower Supply Current") {
      shooterFollower.supplyCurrent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Voltage") {
      shooterMotor.motorOutputVoltage
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Primary Bus Voltage") {
      shooterMotor.busVoltage
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Follower Bus Voltage") {
      shooterFollower.busVoltage
    }

    Logger.addSource(
        Constants.Shooter.TAB,
        "Shooter kP",
        { Constants.Shooter.SHOOTER_KP },
        { newP -> shooterMotor.config_kP(0, newP, 0) },
        false)

    Logger.addSource(
        Constants.Shooter.TAB,
        "Shooter kD",
        { Constants.Shooter.SHOOTER_KD },
        { newD -> shooterMotor.config_kD(0, newD, 0) },
        false)
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
              Constants.Shooter.SHOOTER_KV * velocity.inRotationsPerSecond) / 12.0)
    }
    get() = _targetVelocity

  fun setOpenLoopPower(power: Double) {
    _targetVelocity = 0.rotations.perMinute
    shooterMotor.set(ControlMode.PercentOutput, power)
  }
}
