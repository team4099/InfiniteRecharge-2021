package com.team4099.robot2021.subsystems

import com.ctre.phoenix.sensors.CANCoder
import com.ctre.phoenix.sensors.SensorInitializationStrategy
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.*
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.derived.*
import com.team4099.robot2021.config.Constants
import kotlin.math.IEEErem
import kotlin.math.withSign

class Wheel(private val directionSpark: CANSparkMax, private val driveSpark: CANSparkMax,  private val encoder: CANCoder, private val zeroOffset: Angle, public val label: String) {

  private val directionPID = directionSpark.pidController
  private val drivePID = driveSpark.pidController

  private val directionSensor = sparkMaxAngularMechanismSensor(directionSpark, Constants.Drivetrain.DIRECTION_SENSOR_GEAR_RATIO)
  private val driveSensor = sparkMaxLinearMechanismSensor(driveSpark, Constants.Drivetrain.DRIVE_SENSOR_GEAR_RATIO, 3.inches)

  private val directionAbsolute = AngularMechanismSensor(Constants.Drivetrain.ABSOLUTE_GEAR_RATIO,Timescale.CTRE, { encoder.velocity }, { Math.toRadians(encoder.absolutePosition) })

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
      //Logger.addEvent("Drivetrain", "label: $label, value: ${value.inDegrees}, reference raw position: ${directionSensor.positionToRawUnits(value)}, current raw position: ${directionSensor.getRawPosition()}")
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
    directionPID.setIZone(0.0)
    directionPID.setSmartMotionMinOutputVelocity(0.0, 0)
    directionPID.setSmartMotionAllowedClosedLoopError(5.0, 0)

    directionSpark.burnFlash()

    drivePID.p = Constants.Drivetrain.PID.DRIVE_KP
    drivePID.i = Constants.Drivetrain.PID.DRIVE_KI
    drivePID.d = Constants.Drivetrain.PID.DRIVE_KD
    drivePID.ff = Constants.Drivetrain.PID.DRIVE_KFF
    driveSpark.burnFlash()
  }


  fun set(direction: Angle, speed: LinearVelocity) {
    if(speed == 0.feet.perSecond){
      speedSetPoint = 0.feet.perSecond
    }
    var directionDifference =
      (direction - directionSensor.position).inRadians.IEEErem(2 * Math.PI).radians

    val isInverted = directionDifference.absoluteValue > (Math.PI / 2).radians
    if (isInverted) {
      directionDifference -= Math.PI.withSign(directionDifference.inRadians).radians
    }
    speedSetPoint = if (isInverted) { speed * -1 } else { speed }
    directionSetPoint = direction + directionDifference
    Logger.addEvent("Drivetrain", "label: $label, direction sensor: ${directionSensor.position.inDegrees}")
  }

  fun setVelocitySetpoint(velocity: LinearVelocity, acceleration: LinearAcceleration) {
    drivePID.setReference(
      driveSensor.velocityToRawUnits(velocity),
      ControlType.kVelocity,
      0,
      (Constants.Drivetrain.PID.DRIVE_KS +
        velocity * Constants.Drivetrain.PID.DRIVE_KV +
        acceleration * Constants.Drivetrain.PID.DRIVE_KA).inVolts,
      CANPIDController.ArbFFUnits.kVoltage
    )
  }

  fun resetModuleZero () {
    encoder.configFactoryDefault()
    encoder.configMagnetOffset(0.0)
    Logger.addEvent("Drivetrain", "label: $label, offset: ${+encoder.absolutePosition + zeroOffset.inDegrees - encoder.configGetMagnetOffset()}, position: ${encoder.position}, lasttimestamp: ${encoder.lastTimestamp}, absolute position: ${encoder.absolutePosition}")
    encoder.configMagnetOffset(-encoder.absolutePosition - zeroOffset.inDegrees - encoder.configGetMagnetOffset())
    encoder.setPositionToAbsolute()
    encoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition)
  }

  fun zeroDirection(){
    directionSpark.encoder.position = directionSensor.positionToRawUnits((encoder.absolutePosition.degrees + zeroOffset))
    Logger.addEvent("DriveTrain", "label: $label, encoder position: ${directionSensor.positionToRawUnits(directionAbsolute.position + zeroOffset)}, absolute position, ${directionAbsolute.position}, zero offset, $zeroOffset, encoder absolute position: ${encoder.absolutePosition.degrees}, encoder abs pos zer off: ${(encoder.absolutePosition.degrees + zeroOffset).inDegrees}")
  }

  fun zeroDrive(){
    driveSpark.encoder.position = 0.0
  }
}
