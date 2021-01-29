package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.inInches
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.derived.Angle
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.tan
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera

object Vision : SubsystemBase() {
  //for up close
  private val closeCamera : PhotonCamera = PhotonCamera("gloworm")
  private val closeCameraResult
    get() = closeCamera.getLatestResult()
  private val target
    get() = closeCameraResult.bestTarget

  //tv
  var hasCloseTargets = false
    get() = closeCamera.hasTargets()
  //tx
  //negative: target on left of screen
  var closeYaw = 0.0.degrees
    get() = target.yaw.degrees
  //ty
  //negative: target on bottom of screen
  var closePitch = 0.0.degrees
    get() = target.pitch.degrees
  //ta
  //area is 0-100%
  var closeArea = 0.0
    get() = target.area

  //from farther away
  private val farCamera : PhotonCamera = PhotonCamera("")
  private val farCameraResult
    get() = farCamera.getLatestResult()
  private val farTarget
    get() = farCameraResult.bestTarget

  var hasFarTargets = false
    get() = farCamera.hasTargets()
  var farYaw = 0.0.degrees
    get() = farTarget.yaw.degrees
  var farPitch = 0.0.degrees
    get() = farTarget.pitch.degrees
  var farArea = 0.0
    get() = farTarget.area

  enum class DistanceState {
    LINE, NEAR, MID, FAR
  }

  init {
    Logger.addSource("Vision","Pipeline"){ pipeline }
    Logger.addSource("Vision","Distance (inches)"){ distance }
  }

  var yawToUse = 0.0.degrees
    get() = when{
      hasCloseTargets && hasFarTargets -> { if (closeArea > farArea) closeYaw else farYaw }
      hasCloseTargets && !hasFarTargets -> closeYaw
      !hasCloseTargets && hasFarTargets -> farYaw
      else -> Constants.Vision.MAX_ANGLE_ERROR
    }
  var onTarget = false
    get() = yawToUse.absoluteValue < Constants.Vision.MAX_ANGLE_ERROR
  var steeringAdjust = 0.0

  var pipeline = Constants.Vision.DRIVER_PIPELINE_ID
    set(value) {
      //this method sets the pipeline index and entry
      closeCamera.setPipelineIndex(value)
      farCamera.setPipelineIndex(value)
      field = value
    }

  private val closeDistance: Length
    get() = (Constants.Vision.TARGET_HEIGHT - Constants.Vision.CLOSE_CAMERA_HEIGHT) / (Constants.Vision.CLOSE_CAMERA_ANGLE + closePitch).tan
  private val farDistance: Length
    get() = (Constants.Vision.TARGET_HEIGHT - Constants.Vision.FAR_CAMERA_HEIGHT) / (Constants.Vision.FAR_CAMERA_ANGLE + farPitch).tan

  private val distance: Length
    get() = if (hasCloseTargets && hasFarTargets) when {
      closeDistance < Constants.Vision.CAMERA_DIST_THRESHOLD -> closeDistance
      farDistance > Constants.Vision.CAMERA_DIST_THRESHOLD -> farDistance
      /*closeDistance > Constants.Vision.CAMERA_DIST_THRESHOLD
        && farDistance < Constants.Vision.CAMERA_DIST_THRESHOLD -> (closeDistance+farDistance)/2 */
      else -> (closeDistance+farDistance)/2
    } else if (hasCloseTargets && !hasFarTargets) closeDistance
    else if (!hasCloseTargets && hasFarTargets) farDistance
    else 0.0.inches

  //could use method from PhotonUtils to calculate distance
  /*private val distanceMeters: Double
    get() = PhotonUtils.calculateDistanceToTargetMeters(
      Constants.Vision.CAMERA_HEIGHT.inMeters,
      Constants.Vision.TARGET_HEIGHT.inMeters,
      Constants.Vision.CAMERA_ANGLE.inRadians,
      pitch.inRadians
    )*/

  val currentDistance: DistanceState
    get() = when(distance.value) {
      //not sure if using the same numbers will work but it should never be exactly 100
      in 0.0..Constants.Shooter.LINE_DISTANCE.inInches -> DistanceState.LINE
      in Constants.Shooter.LINE_DISTANCE.inInches..Constants.Shooter.NEAR_DISTANCE.inInches -> DistanceState.NEAR
      in Constants.Shooter.NEAR_DISTANCE.inInches..Constants.Shooter.MID_DISTANCE.inInches -> DistanceState.MID
      else -> DistanceState.FAR
    }


}
