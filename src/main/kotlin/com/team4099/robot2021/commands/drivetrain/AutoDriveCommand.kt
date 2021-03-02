package com.team4099.robot2021.commands.drivetrain

import com.team4099.lib.hal.Clock
import com.team4099.lib.pathfollow.Trajectory
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.controller.HolonomicDriveController
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds




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

  private val pathFollowController = HolonomicDriveController(xPID, yPID, thetaPID)

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
    val xFF: Double = linearVelocityRefMeters * poseRef.getRotation().getCos()
    val yFF: Double = linearVelocityRefMeters * poseRef.getRotation().getSin()
    val thetaFF: Double = thetaPID.calculate(Drivetrain.pose.theta.inRadians, angleRef.getRadians())

    // Calculate feedback velocities (based on position error).
    val xFeedback: Double = xPID.calculate(Drivetrain.pose.x.inMeters, poseRef.getX())
    val yFeedback: Double = yPID.calculate(Drivetrain.pose.y.inMeters, poseRef.getY())

    Drivetrain.set(
      thetaFF.radians.perSecond,
      Pair(
        (xFF+xFeedback).meters.perSecond,
        (yFF + yFeedback).meters.perSecond
      )
    )
  }

  override fun isFinished(): Boolean {
    trajCurTime = Clock.fpgaTime - trajStartTime
    return trajCurTime > trajDuration
  }
}
