package com.team4099.robot2021.subsystems

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import com.revrobotics.SparkMax
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

  private val directionSensor = sparkMaxAngularMechanismSensor(directionSpark, 1.0)
  private val driveSensor = sparkMaxLinearMechanismSensor(driveSpark, 1.0, 3.inches)

  private val directionAbsolute = AngularMechanismSensor(1.0,Timescale.CTRE,{encoder.velocity},{encoder.absolutePosition})

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
      drivePID.setReference(driveSensor.velocityToRawUnits(value), ControlType.kVelocity)
      field = value
    }
  private var directionSetPoint: Angle = 0.degrees
    set(value) {
      directionPID.setReference(directionSensor.positionToRawUnits(value), ControlType.kSmartMotion)
      field = value
    }

  init {
    Logger.addSource("Drivetrain", "$label Drive Output Current") { driveOutputCurrent}
    Logger.addSource("Drivetrain", "$label Direction Output Current") { directionOutputCurrent }

    Logger.addSource("Drivetrain", "$label Drive Temperature") { driveTemp }
    Logger.addSource("Drivetrain", "$label Direction Temperature") { directionTemp }

    Logger.addSource("Drivetrain", "$label Drive Percent Output") { drivePercentOutput }
    Logger.addSource("Drivetrain", "$label Direction Percent Output") { directionPercentOutput }

    Logger.addSource("Drivetrain", "$label Drive Bus Voltage") { driveBusVoltage }
    Logger.addSource("Drivetrain", "$label Direction Bus Voltage") { directionBusVoltage }

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
  }

  fun zeroDirection(){
    directionSpark.encoder.position = directionSensor.positionToRawUnits(directionAbsolute.position + zeroOffset)
  }

  fun zeroDrive(){
    driveSpark.encoder.position = 0.0
  }
}
