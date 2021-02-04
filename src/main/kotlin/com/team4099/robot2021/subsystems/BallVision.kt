package com.team4099.robot2021.subsystems

import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera

object BallVision : SubsystemBase() {

  private val ballCamera : PhotonCamera = PhotonCamera("ballcam")
  private val ballCameraResult
    get() = ballCamera.getLatestResult()
  private val targets = ballCameraResult.getTargets()

  init {
    ballCamera.setPipelineIndex(Constants.Vision.DRIVER_PIPELINE_ID)
  }

}
