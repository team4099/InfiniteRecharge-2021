package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.derived.degrees
import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera
import org.photonvision.PhotonTrackedTarget
import kotlin.math.absoluteValue

object BallVision : SubsystemBase() {

  private val ballCamera: PhotonCamera = PhotonCamera("ballcam")
  private val ballCameraResult
    get() = ballCamera.getLatestResult()
  private val targets = ballCameraResult.getTargets()

  var ballPath: Trajectory = PathStore.galacticSearchARed
    get() = choosePath()

  init {
    ballCamera.setPipelineIndex(Constants.Vision.DRIVER_PIPELINE_ID)

    Logger.addSource("BallVision", "Best Ball Path") { ballPath }
  }

  fun choosePath() : Trajectory {
    var centerTarget: PhotonTrackedTarget = targets[0]
    var closeCenterTarget: PhotonTrackedTarget = targets[0]
    var pathA = false

    for (target in targets) {
      if (target.yaw.degrees.absoluteValue < Constants.BallVision.CENTER_YAW_THRESHOLD) {
        pathA = true
        centerTarget = target
      }
    }

    if (pathA) {
      //balls in center means path A
      //check area of target (distance of ball) to see if it is red or blue - test to find values for this
      //could use pitch instead if camera is high enough on the robot
      if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) {
        return PathStore.galacticSearchABlue
      } else {
        return PathStore.galacticSearchARed
      }
    } else {
      //no balls in center means path B
      //assume the robot was placed accordingly - row A-B if red, row D-E if blue
      if (centerTarget.yaw < 0) {
        return PathStore.galacticSearchBBlue
      } else {
        return PathStore.galacticSearchBRed
      }

      //code other options here
      //if the robot starts in front of path B balls
      //should be a little bit faster

    }

  }

}
