package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.tan
import com.team4099.lib.units.inDegreesPerSecond
import com.team4099.lib.units.inRotationsPerMinute
import com.team4099.robot2021.config.Constants
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera
import org.photonvision.PhotonUtils
import java.lang.Math.tan

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
    Logger.addSource("Vision","Pipeline"){ pipelineIndex }
    Logger.addSource("Vision","Distance (inches)"){ distance }
  }

  var steeringAdjust = 0.0
  var onTarget = false
    get() = yaw.absoluteValue < Constants.Vision.MAX_ANGLE_ERROR

  private val pipelineIndex = camera.pipelineIndex

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
      in 0.0..100.0 -> DistanceState.LINE
      in 101.0..129.0 -> DistanceState.NEAR
      in 130.0..249.0 -> DistanceState.MID
      else -> DistanceState.FAR
    }


}
