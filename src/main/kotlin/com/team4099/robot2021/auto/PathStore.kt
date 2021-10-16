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
  /*private val trajectoryConfig =
  TrajectoryConfig(
      Constants.Drivetrain.MAX_AUTO_VEL,
      Constants.Drivetrain.MAX_AUTO_ACCEL,
      Constants.Drivetrain.MAX_AUTO_ANGULAR_VEL,
      Constants.Drivetrain.MAX_AUTO_ANGULAR_ACCEL)*/

  // TODO: rename this to slowTrajectoryConfig and uncomment normal one when done testing
  private val trajectoryConfig =
      TrajectoryConfig(
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Constants.Drivetrain.MAX_AUTO_ACCEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_VEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_ACCEL)

  private val slowTrajectoryConfig =
      TrajectoryConfig(
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Constants.Drivetrain.MAX_AUTO_ACCEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_VEL,
          Constants.Drivetrain.MAX_AUTO_ANGULAR_ACCEL)

  // NOTE: Pathweaver y-axis is opposite than ours!
  // 2020 Season
  private val initLineRendezvous = Pose(3.627.meters, 2.429.meters, 0.0.radians)
  private val initLinePowerPort = Pose(3.627.meters, 2.429.meters, 0.0.radians)
  private val initLineFarTrench = Pose(3.627.meters, 6.824.meters, 0.0.radians)
  private val nearTrenchEdge = Pose(5.0.meters, 0.869.meters, 0.0.radians)
  // private val nearTrenchEdge = Translation(5.0.meters, 0.869.meters)
  private val nearTrenchEnd = Pose(7.5.meters, 0.869.meters, 0.0.radians)
  private val farTrench = Pose(5.794.meters, 7.243.meters, 0.0.radians) // what is this
  private val rendezvousPoint2Balls = Pose(5.878.meters, 2.755.meters, 0.0.radians)

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

  val toNearTrench: Trajectory =
      Trajectory(
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Path(initLinePowerPort, nearTrenchEdge),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val intakeInNearTrench: Trajectory =
      Trajectory(
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Path(nearTrenchEdge, nearTrenchEnd),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromNearTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          // Path(nearTrenchEnd, initLinePowerPort),
          Path(nearTrenchEnd, initLineFarTrench),
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
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Path(farTrench, initLineFarTrench),
          0.0.meters.perSecond,
          trajectoryConfig)

  val toRendezvousPoint2Balls =
      Trajectory(
          0.0.meters.perSecond,
          Path(initLineRendezvous, rendezvousPoint2Balls),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromRendezvousPoint2Balls =
      Trajectory(
          Constants.Drivetrain.SLOW_AUTO_VEL,
          Path(rendezvousPoint2Balls, initLinePowerPort),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  // 2021 Season (Shooter is front)
  private val initShootPowerPort =
      Pose(
          Constants.RobotPositions.START_X,
          Constants.RobotPositions.START_Y,
          Constants.RobotPositions.START_ANGLE)
  private val crossBarShoot =
      Pose(
          Constants.RobotPositions.CROSS_BAR_X,
          Constants.RobotPositions.CROSS_BAR_Y,
          Constants.RobotPositions.CROSS_BAR_ANGLE)
  private val avoidBarShoot =
      Pose(
          Constants.RobotPositions.AVOID_BAR_X,
          Constants.RobotPositions.AVOID_BAR_Y,
          Constants.RobotPositions.AVOID_BAR_ANGLE)
  // private val initEnemyTrench = Pose(131.5.inches, 297.5.inches, 180.degrees)
  private val initEnemyTrench = Pose(0.inches, 0.inches, 0.degrees)

  // private val enemyTrench = Pose(230.537.inches, 297.5.inches, 180.degrees)
  // private val enemyTrench = Pose(99.037.inches, 0.inches, 0.degrees)
  // dist between initiation line & trench balls minus 20 inches clearance
  // 110.35
  private val enemyTrench = Pose((130.35 - 20).inches, 0.inches, 0.degrees)

  // on apex of triangle
  // private val bestShotPose = Pose(30.250.inches, 94.655.inches, 180.degrees)
  // estimated against wall

  // private val bestShotPose = Pose(20.inches, 94.655.inches, 180.degrees)
  // private val bestShotPose = Pose((-111.5).inches, (-202.845).inches, 0.degrees)
  // x: dist between initiation line & trench balls minus 20 inches clearance plus dist between
  // initiation line & triangle apex
  // -202.29
  // y: dist between triangle apex and left ball at trench plus half dist between balls
  // -200.68
  private val bestShotPose =
      Pose((-(130.35 - 20 + 91.94)).inches, (-(191.43 + 9.25)).inches, 0.degrees)

  val crossBar: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initShootPowerPort, crossBarShoot).apply {
            addWaypoint(Translation(6.9.meters, 2.9.meters), 202.5.degrees)
            addWaypoint(Translation(6.0.meters, 3.6.meters))
            addWaypoint(Translation(6.972.meters, 4.0.meters))
            build()
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val avoidBarTightAngle: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initShootPowerPort, avoidBarShoot).apply {
            addWaypoint(Translation(6.611.meters, 3.237.meters), 202.5.degrees)
            addWaypoint(Translation(6.029.meters, 3.423.meters))
            addWaypoint(Translation(6.401.meters, 4.273.meters), 22.5.degrees)
            addWaypoint(Translation(6.797.meters, 3.83.meters))
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val avoidBarCircular: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initShootPowerPort, avoidBarShoot).apply {
            addWaypoint(
                Translation(294.524.inches, 113.2.inches),
                202.5.degrees) // 318.260579.inches, 101.8912455.inches
            addWaypoint(Translation(252.8608316.inches, 141.3023018.inches))
            addWaypoint(Translation(254.4389994.inches, 154.5564093.inches), 292.5.degrees)
            addWaypoint(Translation(264.394.inches, 160.916.inches), 22.5.degrees)
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val toEnemyTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initEnemyTrench, enemyTrench),
          Constants.Drivetrain.SLOW_AUTO_VEL,
          trajectoryConfig)

  val fromEnemyTrench: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(enemyTrench, bestShotPose).apply {
            addWaypoint(Translation(bestShotPose.x, enemyTrench.y), 0.degrees)
          },
          0.0.meters.perSecond,
          trajectoryConfig)

  val toPowerPort: Trajectory =
      Trajectory(
          0.0.meters.perSecond,
          Path(initShootPowerPort, bestShotPose),
          0.0.meters.perSecond,
          trajectoryConfig)


  // Infinite Recharge @ Home

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
          slowTrajectoryConfig)

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
