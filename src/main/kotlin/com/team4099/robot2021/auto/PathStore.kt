package com.team4099.robot2021.auto

import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint

object PathStore {
  private val centripetalConstraint = CentripetalAccelerationConstraint(
    Constants.Drivetrain.CENTRIPETAL_ACCEL_METERS_PER_SEC_SQ
  )

  private val config: TrajectoryConfig = TrajectoryConfig(
    Constants.Drivetrain.MAX_VEL_METERS_PER_SEC,
    Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ
  )
    .setKinematics(Drivetrain.swerveDriveKinematics)
    .addConstraint(centripetalConstraint)

  private val slowConfig: TrajectoryConfig = TrajectoryConfig(
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    Constants.Drivetrain.SLOW_ACCEL_METERS_PER_SEC_SQ
  )
    .setKinematics(Drivetrain.swerveDriveKinematics)
    .addConstraint(centripetalConstraint)

  private val reversedConfig: TrajectoryConfig = TrajectoryConfig(
    Constants.Drivetrain.MAX_VEL_METERS_PER_SEC,
    Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ
  )
    .setKinematics(Drivetrain.swerveDriveKinematics)
    .setReversed(true)

  private val slowReversedConfig: TrajectoryConfig = TrajectoryConfig(
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    Constants.Drivetrain.SLOW_ACCEL_METERS_PER_SEC_SQ
  )
    .setKinematics(Drivetrain.swerveDriveKinematics)
    .setReversed(true)

  val driveForward: Trajectory = TrajectoryGenerator.generateTrajectory(
    Pose2d(0.0, 0.0, Rotation2d(0.0)),
    listOf(),
    Pose2d(1.0, 0.0, Rotation2d(0.0)),
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  private val initLinePowerPort = Pose2d(3.627, -2.429, Rotation2d(0.0))
  private val initLineFarTrench = Pose2d(3.627, -6.824, Rotation2d(0.0))
  private val nearTrenchEdge = Pose2d(5.0, -0.869, Rotation2d(0.0))
  private val nearTrenchEnd = Pose2d(7.5, -0.869, Rotation2d(0.0))
  private val farTrench = Pose2d(5.794, -7.243, Rotation2d(-20.0))
  private val rendezvousPoint2Balls = Pose2d(5.878, -2.755, Rotation2d(-20.0))

  val toNearTrench: Trajectory = TrajectoryGenerator.generateTrajectory(
    initLinePowerPort,
    listOf(),
    nearTrenchEdge,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC)
  )

  val intakeInNearTrench: Trajectory = TrajectoryGenerator.generateTrajectory(
    nearTrenchEdge,
    listOf(),
    nearTrenchEnd,
    slowConfig.setStartVelocity(Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC).setEndVelocity(0.0)
  )

  val fromNearTrench: Trajectory = TrajectoryGenerator.generateTrajectory(
    nearTrenchEnd,
    listOf(),
    initLinePowerPort,
    reversedConfig.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val toFarTrench: Trajectory = TrajectoryGenerator.generateTrajectory(
    initLineFarTrench,
    listOf(),
    farTrench,
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val fromFarTrench: Trajectory = TrajectoryGenerator.generateTrajectory(
    farTrench,
    listOf(),
    initLinePowerPort,
    reversedConfig.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val toRendezvousPoint2Balls = TrajectoryGenerator.generateTrajectory(
    initLinePowerPort,
    listOf(),
    rendezvousPoint2Balls,
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val fromRendezvousPoint2Balls = TrajectoryGenerator.generateTrajectory(
    rendezvousPoint2Balls,
    listOf(),
    initLinePowerPort,
    reversedConfig.setStartVelocity(0.0).setEndVelocity(0.0)
  )
}