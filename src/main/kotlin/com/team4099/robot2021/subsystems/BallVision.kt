package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera

object BallVision : SubsystemBase() {

  private val ballCamera : PhotonCamera = PhotonCamera("ballcam")
  private val ballCameraResult
    get() = ballCamera.getLatestResult()
  private val targets = ballCameraResult.getTargets()

  var ballPath : Trajectory = PathStore.galacticSearchARed
    get() {
      //actually choose a path here
      return PathStore.galacticSearchARed
    }

  init {
    ballCamera.setPipelineIndex(Constants.Vision.DRIVER_PIPELINE_ID)

    Logger.addSource("BallVision","Best Ball Path"){ ballPath }
  }

}
