package com.team4099.robot2021.subsystems

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import com.revrobotics.SparkMax
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.*
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.*
import com.team4099.robot2021.config.Constants
import kotlin.math.IEEErem
import kotlin.math.sign
import kotlin.math.withSign

class Wheel(private val directionSpark: CANSparkMax, private val driveSpark: CANSparkMax,  private val encoder: CANCoder, private val zeroOffset: Angle, public val label: String) {

  private val directionPID = directionSpark.pidController
  private val drivePID = driveSpark.pidController

  private val directionSensor = sparkMaxAngularMechanismSensor(directionSpark, Constants.Drivetrain.DIRECTION_SENSOR_GEAR_RATIO)
  private val driveSensor = sparkMaxLinearMechanismSensor(driveSpark, Constants.Drivetrain.DRIVE_SENSOR_GEAR_RATIO, 3.inches)

  private val directionAbsolute = AngularMechanismSensor(Constants.Drivetrain.ABSOLUTE_GEAR_RATIO,Timescale.CTRE, { encoder.velocity }, { encoder.absolutePosition })

  // motor params
  private val driveTemp: Double
    get() = driveSpark.getMotorTemperature()

  private val directionTemp: Double
    get() = directionSpark.getMotorTemperature()

  private val driveOutputCurrent: Double
    get() = directionSpark.getOutputCurrent()

  private val directionOutputCurrent: Double
    get() = directionSpark.getOutputCurrent()

  private val drivePercentOutput: Double
    get() = driveSpark.get()

  private val directionPercentOutput: Double
    get() = directionSpark.get()

  private val driveBusVoltage: Double
    get() = driveSpark.getBusVoltage()

  private val directionBusVoltage: Double
    get() = directionSpark.getBusVoltage()


  private var speedSetPoint: LinearVelocity = 0.feet.perSecond
    set(value) {
      driveSpark.set(value / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
//      drivePID.setReference(driveSensor.velocityToRawUnits(value), ControlType.kVelocity)
      field = value
    }
  private var directionSetPoint: Angle = 0.degrees
    set(value) {
      directionPID.setReference(directionSensor.positionToRawUnits(value), ControlType.kSmartMotion)
      field = value
    }

  init {
    directionSpark.restoreFactoryDefaults()
    driveSpark.restoreFactoryDefaults()

    Logger.addSource("Drivetrain", "$label Drive Output Current") { driveOutputCurrent }
    Logger.addSource("Drivetrain", "$label Direction Output Current") { directionOutputCurrent }

    Logger.addSource("Drivetrain", "$label Drive Temperature") { driveTemp }
    Logger.addSource("Drivetrain", "$label Direction Temperature") { directionTemp }

    Logger.addSource("Drivetrain", "$label Drive Percent Output") { drivePercentOutput }
    Logger.addSource("Drivetrain", "$label Direction Percent Output") { directionPercentOutput }

    Logger.addSource("Drivetrain", "$label Drive Bus Voltage") { driveBusVoltage }
    Logger.addSource("Drivetrain", "$label Direction Bus Voltage") { directionBusVoltage }

    directionPID.p = Constants.Drivetrain.PID.DIRECTION_KP
    directionPID.i = Constants.Drivetrain.PID.DIRECTION_KI
    directionPID.d = Constants.Drivetrain.PID.DIRECTION_KD
    directionPID.ff = Constants.Drivetrain.PID.DIRECTION_KFF
    directionPID.setSmartMotionMaxVelocity(directionSensor.velocityToRawUnits(Constants.Drivetrain.DIRECTION_VEL_MAX), 0)
    directionPID.setSmartMotionMaxAccel(directionSensor.accelerationToRawUnits(Constants.Drivetrain.DIRECTION_ACCEL_MAX), 0)
    directionPID.setOutputRange(-1.0, 1.0)
    directionSpark.burnFlash()

    drivePID.p = Constants.Drivetrain.PID.DRIVE_KP
    drivePID.i = Constants.Drivetrain.PID.DRIVE_KI
    drivePID.d = Constants.Drivetrain.PID.DRIVE_KD
    drivePID.ff = Constants.Drivetrain.PID.DRIVE_KFF
    driveSpark.burnFlash()
  }


  fun set(direction: Angle, speed: LinearVelocity, acceleration: LinearAcceleration ?= 0.0.meters.perSecond.perSecond) {
    if(speed == 0.feet.perSecond){
      driveSpark.set(speed / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
    }
    var directionDifference =
      (direction - directionSensor.position).inRadians.IEEErem(2 * Math.PI).radians

    val isInverted = directionDifference.absoluteValue > (Math.PI / 2).radians
    if (isInverted) {
      directionDifference -= Math.PI.withSign(directionDifference.inRadians).radians
    }
    driveSpark.set(if (isInverted) { speed * -1 } else { speed } / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
    if(acceleration == 0.0.meters.perSecond.perSecond) {
      directionPID.setReference(
        directionSensor.positionToRawUnits(direction + directionDifference),
        ControlType.kSmartMotion
      )
    } else {
      if (acceleration != null) {
        drivePID.setReference(
          driveSensor.velocityToRawUnits(speed),
          ControlType.kVelocity,
          0,
          (Constants.Drivetrain.PID.DRIVE_KS * sign(speed.value) +
            speed * Constants.Drivetrain.PID.DRIVE_KV +
            acceleration * Constants.Drivetrain.PID.DRIVE_KA).inVolts,
          CANPIDController.ArbFFUnits.kVoltage
        )
      }
    }
  }

  fun setVelocitySetpoint(velocity: LinearVelocity, acceleration: LinearAcceleration) {
    drivePID.setReference(
      driveSensor.velocityToRawUnits(velocity),
      ControlType.kVelocity,
      0,
      (Constants.Drivetrain.PID.DRIVE_KS * sign(velocity.value) +
        velocity * Constants.Drivetrain.PID.DRIVE_KV +
        acceleration * Constants.Drivetrain.PID.DRIVE_KA).inVolts,
      CANPIDController.ArbFFUnits.kVoltage
    )
  }

  fun resetModuleZero () {
    encoder.configFactoryDefault()
    encoder.configMagnetOffset(-encoder.position + zeroOffset.inDegrees)
  }

  fun zeroDirection(){
    directionSpark.encoder.position = directionSensor.positionToRawUnits(directionAbsolute.position + zeroOffset)
  }

  fun zeroDrive(){
    driveSpark.encoder.position = 0.0
  }
}
