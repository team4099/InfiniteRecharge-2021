package com.team4099.lib.pathfollow

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.inMetersPerSecond
import com.team4099.lib.units.inMetersPerSecondPerSecond
import edu.wpi.first.wpilibj.spline.PoseWithCurvature
import edu.wpi.first.wpilibj.spline.Spline
import edu.wpi.first.wpilibj.spline.SplineParameterizer
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import edu.wpi.first.wpilibj.trajectory.TrajectoryParameterizer
import java.util.ArrayList

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

    val headingTargets =
  }
}
