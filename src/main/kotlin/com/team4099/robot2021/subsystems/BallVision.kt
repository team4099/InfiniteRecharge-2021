package com.team4099.robot2021.subsystems

import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.photonvision.PhotonCamera

object BallVision : SubsystemBase() {

  private val ballCamera : PhotonCamera = PhotonCamera("ballcam")

  init {

  }

}
