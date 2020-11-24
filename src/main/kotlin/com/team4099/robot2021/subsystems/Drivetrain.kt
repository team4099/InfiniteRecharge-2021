package com.team4099.robot2021.subsystems

import com.analog.adis16470.frc.ADIS16470_IMU
import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.units.*
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.Radian
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.radians
import com.team4099.robot2021.Robot
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlin.math.*

object Drivetrain : SubsystemBase() {
  private val wheels = listOf(
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.FRONT_LEFT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.FRONT_LEFT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.FRONT_LEFT_CANCODER_ID)
    ),
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.FRONT_RIGHT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.FRONT_RIGHT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.FRONT_RIGHT_CANCODER_ID)
    ),
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.BACK_LEFT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.BACK_LEFT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.BACK_LEFT_CANCODER_ID)
    ),
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.BACK_RIGHT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.BACK_RIGHT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.BACK_RIGHT_CANCODER_ID)
    )
  )

  private val wheelSpeeds =
    mutableListOf(0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond)

  private val gyro = ADIS16470_IMU()
  val gyroAngle: Angle
    get() {
      var rawAngle = gyro.angle
      rawAngle += Constants.Drivetrain.GYRO_RATE_COEFFICIENT * gyro.rate
      return rawAngle.IEEErem(360.0).degrees
    }

  var isFieldOriented = true

  init {

  }

  fun set(angularVelocity: AngularVelocity, driveVector: Pair<LinearVelocity, LinearVelocity>) {
    val vX = if (isFieldOriented) {
      driveVector.first * cos(angularVelocity.inRadiansPerSecond) -
        driveVector.second * sin(angularVelocity.inRadiansPerSecond)
    } else {
      driveVector.first
    }
    val vY = if (isFieldOriented) {
      driveVector.second * cos(angularVelocity.inRadiansPerSecond) +
        driveVector.first * sin(angularVelocity.inRadiansPerSecond)
    } else {
      driveVector.second
    }
    val a =
      vX - Constants.Drivetrain.DRIVETRAIN_LENGTH.perSecond / 2 * angularVelocity.inRadiansPerSecond
    val b =
      vX + Constants.Drivetrain.DRIVETRAIN_LENGTH.perSecond / 2 * angularVelocity.inRadiansPerSecond
    val c =
      vY - Constants.Drivetrain.DRIVETRAIN_WIDTH.perSecond / 2 * angularVelocity.inRadiansPerSecond
    val d =
      vY + Constants.Drivetrain.DRIVETRAIN_WIDTH.perSecond / 2 * angularVelocity.inRadiansPerSecond

    wheelSpeeds[0] = hypot(b, d)
    wheelSpeeds[1] = hypot(b, c)
    wheelSpeeds[2] = hypot(a, d)
    wheelSpeeds[3] = hypot(a, c)

    val maxWheelSpeed = wheelSpeeds.max()
    if (maxWheelSpeed != null && maxWheelSpeed > Constants.Drivetrain.DRIVE_SETPOINT_MAX) {
      for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
        wheelSpeeds[i] = wheelSpeeds[i] / maxWheelSpeed.inMetersPerSecond
      }
    }

    wheels[0].set(atan2(b, d), wheelSpeeds[0])
    wheels[1].set(atan2(b, c), wheelSpeeds[1])
    wheels[2].set(atan2(a, d), wheelSpeeds[2])
    wheels[3].set(atan2(a, c), wheelSpeeds[3])
  }

  fun hypot(a: LinearVelocity, b: LinearVelocity): LinearVelocity {
    return kotlin.math.hypot(a.inMetersPerSecond, b.inMetersPerSecond).meters.perSecond
  }

  fun atan2(a: LinearVelocity, b: LinearVelocity): Angle {
    return kotlin.math.atan2(a.inMetersPerSecond, b.inMetersPerSecond).radians
  }
}
