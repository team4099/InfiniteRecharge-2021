package com.team4099.robot2021.subsystems

import com.analog.adis16470.frc.ADIS16470_IMU
import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.geometry.*
import com.team4099.lib.hal.Clock
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.*
import com.team4099.lib.units.base.*
import com.team4099.lib.units.derived.*
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.kinematics.*
import edu.wpi.first.wpilibj.trajectory.Trajectory
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
      CANCoder(Constants.Drivetrain.FRONT_LEFT_CANCODER_ID),
      0.degrees,
      "Front Left Wheel"
    ),
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.FRONT_RIGHT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.FRONT_RIGHT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.FRONT_RIGHT_CANCODER_ID),
      (-90).degrees,
      "Front Right Wheel"
    ),
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.BACK_LEFT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.BACK_LEFT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.BACK_LEFT_CANCODER_ID),
      (-180).degrees,
      "Back Left Wheel"
    ),
    Wheel(
      CANSparkMax(
        Constants.Drivetrain.BACK_RIGHT_DIRECTION_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANSparkMax(
        Constants.Drivetrain.BACK_RIGHT_SPEED_ID, CANSparkMaxLowLevel.MotorType.kBrushless
      ),
      CANCoder(Constants.Drivetrain.BACK_RIGHT_CANCODER_ID),
      (-270).degrees,
      "Back Right Wheel"
    )
  )

  private val wheelSpeeds =
    mutableListOf(0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond)

  private val wheelAngles =
    mutableListOf(0.radians, 0.radians, 0.radians, 0.radians)

  private val gyro = ADIS16470_IMU()

  private val gyroAngle: Angle
    get() {
      var rawAngle = gyro.angle
      rawAngle += Constants.Drivetrain.GYRO_RATE_COEFFICIENT * gyro.rate
      return rawAngle.IEEErem(360.0).degrees
    }


  private val frontLeftWheelLocation = Translation(
    -Constants.Drivetrain.DRIVETRAIN_WIDTH / 2,
    Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
  )
  private val frontRightWheelLocation = Translation(
    Constants.Drivetrain.DRIVETRAIN_WIDTH / 2,
    Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
  )
  private val backLeftWheelLocation = Translation(
    -Constants.Drivetrain.DRIVETRAIN_WIDTH / 2,
    -Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
  )
  private val backRightWheelLocation = Translation(
    Constants.Drivetrain.DRIVETRAIN_WIDTH / 2,
    -Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
  )

  var swerveDriveKinematics = SwerveDriveKinematics(
    frontLeftWheelLocation.translation2d,
    frontRightWheelLocation.translation2d,
    backLeftWheelLocation.translation2d,
    backRightWheelLocation.translation2d
  )

  private var swerveDriveOdometry = SwerveDriveOdometry(
    swerveDriveKinematics,
    gyroAngle.inRotation2ds,
    Pose(0.meters, 0.meters, 0.degrees).pose2d
  )

  private var trajDuration = 0.0.seconds
  private var trajCurTime = 0.0.seconds
  private var trajStartTime = 0.0.seconds

  private lateinit var lastModuleSpeeds: Array<SwerveModuleState>

  private var pathFollowController = RamseteController()
  var path: Trajectory = Trajectory(listOf(Trajectory.State()))
    set(value) {
      trajDuration = value.totalTimeSeconds.seconds
      trajStartTime = Clock.fpgaTime

      val initialSample = value.sample(0.0)
      swerveDriveOdometry.resetPosition(initialSample.poseMeters, -gyroAngle.inRotation2ds)
      pathFollowController = RamseteController(
        Constants.Drivetrain.Gains.RAMSETE_B,
        Constants.Drivetrain.Gains.RAMSETE_ZETA
      )

      Logger.addEvent("Drivetrain", "Path Following Started")

      field = value
    }

  init {
    // Wheel speeds
    Logger.addSource("Drivetrain", "Front Left Wheel Speed") { wheelSpeeds[0] }
    Logger.addSource("Drivetrain", "Front Right Wheel Speed") { wheelSpeeds[1] }
    Logger.addSource("Drivetrain", "Back Left Wheel Speed") { wheelSpeeds[2] }
    Logger.addSource("Drivetrain", "Back Right Wheel Speed") { wheelSpeeds[3] }

    // Wheel angles
    Logger.addSource("Drivetrain", "Front Left Wheel Angles") { wheelAngles[0] }
    Logger.addSource("Drivetrain", "Front Right Wheel Angles") { wheelAngles[1] }
    Logger.addSource("Drivetrain", "Back Left Wheel Angles") { wheelAngles[2] }
    Logger.addSource("Drivetrain", "Back Right Wheel Angles") { wheelAngles[3] }

    //  gyro angle
    Logger.addSource("Drivetrain", "Gyro Angle") { gyroAngle }

    Logger.addSource("Drivetrain", "Path Follow Start Timestamp") { trajStartTime }
    Logger.addSource("Drivetrain", "Path Follow Duration") { trajDuration }
    Logger.addSource("Drivetrain", "Path Follow Current Timestamp") { trajCurTime }

    //  if gyro is connected boolean
    Logger.addSource("Drivetrain", "Gyro Connected") { }
    zeroDirection()
  }

  /**
   * Sets the drivetrain to the specified angular and X & Y velocities.
   * Calculates angular and linear velocities and calls set for each Wheel object.
   *
   * @param angularVelocity The angular velocity of a specified drive
   * @param driveVector.first The linear velocity on the X axis
   * @param driveVector.second The linear velocity on the Y axis
   *
   */
  fun set(
    angularVelocity: AngularVelocity,
    driveVector: Pair<LinearVelocity, LinearVelocity>,
    fieldOriented: Boolean = true
  ) {
    val vX = if (fieldOriented) {
      driveVector.first * gyroAngle.cos -
        driveVector.second * gyroAngle.sin
    } else {
      driveVector.first
    }
    val vY = if (fieldOriented) {
      driveVector.second * gyroAngle.cos +
        driveVector.first * gyroAngle.sin
    } else {
      driveVector.second
    }

    val a =
      vX - angularVelocity * Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
    val b =
      vX + angularVelocity * Constants.Drivetrain.DRIVETRAIN_LENGTH / 2
    val c =
      vY - angularVelocity * Constants.Drivetrain.DRIVETRAIN_WIDTH / 2
    val d =
      vY + angularVelocity * Constants.Drivetrain.DRIVETRAIN_WIDTH / 2

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
    wheelAngles[0] = atan2(b, d)
    wheelAngles[1] = atan2(b, c)
    wheelAngles[2] = atan2(a, d)
    wheelAngles[3] = atan2(a, c)

    wheels[0].set(wheelAngles[0], wheelSpeeds[0])
    wheels[1].set(wheelAngles[1], wheelSpeeds[1])
    wheels[2].set(wheelAngles[2], wheelSpeeds[2])
    wheels[3].set(wheelAngles[3], wheelSpeeds[3])

  }

  fun updateOdometry() {
    swerveDriveOdometry.update(
      gyroAngle.inRotation2ds,
      SwerveModuleState(wheelSpeeds[0].inMetersPerSecond, wheelAngles[0].inRotation2ds),
      SwerveModuleState(wheelSpeeds[1].inMetersPerSecond, wheelAngles[1].inRotation2ds),
      SwerveModuleState(wheelSpeeds[2].inMetersPerSecond, wheelAngles[2].inRotation2ds),
      SwerveModuleState(wheelSpeeds[3].inMetersPerSecond, wheelAngles[3].inRotation2ds)
    )
  }

  fun updatePathFollowing(timestamp: Time) {
    trajCurTime = timestamp - trajStartTime
    val sample = path.sample(trajCurTime.inSeconds)

    val drivetrainSpeeds = pathFollowController.calculate(swerveDriveOdometry.poseMeters, sample)
    // Note: ChassisSpeeds takes x as forward so it is swapped
    set(
      drivetrainSpeeds.omegaRadiansPerSecond.radians.perSecond,
      Pair(
        drivetrainSpeeds.vyMetersPerSecond.meters.perSecond,
        drivetrainSpeeds.vxMetersPerSecond.meters.perSecond
      ),
      fieldOriented = false
    )
  }


  /**
   * Checks if path following has reached the end of the path.
   *
   * @param timestamp The current time. Value originates from Timer.getFPGATimestamp.
   * @return If path following is finished.
   */
  fun isPathFinished(timestamp: Time): Boolean {
    trajCurTime = timestamp - trajStartTime
    return trajCurTime > trajDuration
  }

  private fun hypot(a: LinearVelocity, b: LinearVelocity): LinearVelocity {
    return kotlin.math.hypot(a.inMetersPerSecond, b.inMetersPerSecond).meters.perSecond
  }

  private fun atan2(a: LinearVelocity, b: LinearVelocity): Angle {
    return kotlin.math.atan2(a.inMetersPerSecond, b.inMetersPerSecond).radians
  }

  fun resetModuleZero() {
    wheels.forEach {
      it.resetModuleZero()
    }
  }

  fun zeroSensors() {
    gyro.reset()
    zeroDirection()
    zeroDrive()
  }

  private fun zeroDirection() {
    wheels.forEach {
      it.zeroDirection()
    }
  }

  private fun zeroDrive() {
    wheels.forEach {
      it.zeroDrive()
    }
  }
}
