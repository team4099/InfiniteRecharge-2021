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

object PathStore {
  private val trajectoryConstant = TrajectoryConfig(Constants.Drivetrain.MAX_VEL_METERS_PER_SEC, Constants.Drivetrain.MAX_ACCEL_METERS_PER_SEC_SQ, Constants.Drivetrain.MAX_VEL_ANGULAR_PER_SEC,Constants.Drivetrain.MAX_ACCEL_ANGULAR_PER_SEC_SQ)

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
    trajectoryConstant
  )

  val fromGreentoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      green,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )

  val fromYellowtoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      yellow,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )
  val fromBluetoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      blue,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )

  val fromRedtoReintroduction = Trajectory(
    0.0.meters.perSecond,
    Path(
      red,
      reintroductionZone
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )

  val fromIntrotoGreen = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      green
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )

  val fromIntrotoYellow = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      yellow
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )

  val fromIntrotoBlue = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      blue
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )

  val fromIntrotoRed = Trajectory(
    0.0.meters.perSecond,
    Path(
      reintroductionZone,
      red
    ),
    Constants.Drivetrain.SLOW_VEL_METERS_PER_SEC,
    trajectoryConstant
  )
}
