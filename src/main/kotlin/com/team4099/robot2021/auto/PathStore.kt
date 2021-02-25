package com.team4099.robot2021.auto

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.perSecond
import com.team4099.lib.units.step
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig

object PathStore {
  private val trajectoryConfig = TrajectoryConfig(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC, Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ)

  private val initLinePowerPort = Pose(3.627.meters, (-2.429).meters, 0.0.radians)
  private val initLineFarTrench = Pose(3.627.meters, (-6.824).meters, 0.0.radians)
  private val nearTrenchEdge = Pose(5.0.meters, (-0.869).meters, 0.0.radians)
  private val nearTrenchEnd = Pose(7.5.meters, (-0.869).meters, 0.0.radians)
  private val farTrench = Pose(5.794.meters, (-7.243).meters, (-20.0).radians)
  private val rendezvousPoint2Balls = Pose(5.878.meters, (-2.755).meters, (-20.0).radians)

  val toNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      initLinePowerPort,
      nearTrenchEdge
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val intakeInNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      nearTrenchEdge,
      nearTrenchEnd
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      nearTrenchEnd,
      initLinePowerPort
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val toFarTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      initLineFarTrench,
      farTrench
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromFarTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      farTrench,
      initLinePowerPort
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val toRendezvousPoint2Balls = Trajectory(
    0.0.meters.perSecond,
    Path(
      initLinePowerPort,
      rendezvousPoint2Balls
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromRendezvousPoint2Balls = Trajectory(
    0.0.meters.perSecond,
    Path(
      rendezvousPoint2Balls,
      initLinePowerPort
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  private val navPoints = mapOf(
    "A" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(2.5.feet, x) },
    "B" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(5.feet, x) },
    "C" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(7.5.feet, x) },
    "D" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(10.feet, x) },
    "E" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(12.5.feet, x) }
  )

  private val reintroductionZone = Pose(navPoints["C"]!![10] + Translation(15.inches, 0.feet), 0.degrees)
  private val green = Pose(navPoints["C"]!![2] + Translation(15.inches, 0.feet), 0.degrees)
  private val yellow = Pose(navPoints["C"]!![5] + Translation(15.inches, 0.feet), 0.degrees)
  private val red = Pose(navPoints["C"]!![8] + Translation(15.inches, 0.feet), 0.degrees)
  private val blue = Pose(navPoints["C"]!![6] + Translation(15.inches, 0.feet), 0.degrees)

  val galacticSearchARed: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      Pose(navPoints["C"]!![1] + Translation(30.inches, 0.feet), 0.degrees),
      Pose(navPoints["C"]!![11] - Translation(30.inches, 0.feet), 0.degrees)
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val toNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      initLinePowerPort,
      nearTrenchEdge
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val intakeInNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      nearTrenchEdge,
      nearTrenchEnd
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromNearTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      nearTrenchEnd,
      initLinePowerPort
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val toFarTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      initLineFarTrench,
      farTrench
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromFarTrench: Trajectory = Trajectory(
    0.0.meters.perSecond,
    Path(
      farTrench,
      initLinePowerPort
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val toRendezvousPoint2Balls = Trajectory(
    0.0.meters.perSecond,
    Path(
      initLinePowerPort,
      rendezvousPoint2Balls
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromRendezvousPoint2Balls = Trajectory(
    0.0.meters.perSecond,
    Path(
      rendezvousPoint2Balls,
      initLinePowerPort
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromGreentoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      green,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromYellowtoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      yellow,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )
  val fromBluetoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      blue,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromRedtoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      red,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromIntrotoGreen = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      green
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromIntrotoYellow = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      yellow
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromIntrotoBlue = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      blue
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )

  val fromIntrotoRed = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      red
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConfig
  )
}
