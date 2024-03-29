package com.team4099.robot2021.commands.drivetrain

import com.team4099.lib.hal.Clock
import com.team4099.lib.logging.Logger
import com.team4099.lib.pathfollow.Trajectory
import com.team4099.lib.units.base.inFeet
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.inSeconds
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.derived.cos
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.derived.sin
import com.team4099.lib.units.inRadiansPerSecond
import com.team4099.lib.units.inRadiansPerSecondPerSecond
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.CommandBase
import kotlin.math.PI

class AutoDriveCommand(private val trajectory: Trajectory) : CommandBase() {
  private val xPID =
      PIDController(
          Constants.Drivetrain.PID.AUTO_POS_KP,
          Constants.Drivetrain.PID.AUTO_POS_KI,
          Constants.Drivetrain.PID.AUTO_POS_KD)
  private val yPID =
      PIDController(
          Constants.Drivetrain.PID.AUTO_POS_KP,
          Constants.Drivetrain.PID.AUTO_POS_KI,
          Constants.Drivetrain.PID.AUTO_POS_KD)
  private val thetaPID =
      ProfiledPIDController(
          Constants.Drivetrain.PID.DRIVE_THETA_PID_KP,
          Constants.Drivetrain.PID.DRIVE_THETA_PID_KI,
          Constants.Drivetrain.PID.DRIVE_THETA_PID_KD,
          TrapezoidProfile.Constraints(
              Constants.Drivetrain.MAX_AUTO_ANGULAR_VEL.inRadiansPerSecond,
              Constants.Drivetrain.MAX_AUTO_ANGULAR_ACCEL.inRadiansPerSecondPerSecond))

  private var trajCurTime = 0.0.seconds
  private var trajStartTime = 0.0.seconds

  init {
    addRequirements(Drivetrain)
    thetaPID.enableContinuousInput(-PI, PI)

    Logger.addSource("Drivetrain", "Path Follow Start Timestamp") { trajStartTime.inSeconds }
    Logger.addSource("Drivetrain", "Path Follow Current Timestamp") { trajCurTime.inSeconds }
    Logger.addSource(
        "Drivetrain Tuning",
        "theta kP",
        { Constants.Drivetrain.PID.DRIVE_THETA_PID_KP },
        { newP -> thetaPID.p = newP },
        false)
    Logger.addSource(
        "Drivetrain Tuning",
        "theta kD",
        { Constants.Drivetrain.PID.DRIVE_THETA_PID_KD },
        { newD -> thetaPID.d = newD },
        false)
    Logger.addSource(
        "Drivetrain Tuning",
        "auto position kp",
        { Constants.Drivetrain.PID.AUTO_POS_KP },
        { newP ->
          xPID.p = newP
          yPID.p = newP
        },
        false)
    Logger.addSource(
        "Drivetrain Tuning",
        "auto position kd",
        { Constants.Drivetrain.PID.AUTO_POS_KD },
        { newD ->
          xPID.d = newD
          yPID.d = newD
        },
        false)
    Logger.addSource("Drivetrain Tuning", "desired x") {
      trajectory.sample(trajCurTime).pose.x.inFeet
    }
    Logger.addSource("Drivetrain Tuning", "desired y") {
      trajectory.sample(trajCurTime).pose.y.inFeet
    }

    Logger.addSource("Drivetrain Tuning", "desired theta") {
      trajectory.sample(trajCurTime).pose.theta.inDegrees
    }

    thetaPID.enableContinuousInput(-PI, PI)
  }

  override fun initialize() {
    Drivetrain.pose = trajectory.startingPose
    trajStartTime = Clock.fpgaTime + trajectory.startTime
  }

  override fun execute() {
    trajCurTime = Clock.fpgaTime - trajStartTime
    val desiredState = trajectory.sample(trajCurTime)
    val xFF = desiredState.linearVelocity * desiredState.curvature.cos
    val yFF = desiredState.linearVelocity * desiredState.curvature.sin
    val thetaFF =
        thetaPID.calculate(-Drivetrain.pose.theta.inRadians, desiredState.pose.theta.inRadians)
            .radians
            .perSecond

    // Calculate feedback velocities (based on position error).
    val xFeedback =
        -xPID.calculate(Drivetrain.pose.x.inMeters, desiredState.pose.x.inMeters).meters.perSecond
    val yFeedback =
        -yPID.calculate(Drivetrain.pose.y.inMeters, desiredState.pose.y.inMeters).meters.perSecond

    val xAccel = desiredState.linearAcceleration * desiredState.curvature.cos
    val yAccel = desiredState.linearAcceleration * desiredState.curvature.sin

    Drivetrain.set(
        thetaFF,
        Pair(yFF + yFeedback, xFF + xFeedback),
        true,
        0.radians.perSecond.perSecond,
        Pair(yAccel, xAccel))
  }

  override fun isFinished(): Boolean {
    trajCurTime = Clock.fpgaTime - trajStartTime
    return trajCurTime > trajectory.endTime
  }

  override fun end(interrupted: Boolean) {
    if (interrupted) {
      // Stop where we are if interrupted
      Drivetrain.set(0.degrees.perSecond, Pair(0.meters.perSecond, 0.meters.perSecond))
    } else {
      // Execute one last time to end up in the final state of the trajectory
      // Since we weren't interrupted, we know curTime > endTime
      execute()
    }
  }
}
