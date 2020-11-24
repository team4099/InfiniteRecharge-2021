package com.team4099.robot2021.subsystems

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import com.revrobotics.SparkMax
import com.team4099.lib.units.*
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.degrees

class Wheel(private val directionSpark: CANSparkMax, private val driveSpark: CANSparkMax,  private val encoder: CANCoder) {

  private val directionPID = directionSpark.pidController
  private val drivePID = driveSpark.pidController

  private val directionSensor = sparkMaxAngularMechanismSensor(directionSpark, 1.0)
  private val driveSensor = sparkMaxLinearMechanismSensor(driveSpark, 1.0, 3.inches)

  private var speedSetPoint: LinearVelocity = 0.feet.perSecond
    set(value) {
      drivePID.setReference(driveSensor.velocityToRawUnits(value), ControlType.kVelocity)
      field = value
    }
  private var angleSetPoint: Angle = 0.degrees
    set(value) {
      directionPID.setReference(directionSensor.positionToRawUnits(value), ControlType.kSmartMotion)
      field = value
    }

  fun set(direction: Angle, speed: LinearVelocity){
    TODO("do this")
  }
}
