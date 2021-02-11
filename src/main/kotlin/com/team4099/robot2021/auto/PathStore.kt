package com.team4099.robot2021.auto

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.pathfollow.Path
import com.team4099.lib.pathfollow.Trajectory
import com.team4099.lib.pathfollow.TrajectoryConfig
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.perSecond
import com.team4099.lib.units.step
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint

object PathStore {
  private val trajConfig = TrajectoryConfig(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC, Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ, Constants.Drivetrain.MAX_VEL_ANGULAR_PER_SEC,Constants.Drivetrain.MAX_ACCEL_ANGULAR_PER_SEC_SQ)

/*  private val centripetalConstraint = CentripetalAccelerationConstraint(
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

 */

  private val navPoints = mapOf(
    "A" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(2.5.feet, x) },
    "B" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(5.feet, x) },
    "C" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(7.5.feet, x) },
    "D" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(10.feet, x) },
    "E" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(12.5.feet, x) }
  )

  private val initLinePowerPort = Pose2d(3.627, -2.429, Rotation2d(0.0))
  private val initLineFarTrench = Pose2d(3.627, -6.824, Rotation2d(0.0))
  private val nearTrenchEdge = Pose2d(5.0, -0.869, Rotation2d(0.0))
  private val nearTrenchEnd = Pose2d(7.5, -0.869, Rotation2d(0.0))
  private val farTrench = Pose2d(5.794, -7.243, Rotation2d(-20.0))
  private val rendezvousPoint2Balls = Pose2d(5.878, -2.755, Rotation2d(-20.0))

  val galacticSearchARed: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(navPoints["C"]!![1] + Translation(30.inches, 0.feet), 0.degrees),
      Pose(navPoints["C"]!![11] - Translation(30.inches, 0.feet), 0.degrees)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val toNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(initLinePowerPort),
      Pose(nearTrenchEdge)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val intakeInNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(nearTrenchEdge),
      Pose(nearTrenchEnd)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val fromNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(nearTrenchEnd),
      Pose(initLinePowerPort)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val toFarTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(initLineFarTrench),
      Pose(farTrench)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val fromFarTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(farTrench),
      Pose(initLinePowerPort)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val toRendezvousPoint2Balls = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(initLinePowerPort),
      Pose(rendezvousPoint2Balls)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )

  val fromRendezvousPoint2Balls = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(rendezvousPoint2Balls),
      Pose(initLinePowerPort)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajConfig
  )
}
