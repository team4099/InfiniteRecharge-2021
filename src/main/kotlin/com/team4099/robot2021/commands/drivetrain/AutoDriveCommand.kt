package com.team4099.robot2021.commands.drivetrain

import com.analog.adis16470.frc.ADIS16470_IMU
import com.team4099.lib.hal.Clock
import com.team4099.lib.pathfollow.Path
import com.team4099.lib.pathfollow.Trajectory
import com.team4099.lib.pathfollow.TrajectoryState
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.degrees
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.controller.HolonomicDriveController
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import kotlin.math.IEEErem


class AutoDriveCommand(private val path: Trajectory) : CommandBase() {
  private val xPID = PIDController(
    Constants.Drivetrain.PID.DRIVE_X_PID_KP,
    Constants.Drivetrain.PID.DRIVE_X_PID_KI,
    Constants.Drivetrain.PID.DRIVE_X_PID_KD
  )
  private val yPID = PIDController(
    Constants.Drivetrain.PID.DRIVE_Y_PID_KP,
    Constants.Drivetrain.PID.DRIVE_Y_PID_KI,
    Constants.Drivetrain.PID.DRIVE_Y_PID_KD
  )
  private val thetaPID = ProfiledPIDController(
    Constants.Drivetrain.PID.DRIVE_THETA_PID_KP,
    Constants.Drivetrain.PID.DRIVE_THETA_PID_KI,
    Constants.Drivetrain.PID.DRIVE_THETA_PID_KD,
    TrapezoidProfile.Constraints(
      Constants.Drivetrain.PID.DRIVE_THETA_PID_MAX_VEL.value,
      Constants.Drivetrain.PID.DRIVE_THETA_PID_MAX_ACCEL.value
    )
  )

  private var trajDuration = 0.0.seconds
  private var trajCurTime = 0.0.seconds
  private var trajStartTime = 0.0.seconds

  private val pose : Path ?= null
  private val kinematics: SwerveDriveKinematics = Drivetrain.swerveDriveKinematics
  private val pathFollowController = HolonomicDriveController(xPID, yPID, thetaPID)
  private val swerveState = SwerveModuleState()
  private val gyro = ADIS16470_IMU()
  private val gyroAngle: Angle
    get() {
      var rawAngle = gyro.angle
      rawAngle += Constants.Drivetrain.GYRO_RATE_COEFFICIENT * gyro.rate
      return rawAngle.IEEErem(360.0).degrees
    }

  init {
    addRequirements(Drivetrain)
  }

  override fun initialize() {
    trajStartTime = Clock.fpgaTime
  }

  override fun execute() {
    trajCurTime = Clock.fpgaTime - trajStartTime
    var desiredState = path
    Drivetrain.updatePathFollowing(Clock.fpgaTime)
  }

  override fun isFinished(): Boolean {
    return Drivetrain.isPathFinished(Clock.fpgaTime)
  }
}
