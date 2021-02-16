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

  private fun choosePath() : Trajectory {
    var centerTarget: PhotonTrackedTarget = targets[0]
    var offCenterTarget: PhotonTrackedTarget = targets[0]
    var pathA = false
    var ballsOnLeft = false
    var ballsOnRight = true
    var ballsOffCenter = 0

    for (target in targets) {
      if (target.yaw.degrees.absoluteValue < Constants.BallVision.CENTER_YAW_THRESHOLD) {
        //comment path if using option 2
        pathA = true
        centerTarget = target
      }
      else{
        //lines for option 3
        offCenterTarget = target
        ballsOffCenter++
        //option 2 + 3?
        if (target.yaw.degrees.absoluteValue < 0.degrees){
          ballsOnLeft = true
        }
        else {
          ballsOnRight = true
        }
      }
    }

    //Option 1 & 2
    /*if (pathA) {
      //balls in center means path A
      //check area of target (distance of ball) to see if it is red or blue - test to find values for this
      //could use pitch instead if camera is high enough on the robot
      if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) {
        return PathStore.galacticSearchABlue
      } else {
        return PathStore.galacticSearchARed
      }
    } else { //ELSE: Option 1 Only
      //no balls in center means path B
      //assume the robot was placed accordingly - above row B if red, below row D if blue
      if (centerTarget.yaw < 0) {
        return PathStore.galacticSearchBBlue
      } else {
        return PathStore.galacticSearchBRed
      }
    }*/ //End Else 1

    //if the robot starts in front of path B/D balls - should be a little bit faster
    //change trajectories accordingly!
    //Option 2 Method 1
    /*return when (Pair(ballsOnLeft, ballsOnRight)){
      Pair(true,true) -> if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) PathStore.galacticSearchABlue
      else PathStore.galacticSearchARed
      Pair(true,false) -> PathStore.galacticSearchBBlue //only on left
      Pair(false,true) -> PathStore.galacticSearchBRed //only on right
      else -> PathStore.galacticSearchARed
    }*/

    //Option 2 Method 2
    /*if (ballsOnLeft && ballsOnRight){
      if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) {
        return PathStore.galacticSearchABlue
      } else {
        return PathStore.galacticSearchARed
      }
    }
    else if (ballsOnLeft && !ballsOnRight){
      return PathStore.galacticSearchBBlue
    }
    else if (!ballsOnLeft && ballsOnRight){
      return PathStore.galacticSearchBRed
    }
    else{
      return PathStore.galacticSearchARed
    }*/

    //Option 3
    //Make sure trajectories are updated to match if this is chosen
    //for path A, start on row C for red and row E for blue
    //for path B, start on row B for red and row D for blue
    if (ballsOffCenter == 1) {
      if (ballsOnLeft){
      //if (offCenterTarget.yaw.degrees.absoluteValue < 0.degrees){
        return PathStore.galacticSearchBBlue
      }
      else /*if (ballsOnRight)*/ {
        return PathStore.galacticSearchBRed
      }
    }
    else /*if (ballsOffCenter == 2)*/ {
      if (ballsOnRight) {
        return PathStore.galacticSearchARed
      }
      else {
        return PathStore.galacticSearchABlue
      }
    }
  }

  //we could also add the alternative logic for clearer option 3

}
