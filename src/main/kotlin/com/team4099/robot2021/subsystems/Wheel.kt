package com.team4099.robot2021.subsystems

import com.ctre.phoenix.sensors.CANCoder
import com.ctre.phoenix.sensors.SensorInitializationStrategy
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.AngularMechanismSensor
import com.team4099.lib.units.LinearAcceleration
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.Timescale
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.ElectricalPotential
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.derived.volts
import com.team4099.lib.units.inFeetPerSecond
import com.team4099.lib.units.perSecond
import com.team4099.lib.units.sparkMaxAngularMechanismSensor
import com.team4099.lib.units.sparkMaxLinearMechanismSensor
import com.team4099.robot2021.config.Constants
import kotlin.math.IEEErem
import kotlin.math.withSign

class Wheel(
  private val directionSpark: CANSparkMax,
  private val driveSpark: CANSparkMax,
  private val encoder: CANCoder,
  private val zeroOffset: Angle,
  private val label: String
) {

  private val directionPID = directionSpark.pidController
  private val drivePID = driveSpark.pidController

  private val directionSensor =
      sparkMaxAngularMechanismSensor(
          directionSpark, Constants.Drivetrain.DIRECTION_SENSOR_GEAR_RATIO)
  private val driveSensor =
      sparkMaxLinearMechanismSensor(
          driveSpark, Constants.Drivetrain.DRIVE_SENSOR_GEAR_RATIO, 3.inches)

  private val directionAbsolute =
      AngularMechanismSensor(
          Constants.Drivetrain.ABSOLUTE_GEAR_RATIO,
          Timescale.CTRE,
          { encoder.velocity },
          { Math.toRadians(encoder.absolutePosition) })

  // motor params
  private val driveTemp: Double
    get() = driveSpark.motorTemperature

  private val directionTemp: Double
    get() = directionSpark.motorTemperature

  private val driveOutputCurrent: Double
    get() = directionSpark.outputCurrent

  private val directionOutputCurrent: Double
    get() = directionSpark.outputCurrent

  private val drivePercentOutput: Double
    get() = driveSpark.get()

  private val directionPercentOutput: Double
    get() = directionSpark.get()

  private val driveBusVoltage: ElectricalPotential
    get() = driveSpark.busVoltage.volts

  private val directionBusVoltage: ElectricalPotential
    get() = directionSpark.busVoltage.volts

  val driveOutputVoltage: ElectricalPotential
    get() = driveBusVoltage * drivePercentOutput

  val driveDistance: Length
    get() = driveSensor.position

  val driveVelocity: LinearVelocity
    get() = driveSensor.velocity

  private var speedSetPoint: LinearVelocity = 0.feet.perSecond

  private var directionSetPoint: Angle = 0.degrees
    set(value) {
      // Logger.addEvent("Drivetrain", "label: $label, value: ${value.inDegrees}, reference raw
      // position: ${directionSensor.positionToRawUnits(value)}, current raw position:
      // ${directionSensor.getRawPosition()}")
      directionPID.setReference(directionSensor.positionToRawUnits(value), ControlType.kSmartMotion)
      field = value
    }

  init {
    driveSpark.restoreFactoryDefaults()
    directionSpark.restoreFactoryDefaults()

    driveSpark.clearFaults()
    directionSpark.clearFaults()

    Logger.addSource("$label Drivetrain", "Drive Faults") { driveSpark.faults }
    Logger.addSource("$label Drivetrain", "Direction Faults") { directionSpark.faults }

    Logger.addSource("$label Drivetrain", "Drive Output Current") { driveOutputCurrent }
    Logger.addSource("$label Drivetrain", "Direction Output Current") { directionOutputCurrent }

    Logger.addSource("$label Drivetrain", "Drive Temperature") { driveTemp }
    Logger.addSource("$label Drivetrain", "Direction Temperature") { directionTemp }

    Logger.addSource("$label Drivetrain", "Drive Percent Output") { drivePercentOutput }
    Logger.addSource("$label Drivetrain", "Direction Percent Output") { directionPercentOutput }

    Logger.addSource("$label Drivetrain", "Drive Bus Voltage") { driveBusVoltage }
    Logger.addSource("$label Drivetrain", "Direction Bus Voltage") { directionBusVoltage }

    Logger.addSource("$label Drivetrain", "Drive SetPoint") { speedSetPoint.inFeetPerSecond }
    Logger.addSource("$label Drivetrain", "Direction SetPoint") { directionSetPoint.inDegrees }

    directionPID.p = Constants.Drivetrain.PID.DIRECTION_KP
    directionPID.i = Constants.Drivetrain.PID.DIRECTION_KI
    directionPID.iZone = 0.0
    directionPID.d = Constants.Drivetrain.PID.DIRECTION_KD
    directionPID.ff = Constants.Drivetrain.PID.DIRECTION_KFF
    directionPID.setSmartMotionMaxVelocity(
        directionSensor.velocityToRawUnits(Constants.Drivetrain.DIRECTION_VEL_MAX), 0)
    directionPID.setSmartMotionMaxAccel(
        directionSensor.accelerationToRawUnits(Constants.Drivetrain.DIRECTION_ACCEL_MAX), 0)
    directionPID.setOutputRange(-1.0, 1.0)
    directionPID.setSmartMotionMinOutputVelocity(0.0, 0)
    directionPID.setSmartMotionAllowedClosedLoopError(
        directionSensor.positionToRawUnits(Constants.Drivetrain.ALLOWED_ANGLE_ERROR), 0)
    directionSpark.setSmartCurrentLimit(Constants.Drivetrain.DIRECTION_SMART_CURRENT_LIMIT)

    directionSpark.burnFlash()

    drivePID.p = Constants.Drivetrain.PID.DRIVE_KP
    drivePID.i = Constants.Drivetrain.PID.DRIVE_KI
    drivePID.d = Constants.Drivetrain.PID.DRIVE_KD
    drivePID.ff = Constants.Drivetrain.PID.DRIVE_KFF
    driveSpark.setSmartCurrentLimit(Constants.Drivetrain.DRIVE_SMART_CURRENT_LIMIT)
    driveSpark.burnFlash()
  }

  fun set(
    direction: Angle,
    speed: LinearVelocity,
    acceleration: LinearAcceleration = 0.0.meters.perSecond.perSecond
  ) {
    if (speed == 0.feet.perSecond) {
      driveSpark.set(speed / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
    }
    var directionDifference =
        (direction - directionSensor.position).inRadians.IEEErem(2 * Math.PI).radians

    val isInverted = directionDifference.absoluteValue > (Math.PI / 2).radians
    if (isInverted) {
      directionDifference -= Math.PI.withSign(directionDifference.inRadians).radians
    }

    speedSetPoint =
        if (isInverted) {
          speed * -1
        } else {
          speed
        }
    directionSetPoint = directionSensor.position + directionDifference
    driveSpark.set(speedSetPoint / Constants.Drivetrain.DRIVE_SETPOINT_MAX)

    /*if(acceleration == 0.0.meters.perSecond.perSecond) {
      directionPID.setReference(
        directionSensor.positionToRawUnits(directionSetPoint),
        ControlType.kSmartMotion
        )

    } else {
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

     */
  }

  fun setOpenLoop(
    direction: Angle,
    speed: Double
  ) {
    var directionDifference =
      (direction - directionSensor.position).inRadians.IEEErem(2 * Math.PI).radians

    val isInverted = directionDifference.absoluteValue > (Math.PI / 2).radians
    if (isInverted) {
      directionDifference -= Math.PI.withSign(directionDifference.inRadians).radians
    }

    val outputPower =
      if (isInverted) {
        speed * -1
      } else {
        speed
      }
    directionSetPoint = directionSensor.position + directionDifference
    driveSpark.set(outputPower)
  }

  fun resetModuleZero() {
    encoder.configFactoryDefault()
    encoder.configMagnetOffset(0.0)
    encoder.configMagnetOffset(
        -encoder.absolutePosition - zeroOffset.inDegrees - encoder.configGetMagnetOffset())
    encoder.setPositionToAbsolute()
    encoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition)
  }

  fun zeroDirection() {
    directionSpark.encoder.position =
        directionSensor.positionToRawUnits(encoder.absolutePosition.degrees + zeroOffset)
  }

  fun zeroDrive() {
    driveSpark.encoder.position = 0.0
  }
}
