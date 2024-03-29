package com.team4099.robot2021.subsystems

import com.analog.adis16470.frc.ADIS16470_IMU
import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.AngularAcceleration
import com.team4099.lib.units.AngularVelocity
import com.team4099.lib.units.LinearAcceleration
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inFeet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.cos
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.derived.inRotation2ds
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.derived.sin
import com.team4099.lib.units.derived.times
import com.team4099.lib.units.inFeetPerSecond
import com.team4099.lib.units.inMetersPerSecond
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics
import edu.wpi.first.wpilibj.kinematics.SwerveDriveOdometry
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlin.math.IEEErem

object Drivetrain : SubsystemBase() {
  val wheels =
      listOf(
          Wheel(
              CANSparkMax(
                  Constants.Drivetrain.FRONT_LEFT_DIRECTION_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANSparkMax(
                  Constants.Drivetrain.FRONT_LEFT_SPEED_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANCoder(Constants.Drivetrain.FRONT_LEFT_CANCODER_ID),
              0.degrees,
              "Front Left Wheel"),
          Wheel(
              CANSparkMax(
                  Constants.Drivetrain.FRONT_RIGHT_DIRECTION_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANSparkMax(
                  Constants.Drivetrain.FRONT_RIGHT_SPEED_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANCoder(Constants.Drivetrain.FRONT_RIGHT_CANCODER_ID),
              0.degrees,
              "Front Right Wheel"),
          Wheel(
              CANSparkMax(
                  Constants.Drivetrain.BACK_LEFT_DIRECTION_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANSparkMax(
                  Constants.Drivetrain.BACK_LEFT_SPEED_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANCoder(Constants.Drivetrain.BACK_LEFT_CANCODER_ID),
              0.degrees,
              "Back Left Wheel"),
          Wheel(
              CANSparkMax(
                  Constants.Drivetrain.BACK_RIGHT_DIRECTION_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANSparkMax(
                  Constants.Drivetrain.BACK_RIGHT_SPEED_ID,
                  CANSparkMaxLowLevel.MotorType.kBrushless),
              CANCoder(Constants.Drivetrain.BACK_RIGHT_CANCODER_ID),
              0.degrees,
              "Back Right Wheel"))

  private val wheelSpeeds =
      mutableListOf(0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond)

  private val wheelAngles = mutableListOf(0.radians, 0.radians, 0.radians, 0.radians)

  private val wheelAccelerations =
      mutableListOf(
          0.feet.perSecond.perSecond,
          0.feet.perSecond.perSecond,
          0.feet.perSecond.perSecond,
          0.feet.perSecond.perSecond)

  private val gyro = ADIS16470_IMU()

  val gyroAngle: Angle
    get() {
      var rawAngle = gyro.angle + gyroOffset.inDegrees
      rawAngle += Constants.Drivetrain.GYRO_RATE_COEFFICIENT * gyro.rate
      return rawAngle.IEEErem(360.0).degrees
    }
  var gyroOffset: Angle = 0.0.degrees

  private val frontLeftWheelLocation =
      Translation(
          -Constants.Drivetrain.DRIVETRAIN_WIDTH / 2, Constants.Drivetrain.DRIVETRAIN_LENGTH / 2)
  private val frontRightWheelLocation =
      Translation(
          Constants.Drivetrain.DRIVETRAIN_WIDTH / 2, Constants.Drivetrain.DRIVETRAIN_LENGTH / 2)
  private val backLeftWheelLocation =
      Translation(
          -Constants.Drivetrain.DRIVETRAIN_WIDTH / 2, -Constants.Drivetrain.DRIVETRAIN_LENGTH / 2)
  private val backRightWheelLocation =
      Translation(
          Constants.Drivetrain.DRIVETRAIN_WIDTH / 2, -Constants.Drivetrain.DRIVETRAIN_LENGTH / 2)

  val swerveDriveKinematics =
      SwerveDriveKinematics(
          frontLeftWheelLocation.translation2d,
          frontRightWheelLocation.translation2d,
          backLeftWheelLocation.translation2d,
          backRightWheelLocation.translation2d)

  private val swerveDriveOdometry =
      SwerveDriveOdometry(
          swerveDriveKinematics,
          gyroAngle.inRotation2ds,
          Pose(0.meters, 0.meters, 0.degrees).pose2d)

  var pose: Pose
    get() = Pose(swerveDriveOdometry.poseMeters)
    set(value) {
      swerveDriveOdometry.resetPosition(value.pose2d, gyroAngle.inRotation2ds)
    }

  init {
    // Wheel speeds
    Logger.addSource("Drivetrain", "Front Left Wheel Speed") { wheelSpeeds[0].inFeetPerSecond }
    Logger.addSource("Drivetrain", "Front Right Wheel Speed") { wheelSpeeds[1].inFeetPerSecond }
    Logger.addSource("Drivetrain", "Back Left Wheel Speed") { wheelSpeeds[2].inFeetPerSecond }
    Logger.addSource("Drivetrain", "Back Right Wheel Speed") { wheelSpeeds[3].inFeetPerSecond }

    // Wheel angles
    Logger.addSource("Drivetrain", "Front Left Wheel Angles") { wheelAngles[0].inDegrees }
    Logger.addSource("Drivetrain", "Front Right Wheel Angles") { wheelAngles[1].inDegrees }
    Logger.addSource("Drivetrain", "Back Left Wheel Angles") { wheelAngles[2].inDegrees }
    Logger.addSource("Drivetrain", "Back Right Wheel Angles") { wheelAngles[3].inDegrees }

    //  gyro angle
    Logger.addSource("Drivetrain", "Gyro Angle") { gyroAngle.inDegrees }

    //  if gyro is connected boolean
    Logger.addSource("Drivetrain", "Gyro Connected") {}

    Logger.addSource("Drivetrain", "pos x") { pose.x.inFeet }
    Logger.addSource("Drivetrain", "pos y") { pose.y.inFeet }

    zeroDirection()
  }

  override fun periodic() {
    this.updateOdometry()
  }

  /**
   * Sets the drivetrain to the specified angular and X & Y velocities. Calculates angular and
   * linear velocities and calls set for each Wheel object.
   *
   * @param angularVelocity The angular velocity of a specified drive
   * @param driveVector.first The linear velocity on the X axis
   * @param driveVector.second The linear velocity on the Y axis
   */
  fun set(
    angularVelocity: AngularVelocity,
    driveVector: Pair<LinearVelocity, LinearVelocity>,
    fieldOriented: Boolean = true,
    angularAcceleration: AngularAcceleration = 0.0.radians.perSecond.perSecond,
    driveAcceleration: Pair<LinearAcceleration, LinearAcceleration> =
        Pair(0.0.meters.perSecond.perSecond, 0.0.meters.perSecond.perSecond)
  ) {

    // Logger.addEvent("Drivetrain", "setting with $driveVector and $angularVelocity")
    //    Logger.addEvent("Drivetrain", "gyro angle: ${(-gyroAngle).inDegrees}")
    val vX =
        if (fieldOriented) {
          driveVector.first * (-gyroAngle).cos - driveVector.second * (-gyroAngle).sin
        } else {
          driveVector.first
        }
    val vY =
        if (fieldOriented) {
          driveVector.second * (-gyroAngle).cos + driveVector.first * (-gyroAngle).sin
        } else {
          driveVector.second
        }

    val aY =
        if (fieldOriented) {
          driveAcceleration.second * (-gyroAngle).cos + driveAcceleration.first * (-gyroAngle).sin
        } else {
          driveAcceleration.second
        }
    val aX =
        if (fieldOriented) {
          driveAcceleration.first * (-gyroAngle).cos - driveAcceleration.second * (-gyroAngle).sin
        } else {
          driveAcceleration.first
        }

    val a = vX - angularVelocity * Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
    val b = vX + angularVelocity * Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
    val c = vY - angularVelocity * Constants.Drivetrain.DRIVETRAIN_WIDTH / 2
    val d = vY + angularVelocity * Constants.Drivetrain.DRIVETRAIN_WIDTH / 2
    // Logger.addEvent("Drivetrain", "vX: $vX, angular velocity: $angularVelocity")

    wheelSpeeds[0] = hypot(b, d)
    wheelSpeeds[1] = hypot(b, c)
    wheelSpeeds[2] = hypot(a, d)
    wheelSpeeds[3] = hypot(a, c)

    val aA =
        aX -
            (angularAcceleration.value * Constants.Drivetrain.DRIVETRAIN_LENGTH.value / 2).inches
                .perSecond
                .perSecond
    val aB =
        aX +
            (angularAcceleration.value * Constants.Drivetrain.DRIVETRAIN_LENGTH.value / 2).inches
                .perSecond
                .perSecond
    val aC =
        aY -
            (angularAcceleration.value * Constants.Drivetrain.DRIVETRAIN_WIDTH.value / 2).inches
                .perSecond
                .perSecond
    val aD =
        aY -
            (angularAcceleration.value * Constants.Drivetrain.DRIVETRAIN_WIDTH.value / 2).inches
                .perSecond
                .perSecond

    wheelAccelerations[0] = kotlin.math.hypot(aB.value, aD.value).feet.perSecond.perSecond
    wheelAccelerations[1] = kotlin.math.hypot(aB.value, aC.value).feet.perSecond.perSecond
    wheelAccelerations[2] = kotlin.math.hypot(aA.value, aD.value).feet.perSecond.perSecond
    wheelAccelerations[3] = kotlin.math.hypot(aA.value, aC.value).feet.perSecond.perSecond

    val maxWheelSpeed = wheelSpeeds.max()
    if (maxWheelSpeed != null && maxWheelSpeed > Constants.Drivetrain.DRIVE_SETPOINT_MAX) {
      for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
        wheelSpeeds[i] = wheelSpeeds[i] / maxWheelSpeed.inMetersPerSecond
      }
    }
    wheelAngles[0] = atan2(b, d)
    wheelAngles[1] = atan2(b, c)
    wheelAngles[2] = atan2(a, d)
    wheelAngles[3] = atan2(a, c)
    //    Logger.addEvent("Drivetrain", "wheel angle: $wheelAngles")

    wheels[0].set(wheelAngles[0], wheelSpeeds[0], wheelAccelerations[0])
    wheels[1].set(wheelAngles[1], wheelSpeeds[1], wheelAccelerations[1])
    wheels[2].set(wheelAngles[2], wheelSpeeds[2], wheelAccelerations[2])
    wheels[3].set(wheelAngles[3], wheelSpeeds[3], wheelAccelerations[3])
  }

  fun setOpenLoop(
    angularVelocity: AngularVelocity,
    driveVector: Pair<LinearVelocity, LinearVelocity>,
    fieldOriented: Boolean = true
  ) {
    // Logger.addEvent("Drivetrain", "setting open loop with $driveVector and $angularVelocity")

    val vX =
        if (fieldOriented) {
          driveVector.first * (-gyroAngle).cos - driveVector.second * (-gyroAngle).sin
        } else {
          driveVector.first
        }
    val vY =
        if (fieldOriented) {
          driveVector.second * (-gyroAngle).cos + driveVector.first * (-gyroAngle).sin
        } else {
          driveVector.second
        }

    val a = vX - angularVelocity * Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
    val b = vX + angularVelocity * Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
    val c = vY - angularVelocity * Constants.Drivetrain.DRIVETRAIN_WIDTH / 2
    val d = vY + angularVelocity * Constants.Drivetrain.DRIVETRAIN_WIDTH / 2

    wheelSpeeds[0] = hypot(b, d)
    wheelSpeeds[1] = hypot(b, c)
    wheelSpeeds[2] = hypot(a, d)
    wheelSpeeds[3] = hypot(a, c)

    val maxWheelSpeed = wheelSpeeds.max()
    if (maxWheelSpeed != null && maxWheelSpeed > Constants.Drivetrain.DRIVE_SETPOINT_MAX) {
      for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
        wheelSpeeds[i] =
            wheelSpeeds[i] / maxWheelSpeed.inMetersPerSecond *
                Constants.Drivetrain.DRIVE_SETPOINT_MAX.inMetersPerSecond
      }
    }
    wheelAngles[0] = atan2(b, d)
    wheelAngles[1] = atan2(b, c)
    wheelAngles[2] = atan2(a, d)
    wheelAngles[3] = atan2(a, c)

    wheels[0].setOpenLoop(wheelAngles[0], wheelSpeeds[0] / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
    wheels[1].setOpenLoop(wheelAngles[1], wheelSpeeds[1] / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
    wheels[2].setOpenLoop(wheelAngles[2], wheelSpeeds[2] / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
    wheels[3].setOpenLoop(wheelAngles[3], wheelSpeeds[3] / Constants.Drivetrain.DRIVE_SETPOINT_MAX)
  }

  private fun updateOdometry() {
    swerveDriveOdometry.update(
        gyroAngle.inRotation2ds,
        SwerveModuleState(
            wheels[0].driveVelocity.inMetersPerSecond, wheels[0].directionPosition.inRotation2ds),
        SwerveModuleState(
            wheels[1].driveVelocity.inMetersPerSecond, wheels[1].directionPosition.inRotation2ds),
        SwerveModuleState(
            wheels[2].driveVelocity.inMetersPerSecond, wheels[2].directionPosition.inRotation2ds),
        SwerveModuleState(
            wheels[3].driveVelocity.inMetersPerSecond, wheels[3].directionPosition.inRotation2ds))
  }

  private fun hypot(a: LinearVelocity, b: LinearVelocity): LinearVelocity {
    return kotlin.math.hypot(a.inMetersPerSecond, b.inMetersPerSecond).meters.perSecond
  }

  private fun atan2(a: LinearVelocity, b: LinearVelocity): Angle {
    return kotlin.math.atan2(a.inMetersPerSecond, b.inMetersPerSecond).radians
  }

  fun resetModuleZero() {
    wheels.forEach { it.resetModuleZero() }
  }

  fun zeroSensors() {
    zeroGyro()
    zeroDirection()
    zeroDrive()
  }

  fun zeroGyro() {
    gyro.reset()
  }
  fun zeroGyro(offset: Angle) {
    zeroGyro()
    gyroOffset = offset
  }

  fun zeroDirection() {
    wheels.forEach { it.zeroDirection() }
  }

  private fun zeroDrive() {
    wheels.forEach { it.zeroDrive() }
  }
}
