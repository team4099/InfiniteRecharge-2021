package com.team4099.robot2021.auto

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.perSecond
import com.team4099.lib.units.step
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint

object PathStore {
  private val centripetalConstraint =
      CentripetalAccelerationConstraint(Constants.Drivetrain.CENTRIPETAL_ACCEL_METERS_PER_SEC_SQ)

  private val config: TrajectoryConfig =
      TrajectoryConfig(
              Constants.Drivetrain.MAX_VEL_METERS_PER_SEC,
              Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ)
          .setKinematics(Drivetrain.swerveDriveKinematics)
          .addConstraint(centripetalConstraint)

  private val slowConfig: TrajectoryConfig =
      TrajectoryConfig(
              Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
              Constants.Drivetrain.SLOW_ACCEL_METERS_PER_SEC_SQ)
          .setKinematics(Drivetrain.swerveDriveKinematics)
          .addConstraint(centripetalConstraint)

  private val reversedConfig: TrajectoryConfig =
      TrajectoryConfig(
              Constants.Drivetrain.MAX_VEL_METERS_PER_SEC,
              Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ)
          .setKinematics(Drivetrain.swerveDriveKinematics)
          .setReversed(true)

  private val slowReversedConfig: TrajectoryConfig =
      TrajectoryConfig(
              Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
              Constants.Drivetrain.SLOW_ACCEL_METERS_PER_SEC_SQ)
          .setKinematics(Drivetrain.swerveDriveKinematics)
          .setReversed(true)

  val driveForward: Trajectory =
      TrajectoryGenerator.generateTrajectory(
          Pose2d(0.0, 0.0, Rotation2d(0.0)),
          listOf(),
          Pose2d(1.0, 0.0, Rotation2d(0.0)),
          config.setStartVelocity(0.0).setEndVelocity(0.0))

  private val navPoints =
      mapOf(
          "A" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(2.5.feet, x) },
          "B" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(5.feet, x) },
          "C" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(7.5.feet, x) },
          "D" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(10.feet, x) },
          "E" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(12.5.feet, x) })

  private val initLinePowerPort = Pose2d(3.627, -2.429, Rotation2d(0.0))
  private val initLineFarTrench = Pose2d(3.627, -6.824, Rotation2d(0.0))
  private val nearTrenchEdge = Pose2d(5.0, -0.869, Rotation2d(0.0))
  private val nearTrenchEnd = Pose2d(7.5, -0.869, Rotation2d(0.0))
  private val farTrench = Pose2d(5.794, -7.243, Rotation2d(-20.0))
  private val rendezvousPoint2Balls = Pose2d(5.878, -2.755, Rotation2d(-20.0))

  private val reintroductionZone =
      Pose(navPoints["C"]!![10] + Translation(15.inches, 0.feet), 0.degrees).pose2d
  private val green = Pose(navPoints["C"]!![2] + Translation(15.inches, 0.feet), 0.degrees).pose2d
  private val yellow = Pose(navPoints["C"]!![5] + Translation(15.inches, 0.feet), 0.degrees).pose2d
  private val red = Pose(navPoints["C"]!![8] + Translation(15.inches, 0.feet), 0.degrees).pose2d
  private val blue = Pose(navPoints["C"]!![6] + Translation(15.inches, 0.feet), 0.degrees).pose2d

  val galacticSearchARed: Trajectory = TrajectoryGenerator.generateTrajectory(
    Pose(navPoints["C"]!![1] + Translation(15.inches, 0.feet), 0.degrees).pose2d,
    listOf(
      navPoints["C"]!![3].translation2d,
      navPoints["D"]!![5].translation2d,
      navPoints["A"]!![6].translation2d
    ),
    Pose(navPoints["C"]!![11] - Translation(15.inches, 0.feet), 0.degrees).pose2d,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )
  /*
  val ARedPath = Path(
    Pose(navPoints["C"]!![1] + Translation(30.inches, 0.feet), 0.degrees),
    Pose(navPoints["C"]!![11] - Translation(30.inches, 0.feet), 0.degrees)
  ).apply {
    addWaypoint(navPoints["C"]!![3].translation2d)
    addWaypoint(navPoints["D"]!![5].translation2d)
    addWaypoint(navPoints["A"]!![6].translation2d)
    build()
  }
  val galacticSearchARed: Trajectory = Trajectory(
    0.0.meters.perSecond,
    ARedPath,
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )*/

  val galacticSearchABlue: Trajectory = TrajectoryGenerator.generateTrajectory(
    Pose(navPoints["C"]!![1] + Translation(15.inches, 0.feet), 0.degrees).pose2d,
    listOf(
      navPoints["E"]!![6].translation2d,
      navPoints["B"]!![7].translation2d,
      navPoints["C"]!![9].translation2d
    ),
    Pose(navPoints["C"]!![11] - Translation(15.inches, 0.feet), 0.degrees).pose2d,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )
  /*
  val ABluePath = Path(
    Pose(navPoints["C"]!![1] + Translation(30.inches, 0.feet), 0.degrees),
    Pose(navPoints["C"]!![11] - Translation(30.inches, 0.feet), 0.degrees)
  ).apply {
    addWaypoint(navPoints["E"]!![6].translation2d)
    addWaypoint(navPoints["B"]!![7].translation2d)
    addWaypoint(navPoints["C"]!![9].translation2d)
    build()
  }
  val galacticSearchABlue: Trajectory = Trajectory(
    0.0.meters.perSecond,
    ABluePath,
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )*/

  val galacticSearchBRed: Trajectory = TrajectoryGenerator.generateTrajectory(
    Pose(navPoints["C"]!![1] + Translation(15.inches, 0.feet), 0.degrees).pose2d,
    listOf(
      navPoints["B"]!![3].translation2d,
      navPoints["D"]!![5].translation2d,
      navPoints["B"]!![7].translation2d
    ),
    Pose(navPoints["C"]!![11] - Translation(15.inches, 0.feet), 0.degrees).pose2d,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )
  /*
  val BRedPath = Path(
    Pose(navPoints["B"]!![1] + Translation(30.inches, 15.inches), 0.degrees),
    Pose(navPoints["B"]!![11] - Translation(30.inches, 0.feet), 0.degrees)
  ).apply {
    addWaypoint(navPoints["B"]!![3].translation2d)
    addWaypoint(navPoints["D"]!![5].translation2d)
    addWaypoint(navPoints["B"]!![7].translation2d)
    build()
  }
  val galacticSearchBRed: Trajectory = Trajectory(
    0.0.meters.perSecond,
    BRedPath,
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )*/

  val galacticSearchBBlue: Trajectory = TrajectoryGenerator.generateTrajectory(
    Pose(navPoints["C"]!![1] + Translation(15.inches, 0.feet), 0.degrees).pose2d,
    listOf(
      navPoints["D"]!![6].translation2d,
      navPoints["B"]!![8].translation2d,
      navPoints["D"]!![10].translation2d
    ),
    Pose(navPoints["C"]!![11] - Translation(15.inches, 0.feet), 0.degrees).pose2d,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )
  /*
  val BBluePath = Path(
    Pose(navPoints["E"]!![1] + Translation(30.inches, 15.inches), 0.degrees),
    Pose(navPoints["D"]!![11] - Translation(30.inches, 0.feet), 0.degrees)
  ).apply {
    addWaypoint(navPoints["D"]!![6].translation2d)
    addWaypoint(navPoints["B"]!![8].translation2d)
    addWaypoint(navPoints["D"]!![10].translation2d)
    build()
  }
  val galacticSearchBBlue: Trajectory = Trajectory(
    0.0.meters.perSecond,
    BBluePath,
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )*/

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

  val toRendezvousPoint2Balls: Trajectory = TrajectoryGenerator.generateTrajectory(
    initLinePowerPort,
    listOf(),
    rendezvousPoint2Balls,
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val fromRendezvousPoint2BallsToPowerPort: Trajectory = TrajectoryGenerator.generateTrajectory(
    rendezvousPoint2Balls,
    listOf(),
    initLinePowerPort,
    reversedConfig.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val fromGreentoReintroduction: Trajectory = TrajectoryGenerator.generateTrajectory(
    green,
    listOf(),
    reintroductionZone,
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val fromYellowtoReintroduction: Trajectory = TrajectoryGenerator.generateTrajectory(
    yellow,
    listOf(),
    reintroductionZone,
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )
  val fromBluetoReintroduction: Trajectory = TrajectoryGenerator.generateTrajectory(
    blue,
    listOf(navPoints["C"]!![6].translation2d),
    reintroductionZone,
    config.setStartVelocity(0.0).setEndVelocity(0.0)
  )

  val fromRedtoReintroduction: Trajectory = TrajectoryGenerator.generateTrajectory(
    red,
    listOf(navPoints["C"]!![8].translation2d),
    reintroductionZone,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )

  val fromIntrotoGreen: Trajectory = TrajectoryGenerator.generateTrajectory(
    reintroductionZone,
    listOf(navPoints["C"]!![10].translation2d),
    green,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )

  val fromIntrotoYellow: Trajectory = TrajectoryGenerator.generateTrajectory(
    reintroductionZone,
    listOf(navPoints["C"]!![10].translation2d),
    yellow,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )

  val fromIntrotoBlue: Trajectory = TrajectoryGenerator.generateTrajectory(
    reintroductionZone,
    listOf(navPoints["C"]!![10].translation2d),
    blue,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )

  val fromIntrotoRed: Trajectory = TrajectoryGenerator.generateTrajectory(
    reintroductionZone,
    listOf(navPoints["C"]!![10].translation2d),
    red,
    config.setStartVelocity(0.0).setEndVelocity(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC)
  )
}
