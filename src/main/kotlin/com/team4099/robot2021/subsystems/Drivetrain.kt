package com.team4099.robot2021.subsystems

import com.analog.adis16470.frc.ADIS16470_IMU
import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.geometry.*
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.*
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.*
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Rotation2d
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
      "Back Right"
    )
  )

  private val wheelSpeeds =
    mutableListOf(0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond, 0.feet.perSecond)

  private val wheelAngles =
    mutableListOf(0.radians, 0.radians, 0.radians, 0.radians)

  private val gyro = ADIS16470_IMU()

  val gyroAngle: Angle
    get() {
      var rawAngle = gyro.angle
      rawAngle += Constants.Drivetrain.GYRO_RATE_COEFFICIENT * gyro.rate
      return rawAngle.IEEErem(360.0).degrees
    }

  var isFieldOriented = true

  private var frontLeftWheelLocation = Translation(-Constants.Drivetrain.DRIVETRAIN_WIDTH/2, Constants.Drivetrain.DRIVETRAIN_LENGTH/2)
  private var frontRightWheelLocation = Translation(Constants.Drivetrain.DRIVETRAIN_WIDTH/2, Constants.Drivetrain.DRIVETRAIN_LENGTH/2)
  private var backLeftWheelLocation = Translation(-Constants.Drivetrain.DRIVETRAIN_WIDTH/2, -Constants.Drivetrain.DRIVETRAIN_LENGTH/2)
  private var backRightWheelLocation = Translation(Constants.Drivetrain.DRIVETRAIN_WIDTH/2, -Constants.Drivetrain.DRIVETRAIN_LENGTH/2)

  var swerveDriveKinematics = SwerveDriveKinematics(
    frontLeftWheelLocation.translation2d,
    frontRightWheelLocation.translation2d,
    backLeftWheelLocation.translation2d,
    backRightWheelLocation.translation2d
  )

  private var swerveDriveOdometry = SwerveDriveOdometry(
    swerveDriveKinematics,
    Rotation2d(gyroAngle.inRadians),
    Pose(0.meters, 0.meters, 0.degrees).pose2d // TODO: Later: Figure out what the starting position will be
  )

  private var trajDuration = 0.0
  private var trajCurTime = 0.0
  private var trajStartTime = 0.0

  private lateinit var lastModuleSpeeds: Array<SwerveModuleState>

  private var pathFollowController = RamseteController()
  var path: Trajectory = Trajectory(listOf(Trajectory.State()))
    set(value) {
      trajDuration = value.totalTimeSeconds
      trajStartTime = Clock.fpgaTime

      // TODO: When set Trajectory in command run ZeroSensors command
      val initialSample = value.sample(0.0)
      swerveDriveOdometry.resetPosition(initialSample.poseMeters, Rotation2d(-gyroAngle.inRadians))
      pathFollowController = RamseteController(
        Constants.Drivetrain.Gains.RAMSETE_B,
        Constants.Drivetrain.Gains.RAMSETE_ZETA
      )

      Logger.addEvent("Epic Gaming Drivertrains 4099 remeber to like and subscribe!", "Whats up gamers today we Begin path following")

      field = value
    }

  fun isPathFinished(): Boolean {
    return (trajCurTime - trajStartTime) > trajDuration
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

    // Wheel positions (from odometry)?
    Logger.addSource("Drivetrain", "Front Left Wheel Position") { frontLeftWheelLocation }
    Logger.addSource("Drivetrain", "Front Right Wheel Position") { frontRightWheelLocation }
    Logger.addSource("Drivetrain", "Back Left Wheel Position") { backLeftWheelLocation }
    Logger.addSource("Drivetrain", "Back Right Wheel Position") { backRightWheelLocation }

    //  gyro angle
    Logger.addSource("Drivetrain", "Gyro Angle") { gyroAngle }
    //  pathfollow time stamp (need to do pathfollow stuff first?)

    //  if gyro is connected boolean
    Logger.addSource("Drivetrain", "Gyro Connected") {  }

    // TODO: X Pose and Y pose, wheel positions, wheel velocity, wheel target velocities?

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
  fun set(angularVelocity: AngularVelocity, driveVector: Pair<LinearVelocity, LinearVelocity>) {
    val vX = if (isFieldOriented) {
      driveVector.first * gyroAngle.cos -
        driveVector.second * gyroAngle.sin
    } else {
      driveVector.first
    }
    val vY = if (isFieldOriented) {
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

    // odometry
    // TODO: Put in periodic
    var gyro2d = Rotation2d(gyroAngle.inRadians)

    swerveDriveOdometry.update(
      gyro2d,
      SwerveModuleState(wheelSpeeds[0].inMetersPerSecond, Rotation2d(wheelAngles[0].inRadians)),
      SwerveModuleState(wheelSpeeds[1].inMetersPerSecond, Rotation2d(wheelAngles[1].inRadians)),
      SwerveModuleState(wheelSpeeds[2].inMetersPerSecond, Rotation2d(wheelAngles[2].inRadians)),
      SwerveModuleState(wheelSpeeds[3].inMetersPerSecond, Rotation2d(wheelAngles[3].inRadians))
    )
  }

  private fun updatePathFollowing(timestamp: Double, dT: Double) {
    trajCurTime = timestamp - trajStartTime
    val sample = path.sample(trajCurTime)

    val drivetrainSpeeds = pathFollowController.calculate(swerveDriveOdometry.poseMeters, sample)
    // Note: ChassisSpeeds takes x as forward so it is swapped
    set(
      drivetrainSpeeds.omegaRadiansPerSecond.radians.perSecond,
      Pair(drivetrainSpeeds.vyMetersPerSecond.meters.perSecond,drivetrainSpeeds.vxMetersPerSecond.meters.perSecond)
    )
  }

  /**
   * Checks if path following has reached the end of the path.
   *
   * @param timestamp The current time. Value originates from Timer.getFPGATimestamp.
   * @return If path following is finished.
   */
  fun isPathFinished(timestamp: Double): Boolean {
    trajCurTime = timestamp - trajStartTime
    return trajCurTime > trajDuration
  }

  fun hypot(a: LinearVelocity, b: LinearVelocity): LinearVelocity {
    return kotlin.math.hypot(a.inMetersPerSecond, b.inMetersPerSecond).meters.perSecond
  }

  fun atan2(a: LinearVelocity, b: LinearVelocity): Angle {
    return kotlin.math.atan2(a.inMetersPerSecond, b.inMetersPerSecond).radians
  }

  fun zeroSensors(){
    gyro.reset()
    zeroDirection()
    zeroDrive()
  }

  fun zeroDirection(){
    wheels.forEach {
      it.zeroDirection()
    }
  }

  fun zeroDrive(){
    wheels.forEach {
      it.zeroDrive()
    }
  }
}
