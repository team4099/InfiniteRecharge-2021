package com.team4099.lib.pathfollow

import com.team4099.lib.geometry.Pose
import com.team4099.lib.geometry.Translation
import com.team4099.lib.logging.Logger
import com.team4099.lib.logging.Logger.Severity.ERROR
import com.team4099.lib.units.derived.Angle
import edu.wpi.first.wpilibj.spline.PoseWithCurvature
import edu.wpi.first.wpilibj.spline.SplineHelper
import edu.wpi.first.wpilibj.spline.SplineParameterizer

/**
 * A path on the XY plane constructed with cubic splines.
 *
 * Heading of holonomic drivetrains can be controlled at any waypoint.
 */
class Path constructor(val startingPose: Pose, val endingPose: Pose) {
  val headingPointMap = sortedMapOf<Int, Angle>()
  var splinePoints = mutableListOf<PoseWithCurvature>()
  private val headingSplineMap = mutableMapOf<Int, Angle>()
  private val waypoints = mutableListOf<Translation>()
  private var built = false

  /**
   * Add a waypoint to the middle of this path.
   *
   * The robot will pass through [nextTranslation] as it travels along the path. If [heading] is set
   * the robot will have that heading at this location, otherwise heading will be interpolated
   * between the previous waypoint with heading specified and the next waypoint with heading
   * specified, including the start and end poses.
   * @param nextTranslation The location of the waypoint.
   * @param heading The target heading at this waypoint, null if the heading at this waypoint does
   * not matter.
   */
  fun addWaypoint(nextTranslation: Translation, heading: Angle? = null) {
    if (built) {
      Logger.addEvent("Path", "Failed to add translation to built path", ERROR)
      return
    }

    // If a heading isn't specified, use either the previous waypoint's heading or
    // the heading of the starting pose
    headingSplineMap[waypoints.size] = heading
      ?: (headingSplineMap[waypoints.size - 1] ?: startingPose.theta)
    waypoints.add(nextTranslation)
  }

  /** Build the path after all desired waypoints have been added. */
  fun build() {
    // Create control vectors from the start and end waypoint
    val waypointTranslation2ds = waypoints.map { it.translation2d }.toTypedArray()
    val controlVectors =
        SplineHelper.getCubicControlVectorsFromWaypoints(
            startingPose.pose2d, waypointTranslation2ds, endingPose.pose2d)

    // Create a list of splines
    val splines =
        listOf(
            *SplineHelper.getCubicSplinesFromControlVectors(
                controlVectors.first(), waypointTranslation2ds, controlVectors.last()))

    // Create the vector of spline points.
    splinePoints = mutableListOf()

    // Add the first point to the vector.
    splinePoints.add(splines[0].getPoint(0.0))

    // Iterate through the vector and parameterize each spline, adding the
    // parameterized points to the final vector.
    splines.forEachIndexed { index, spline ->
      val points = SplineParameterizer.parameterize(spline)

      // Append the array of poses to the vector. We are removing the first
      // point because it's a duplicate of the last point from the previous
      // spline.
      splinePoints.addAll(points.subList(1, points.size))

      // Map spline index for heading to point index
      val splineHeading = headingSplineMap[index]
      headingPointMap[splinePoints.size - 1] = splineHeading
    }
    built = true
  }
}
