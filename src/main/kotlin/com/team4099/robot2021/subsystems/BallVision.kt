package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.pathfollow.Trajectory
import com.team4099.lib.units.derived.degrees
import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlin.math.absoluteValue
import org.photonvision.PhotonCamera
import org.photonvision.PhotonTrackedTarget

object BallVision : SubsystemBase() {

  private val ballCamera: PhotonCamera = PhotonCamera("ballcam")
  private val ballCameraResult
    get() = ballCamera.getLatestResult()
  private val targets
    get() = ballCameraResult.getTargets()

  private var ballsOnLeft = false
  private var ballsOnRight = false

  enum class BallPath(val path: Trajectory) {
    NONE(PathStore.driveForward),
    A_RED(PathStore.galacticSearchARed),
    A_BLUE(PathStore.galacticSearchABlue),
    B_RED(PathStore.galacticSearchBRed),
    B_BLUE(PathStore.galacticSearchBBlue)
  }

  init {
    ballCamera.pipelineIndex = 0

    Logger.addSource("BallVision", "Ball Camera Pipeline") { ballCamera.pipelineIndex }
    Logger.addSource("BallVision", "Best Ball Path") { choosePath().name }
    Logger.addSource("BallVision", "Number of Targets") { targets.size }

    Logger.addSource("BallVision", "Balls On Left") { targets.size }
    Logger.addSource("BallVision", "Balls On Right") { targets.size }
  }

  fun choosePath(): BallPath {
    if (targets.isEmpty()) {
      return BallPath.NONE
    }

    var centerTarget: PhotonTrackedTarget = targets[0]
    // var offCenterTarget: PhotonTrackedTarget = targets[0]
    // var pathA = false
    var ballsOffCenter = 0

    for (target in targets) {
      if (target.yaw.degrees.absoluteValue < Constants.BallVision.CENTER_YAW_THRESHOLD) {
        // comment path if using option 2
        // pathA = true
        centerTarget = target
      } else {
        // lines for option 3
        // offCenterTarget = target
        ballsOffCenter++
        // option 2 + 3?
        if (target.yaw.degrees < 0.degrees) {
          ballsOnLeft = true
        } else {
          ballsOnRight = true
        }
      }
    }

    // Option 1 & 2
    /*if (pathA) {
      //balls in center means path A
      //check area of target (distance of ball) to see if it is red or blue - test to find values for this
      //could use pitch instead if camera is high enough on the robot
      if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) {
        return BallPath.A_BLUE
      } else {
        return BallPath.A_RED
      }
    } else { //ELSE: Option 1 Only
      //no balls in center means path B
      //assume the robot was placed accordingly - above row B if red, below row D if blue
      if (centerTarget.yaw < 0) {
        return BallPath.B_BLUE
      } else {
        return BallPath.B_RED
      }
    }*/
    // End Else 1

    // if the robot starts in front of path B/D balls - should be a little bit faster
    // change trajectories accordingly!
    // Option 2 Method 1
    return when (Pair(ballsOnLeft, ballsOnRight)) {
      Pair(true, true) ->
          if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) BallPath.A_BLUE
          else BallPath.A_RED
      Pair(true, false) -> BallPath.B_BLUE // only on left
      Pair(false, true) -> BallPath.B_RED // only on right
      else -> BallPath.NONE
    }

    // Option 2 Method 2
    /*if (ballsOnLeft && ballsOnRight){
      if (centerTarget.area < Constants.BallVision.PATH_A_AREA_THRESHOLD) {
        return BallPath.A_BLUE
      } else {
        return BallPath.A_RED
      }
    }
    else if (ballsOnLeft && !ballsOnRight){
      return BallPath.B_BLUE
    }
    else if (!ballsOnLeft && ballsOnRight){
      return BallPath.B_RED
    }
    else{
      return BallPath.NONE
    }*/

    // Option 3
    // Make sure trajectories are updated to match if this is chosen
    // for path A, start on row C for red and row E for blue
    // for path B, start on row B for red and row D for blue
    /*if (ballsOffCenter == 1) {
      if (ballsOnLeft){
      //if (offCenterTarget.yaw.degrees.absoluteValue < 0.degrees){
        return BallPath.B_BLUE
      }
      else /*if (ballsOnRight)*/ {
        return BallPath.B_RED
      }
    }
    else /*if (ballsOffCenter == 2)*/ {
      if (ballsOnRight) {
        return BallPath.A_RED
      }
      else {
        return BallPath.A_BLUE
      }
    }*/

    // we could also add the alternative logic for clearer option 3
  }
}
