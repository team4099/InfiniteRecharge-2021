package com.team4099.lib.pathfollow

import com.team4099.lib.geometry.Pose
import com.team4099.lib.units.LinearAcceleration
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.base.Time

data class TrajectoryState(
  val timestamp: Time,
  var pose: Pose,
  val linearVelocity: LinearVelocity,
  val linearAcceleration: LinearAcceleration
)
