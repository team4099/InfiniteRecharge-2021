package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.InvertType
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.ctreAngularMechanismSensor
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.inRotationsPerMinute
import com.team4099.lib.units.inRotationsPerSecond
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.simulation.FlywheelSim
import edu.wpi.first.wpilibj.system.plant.DCMotor
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Shooter : SubsystemBase() {
  private val shooterMotor = WPI_TalonSRX(Constants.Shooter.SHOOTER_MOTOR_ID)
  private val shooterSensor = ctreAngularMechanismSensor(shooterMotor, 2048, Constants.Shooter.GEAR_RATIO)

  private val shooterFollower = WPI_TalonSRX(Constants.Shooter.SHOOTER_FOLLOWER_ID)

  private val solenoid =
      DoubleSolenoid(
          Constants.Shooter.SOLENOID_FORWARD_CHANNEL, Constants.Shooter.SOLENOID_REVERSE_CHANNEL)

  private lateinit var simPhysics: FlywheelSim

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

    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Power") {
      shooterMotor.motorOutputPercent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Stator Current") {
      shooterMotor.statorCurrent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Supply Current") {
      shooterMotor.supplyCurrent
    }
    Logger.addSource(Constants.Shooter.TAB, "Shooter Motor Voltage") {
      shooterMotor.motorOutputVoltage
    }

    //adding mutable entries to dashboard
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

    if (RobotBase.isSimulation()) {
      simPhysics = FlywheelSim(DCMotor.getFalcon500(2), Constants.Shooter.GEAR_RATIO, Constants.Shooter.FLYWHEEL_MOI)
      shooterMotor.simCollection.setQuadratureRawPosition(0)
    }
  }

  override fun simulationPeriodic() {
    println("Shooter commanded output: ${shooterMotor.motorOutputPercent} voltage: ${RobotController.getBatteryVoltage()}")
    simPhysics.setInput(shooterMotor.motorOutputPercent * RobotController.getBatteryVoltage())
    simPhysics.update(0.020)

    println("shooter raw velocity: ${shooterSensor.velocityToRawUnits(simPhysics.angularVelocityRPM.rotations.perMinute)}")
    shooterMotor.simCollection.setQuadratureVelocity(shooterSensor.velocityToRawUnits(simPhysics.angularVelocityRPM.rotations.perMinute).toInt())
    shooterMotor.simCollection.addQuadraturePosition(shooterSensor.velocityToRawUnits(simPhysics.angularVelocityRPM.rotations.perMinute * 0.02).toInt())
    shooterMotor.simCollection.setBusVoltage(12.0)
  }

  val currentVelocity
    get() = shooterSensor.velocity

  // velocity set point
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
    println("shooter output: $power")
    shooterMotor.set(ControlMode.PercentOutput, power)
  }

}
