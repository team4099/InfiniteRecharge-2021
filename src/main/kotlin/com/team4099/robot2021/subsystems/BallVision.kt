package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.derived.degrees
import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera
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
    //could implement with these variables instead
    /*var pathA = false
    var pathB = false
    var redPath = false
    var bluePath = false*/

    //this is basically pseudocode so we can make if statements cleaner later
    //also figure out something better than a for loop
    for (target in targets) {
      if (target.yaw.degrees.absoluteValue < Constants.BallVision.CENTER_YAW_THRESHOLD) {
        //balls in center means path A
        //potentially save which target this is elsewhere to refer to outside loop
        //check area of target to see if it is red or blue (test to find values for this)
        //could use pitch instead of camera is high enough on the robot
        if (target.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) {
          return PathStore.galacticSearchABlue
        } else {
          return PathStore.galacticSearchARed
        }
      } else {
        //no balls in center means path B
        //assume the robot was placed accordingly - row A if red, row E if blue
        if (target.yaw < 0) {
          return PathStore.galacticSearchBBlue
        } else {
          return PathStore.galacticSearchBRed
        }
      }
    }

    return PathStore.galacticSearchARed
  }
}
