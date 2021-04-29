package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.ctreAngularMechanismSensor
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.inRotationsPerMinute
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

    shooterFollower.follow(shooterMotor)

    shooterMotor.config_kP(0, Constants.Shooter.SHOOTER_KP, 0)
    shooterMotor.config_kI(0, Constants.Shooter.SHOOTER_KI, 0)
    shooterMotor.config_kD(0, Constants.Shooter.SHOOTER_KD, 0)

    Logger.addSource("Shooter", "Shooter Current Velocity (rpm)") {
      currentVelocity.inRotationsPerMinute
    }
    Logger.addSource("Shooter", "Shooter Target Velocity (rpm)") {
      targetVelocity.inRotationsPerMinute
    }

    Logger.addSource("Shooter", "Shooter Motor Power") { shooterMotor.motorOutputPercent }
    Logger.addSource("Shooter", "Shooter Motor Stator Current") { shooterMotor.statorCurrent }
    Logger.addSource("Shooter", "Shooter Motor Supply Current") { shooterMotor.supplyCurrent }
    Logger.addSource("Shooter", "Shooter Motor Voltage") { shooterMotor.motorOutputVoltage }

    if (RobotBase.isSimulation()) {
      simPhysics = FlywheelSim(DCMotor.getFalcon500(2), Constants.Shooter.GEAR_RATIO, Constants.Shooter.FLYWHEEL_MOI)
      shooterMotor.simCollection.setQuadratureRawPosition(0)
    }
  }

  override fun simulationPeriodic() {
    println("Shooter commanded output: ${shooterMotor.motorOutputPercent} voltage: ${RobotController.getBatteryVoltage()}")
    simPhysics.setInput(shooterMotor.motorOutputPercent * RobotController.getBatteryVoltage())
    simPhysics.update(0.020)

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
          (Constants.Shooter.SHOOTER_KS + Constants.Shooter.SHOOTER_KV * velocity.value))
    }
    get() = _targetVelocity

  fun setOpenLoopPower(power: Double) {
    _targetVelocity = 0.rotations.perMinute
    println("shooter output: $power")
    shooterMotor.set(ControlMode.PercentOutput, power)
  }

}
