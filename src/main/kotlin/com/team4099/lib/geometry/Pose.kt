package com.team4099.lib.geometry

import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.Time
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.radians
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Transform2d
import edu.wpi.first.wpilibj.geometry.Translation2d

data class Pose(val x: Length, val y: Length, val theta: Angle) {
  constructor(translation: Translation, theta: Angle) : this(translation.x, translation.y, theta)

  constructor(pose: Pose2d) : this(
      pose.translation.x.meters, pose.translation.y.meters, pose.rotation.radians.radians)

  val translation = Translation(x, y)

  val pose2d
    get() = Pose2d(translation.x.inMeters, translation.y.inMeters, Rotation2d(theta.inRadians))

  operator fun plus(other: Pose): Pose {
    return Pose(translation + other.translation, theta + other.theta)
  }

  operator fun minus(other: Pose): Pose {
    return Pose(translation - other.translation, theta - other.theta)
  }

  operator fun times(scalar: Double): Pose {
    return Pose(translation * scalar, theta)
  }

  operator fun div(scalar: Double): Pose {
    return Pose(translation / scalar, theta)
  }

  operator fun unaryMinus(): Pose {
    return Pose(-translation, theta + 180.degrees)
  }

  fun update(twist: Twist, timestep: Time): Pose {
    return Pose(pose2d.exp(twist.toTwist2d(timestep)))
  }

  fun change(pose: Pose, timestep: Time): Twist {
    return Twist(pose2d.log(pose.pose2d), timestep)
  }

  fun relativeTo(other: Pose): Pose {
    return Pose(pose2d.relativeTo(other.pose2d))
  }

  fun transformBy(x: Length, y: Length, theta: Angle): Pose {
    return Pose(
        pose2d.transformBy(
            Transform2d(Translation2d(x.inMeters, y.inMeters), Rotation2d(theta.inRadians))))
  }
}
