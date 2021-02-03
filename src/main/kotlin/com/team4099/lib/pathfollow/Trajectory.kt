package com.team4099.lib.pathfollow

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.units.LinearVelocity

class Trajectory(
  private val startPose: Pose,
  private val startVelocity: LinearVelocity,
  guidePoints: List<Translation> = listOf(),
  private val endPose: Pose,
  private val endVelocity: LinearVelocity
) {
  init {
    
  }
}
