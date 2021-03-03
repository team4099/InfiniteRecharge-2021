package com.team4099.robot2021.commands.drivetrain

import com.team4099.lib.hal.Clock
import com.team4099.lib.logging.Logger
import com.team4099.lib.pathfollow.Trajectory
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.inSeconds
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.derived.cos
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.derived.sin
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.CommandBase

class AutoDriveCommand(private val trajectory: Trajectory) : CommandBase() {
  private val xPID =
      PIDController(
          Constants.Drivetrain.PID.DRIVE_X_PID_KP,
          Constants.Drivetrain.PID.DRIVE_X_PID_KI,
          Constants.Drivetrain.PID.DRIVE_X_PID_KD)
  private val yPID =
      PIDController(
          Constants.Drivetrain.PID.DRIVE_Y_PID_KP,
          Constants.Drivetrain.PID.DRIVE_Y_PID_KI,
          Constants.Drivetrain.PID.DRIVE_Y_PID_KD)
  private val thetaPID =
      ProfiledPIDController(
          Constants.Drivetrain.PID.DRIVE_THETA_PID_KP,
          Constants.Drivetrain.PID.DRIVE_THETA_PID_KI,
          Constants.Drivetrain.PID.DRIVE_THETA_PID_KD,
          TrapezoidProfile.Constraints(
              Constants.Drivetrain.PID.DRIVE_THETA_PID_MAX_VEL.value,
              Constants.Drivetrain.PID.DRIVE_THETA_PID_MAX_ACCEL.value))

  private var trajCurTime = 0.0.seconds
  private var trajStartTime = 0.0.seconds

  init {
    addRequirements(Drivetrain)

    Logger.addSource("Drivetrain", "Path Follow Start Timestamp") { trajStartTime.inSeconds }
    Logger.addSource("Drivetrain", "Path Follow Current Timestamp") { trajCurTime.inSeconds }
  }

  override fun initialize() {
    trajStartTime = Clock.fpgaTime + trajectory.startTime
  }

  override fun execute() {
    trajCurTime = Clock.fpgaTime - trajStartTime
    val desiredState = trajectory.sample(trajCurTime)
    val xFF = desiredState.linearVelocity * desiredState.pose.theta.cos
    val yFF = desiredState.linearVelocity * desiredState.pose.theta.sin
    val thetaFF =
        thetaPID.calculate(Drivetrain.pose.theta.inRadians, desiredState.pose.theta.inRadians)
            .radians
            .perSecond

    // Calculate feedback velocities (based on position error).
    val xFeedback =
        xPID.calculate(Drivetrain.pose.x.inMeters, desiredState.pose.x.inMeters).meters.perSecond
    val yFeedback =
        yPID.calculate(Drivetrain.pose.y.inMeters, desiredState.pose.x.inMeters).meters.perSecond

    val xAccel = desiredState.linearAcceleration * desiredState.pose.theta.cos
    val yAccel = desiredState.linearAcceleration * desiredState.pose.theta.sin

    Drivetrain.set(
        thetaFF,
        Pair(xFF + xFeedback, yFF + yFeedback),
        true,
        0.radians.perSecond.perSecond,
        Pair(xAccel, yAccel))
  }

  override fun isFinished(): Boolean {
    trajCurTime = Clock.fpgaTime - trajStartTime
    return trajCurTime > trajectory.endTime
  }
}
