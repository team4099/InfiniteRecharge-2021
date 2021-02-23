package com.team4099.lib.pathfollow

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.base.Time
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.inMetersPerSecond
import com.team4099.lib.units.inMetersPerSecondPerSecond
import com.team4099.lib.units.perSecond
import edu.wpi.first.wpilibj.trajectory.TrajectoryParameterizer
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile

/**
 * A wrapper around the WPILib trajectory class that handles smooth
 * heading changes for holonomic drivetrains.
 */
class Trajectory(
  private val startVelocity: LinearVelocity,
  private val path: Path,
  private val endVelocity: LinearVelocity,
  private val trajectoryConfig: TrajectoryConfig
) {
  init {
    val states = TrajectoryParameterizer.timeParameterizeTrajectory(
      path.splinePoints,
      trajectoryConfig.constraints,
      startVelocity.inMetersPerSecond,
      endVelocity.inMetersPerSecond,
      trajectoryConfig.maxLinearVelocity.inMetersPerSecond,
      trajectoryConfig.maxLinearAcceleration.inMetersPerSecondPerSecond,
      false
    ).states

    states.mapIndexed { index, state ->
      var headingTarget = if (index == 0) {
        path.startingPose.theta
      } else if (index == states.size - 1) {
        path.endingPose.theta
      } else {
        val tailMap = path.headingPointMap.tailMap(index)
        if (tailMap.size == 0) {
          path.endingPose.theta
        } else {
          path.headingPointMap[tailMap.firstKey()]
        }
      }

      if (headingTarget == null) {
        headingTarget = path.endingPose.theta
      }

      TrajectoryState(
        state.timeSeconds.seconds,
        Pose(Translation(state.poseMeters.translation), headingTarget),
        state.velocityMetersPerSecond.meters.perSecond,
        state.accelerationMetersPerSecondSq.meters.perSecond.perSecond
      )
    }
  }
}
