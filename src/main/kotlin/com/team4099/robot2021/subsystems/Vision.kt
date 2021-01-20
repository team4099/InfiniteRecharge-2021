package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.inInches
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.tan
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera

object Vision : SubsystemBase() {
  //for up close
  private val camera : PhotonCamera = PhotonCamera("gloworm")
  //from farther away
  private val zoomedCamera : PhotonCamera = PhotonCamera("")

  private val cameraResult
    get() = camera.getLatestResult()
  private val target
    get() = cameraResult.bestTarget

  //tv
  var hasTargets = false
    get() = camera.hasTargets()
  //tx
  //negative: target on left of screen
  var yaw = 0.0.degrees
    get() = target.yaw.degrees
  //ty
  //negative: target on bottom of screen
  var pitch = 0.0.degrees
    get() = target.pitch.degrees
  //ta
  //area is 0-100%
  var area = 0.0
    get() = target.area


  enum class DistanceState() {
    LINE, NEAR, MID, FAR
  }

  init {
    Logger.addSource("Vision","Pipeline"){ pipeline }
    Logger.addSource("Vision","Distance (inches)"){ distance }
  }

  var steeringAdjust = 0.0
  var onTarget = false
    get() = yaw.absoluteValue < Constants.Vision.MAX_ANGLE_ERROR

  var pipeline = Constants.Vision.DRIVER_PIPELINE_ID
    set(value) {
      //this method sets the pipeline index and entry
      camera.setPipelineIndex(value)
      field = value
    }

  private val distance: Length
    get() = (Constants.Vision.TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) / (Constants.Vision.CAMERA_ANGLE + pitch).tan

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
