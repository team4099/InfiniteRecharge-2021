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
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.perSecond
import com.team4099.lib.units.step
import com.team4099.robot2021.config.Constants

object PathStore {
  private val trajectoryConfig =
      TrajectoryConfig(
          Constants.Drivetrain.MAX_AUTO_VEL,
          Constants.Drivetrain.MAX_AUTO_ACCEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_VEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_ACCEL)

  private val slowTrajectoryConfig =
      TrajectoryConfig(
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Constants.Drivetrain.MAX_AUTO_ACCEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_VEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_ACCEL)

  private val initLinePowerPort = Pose(3.627.meters, (-2.429).meters, 0.0.radians)
  private val initLineFarTrench = Pose(3.627.meters, (-6.824).meters, 0.0.radians)
  private val nearTrenchEdge = Pose(5.0.meters, (-0.869).meters, 0.0.radians)
  private val nearTrenchEnd = Pose(7.5.meters, (-0.869).meters, 0.0.radians)
  private val farTrench = Pose(5.794.meters, (-7.243).meters, (-20.0).radians)
  private val rendezvousPoint2Balls = Pose(5.878.meters, (-2.755).meters, (-20.0).radians)

  private val navPoints =
      mapOf(
          "A" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(x, 2.5.feet) },
          "B" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(x, 5.feet) },
          "C" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(x, 7.5.feet) },
          "D" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(x, 10.feet) },
          "E" to (0.0.feet..30.0.feet step 2.5.feet).map { x -> Translation(x, 12.5.feet) })

  private val reintroductionZone =
      Pose(navPoints["C"]!![10] + Translation(15.inches, 0.feet), 0.degrees)
  private val green = Pose(navPoints["C"]!![2] + Translation(15.inches, 0.feet), 0.degrees)
  private val yellow = Pose(navPoints["C"]!![5] + Translation(15.inches, 0.feet), 0.degrees)
  private val red = Pose(navPoints["C"]!![8] + Translation(15.inches, 0.feet), 0.degrees)
  private val blue = Pose(navPoints["C"]!![6] + Translation(15.inches, 0.feet), 0.degrees)

  val driveForward =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(0.meters, 0.meters, 0.degrees), Pose(5.feet, 0.meters, 0.degrees)),
          0.0.meters.perSecond,
          trajectoryConfig)

  val driveBackwards =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(5.feet, 0.meters, 0.degrees), Pose(0.meters, 0.meters, 0.degrees)),
          0.0.meters.perSecond,
          trajectoryConfig)

  val driveForwardRotation =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(0.meters, 0.meters, 0.degrees), Pose(1.meters, 0.meters, 90.degrees)),
          0.0.meters.perSecond,
          trajectoryConfig)

  val galacticSearchARed: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          // change the end to A10 if there's no wall to run into
          Path(Pose(navPoints["C"]!![1], 0.degrees), Pose(navPoints["A"]!![10], 270.degrees))
              .apply {
            addWaypoint(navPoints["C"]!![3], 0.degrees)
            addWaypoint(navPoints["D"]!![5], 0.degrees)
            addWaypoint(navPoints["A"]!![6], 270.degrees)
            build()
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val galacticSearchABlue: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          // change the end to C11 if there's no wall to run into
          Path(Pose(navPoints["C"]!![1], 0.degrees), Pose(navPoints["C"]!![10], 45.degrees)).apply {
            addWaypoint(navPoints["E"]!![6], 0.degrees)
            addWaypoint(navPoints["B"]!![7], 270.degrees)
            addWaypoint(navPoints["C"]!![9], 45.degrees)
            build()
          },
          0.0.meters.perSecond,
          slowTrajectoryConfig)

  val galacticSearchBRed: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          // change the end to B11 if there's no wall to run into
          Path(Pose(navPoints["A"]!![1], 0.degrees), Pose(navPoints["B"]!![10], 0.degrees)).apply {
            addWaypoint(navPoints["B"]!![3])
            addWaypoint(navPoints["D"]!![5])
            addWaypoint(navPoints["B"]!![7])
            build()
          },
          0.0.meters.perSecond,
          slowTrajectoryConfig)

  val galacticSearchBBlue: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          // change the end to D11 if there's no wall to run into
          Path(
                  Pose(navPoints["E"]!![1], 0.degrees),
                  Pose(navPoints["D"]!![10] - Translation(5.inches, 0.feet), 0.degrees))
              .apply {
            addWaypoint(navPoints["D"]!![6])
            addWaypoint(navPoints["B"]!![8])
            addWaypoint(navPoints["D"]!![10])
            build()
          },
          0.0.meters.perSecond,
          slowTrajectoryConfig)

  val toNearTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initLinePowerPort, nearTrenchEdge),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val intakeInNearTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(nearTrenchEdge, nearTrenchEnd),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromNearTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(nearTrenchEnd, initLinePowerPort),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val toFarTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initLineFarTrench, farTrench),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromFarTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(farTrench, initLinePowerPort),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val toRendezvousPoint2Balls =
      Trajectory(
          0.0.meters.perSecond,
          Path(initLinePowerPort, rendezvousPoint2Balls),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromRendezvousPoint2Balls =
      Trajectory(
          0.0.meters.perSecond,
          Path(rendezvousPoint2Balls, initLinePowerPort),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromGreenToReintroduction =
      Trajectory(
          0.0.meters.perSecond,
          Path(green, reintroductionZone),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromYellowToReintroduction =
      Trajectory(
          0.0.meters.perSecond,
          Path(yellow, reintroductionZone),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromBlueToReintroduction =
      Trajectory(
          0.0.meters.perSecond,
          Path(blue, reintroductionZone),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromRedToReintroduction =
      Trajectory(
          0.0.meters.perSecond,
          Path(red, reintroductionZone),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromIntroToGreen =
      Trajectory(
          0.0.meters.perSecond,
          Path(reintroductionZone, green),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromIntroToYellow =
      Trajectory(
          0.0.meters.perSecond,
          Path(reintroductionZone, yellow),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromIntroToBlue =
      Trajectory(
          0.0.meters.perSecond,
          Path(reintroductionZone, blue),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromIntroToRed =
      Trajectory(
          0.0.meters.perSecond,
          Path(reintroductionZone, red),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val autonavBounce1: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(navPoints["C"]!![1], 0.degrees), Pose(navPoints["A"]!![3], 0.degrees)).apply {
            addWaypoint(navPoints["C"]!![2], 0.degrees)
            build()
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val autonavBounce2: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(navPoints["A"]!![3], 0.degrees), Pose(navPoints["A"]!![6], 0.degrees)).apply {
            addWaypoint(navPoints["D"]!![4])
            addWaypoint(navPoints["E"]!![5])
            addWaypoint(navPoints["D"]!![6])
            build()
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val autonavBounce3: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(navPoints["A"]!![6], 0.degrees), Pose(navPoints["A"]!![9], 0.degrees)).apply {
            addWaypoint(navPoints["D"]!![6])
            addWaypoint(navPoints["E"]!![7])
            addWaypoint(navPoints["E"]!![8])
            addWaypoint(navPoints["D"]!![9])
            build()
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val autonavBounce4: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(navPoints["A"]!![9], 0.degrees), Pose(navPoints["C"]!![11], 0.degrees)).apply {
            addWaypoint(navPoints["C"]!![10])
            build()
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val slalomPath: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(
                  Pose(navPoints["E"]!![1] /*- Translation(15.inches, 0.feet)*/, 0.degrees),
                  Pose(navPoints["C"]!![1] - Translation(0.inches, 10.inches), 0.degrees))
              .apply {
            addWaypoint(navPoints["E"]!![3] /*- Translation(30.inches, 0.feet)*/)
            addWaypoint(navPoints["D"]!![3])
            // was C4
            addWaypoint(navPoints["C"]!![3])
            // addWaypoint(navPoints["B"]!![6] - Translation(0.feet, 15.inches))
            // addWaypoint(navPoints["C"]!![8])
            addWaypoint(navPoints["C"]!![9])
            addWaypoint(navPoints["E"]!![9] + Translation(0.inches, 15.inches))
            // top left of right circle
            // addWaypoint(navPoints["D"]!![9] - Translation(30.inches,30.inches))
            // addWaypoint(navPoints["E"]!![10])
            addWaypoint(navPoints["E"]!![11] + Translation(15.inches, 0.inches))
            // addWaypoint(navPoints["D"]!![11])
            addWaypoint(navPoints["C"]!![11])
            // addWaypoint(navPoints["C"]!![10])
            addWaypoint(navPoints["C"]!![9])
            // addWaypoint(navPoints["D"]!![9])
            // rounding corner here - E8
            addWaypoint(navPoints["E"]!![9])

            // addWaypoint(navPoints["E"]!![6])
            addWaypoint(navPoints["E"]!![3])
            addWaypoint(navPoints["C"]!![3] - Translation(0.inches, 10.inches))
            // addWaypoint(navPoints["C"]!![3] - Translation(30.inches, 0.feet))
            build()
          },
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val barrelPath: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(Pose(navPoints["C"]!![1], 0.degrees), Pose(navPoints["B"]!![2], 0.degrees)).apply {
            addWaypoint(navPoints["C"]!![5])
            addWaypoint(navPoints["D"]!![6])
            addWaypoint(navPoints["E"]!![5])
            addWaypoint(navPoints["D"]!![4])
            addWaypoint(navPoints["C"]!![5])
            addWaypoint(navPoints["C"]!![7])
            addWaypoint(navPoints["B"]!![9])
            addWaypoint(navPoints["A"]!![8] - Translation(0.inches, 30.inches))
            addWaypoint(navPoints["B"]!![7])
            addWaypoint(navPoints["D"]!![7])
            addWaypoint(navPoints["E"]!![10])
            addWaypoint(navPoints["D"]!![11])
            addWaypoint(navPoints["C"]!![10])
            addWaypoint(navPoints["C"]!![7])
            build()
          },
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)
}
