package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj2.command.CommandBase

class SpinUpCommand(
  private val withVision: Boolean = false,
  private val accuracy: Boolean = false,
  private val distance: Vision.DistanceState = Vision.DistanceState.NEAR
) : CommandBase() {
  init {
    addRequirements(Shooter)
  }

  override fun initialize() {
    Logger.addEvent("SpinUpCommand", "Started shooter spin-up command")
  }
  override fun execute() {
    if (withVision) {
      Shooter.targetVelocity =
          when (Vision.currentDistance) {
            Vision.DistanceState.NEAR -> Constants.Shooter.NEAR_VELOCITY
            Vision.DistanceState.LINE -> Constants.Shooter.LINE_VELOCITY
            Vision.DistanceState.MID -> Constants.Shooter.MID_VELOCITY
            Vision.DistanceState.FAR -> Constants.Shooter.FAR_VELOCITY
          }
    } else {
      if (accuracy) {
        Shooter.targetVelocity =
            when (distance) {
              Vision.DistanceState.NEAR -> Constants.Shooter.NEAR_VELOCITY
              Vision.DistanceState.LINE -> Constants.Shooter.LINE_VELOCITY
              Vision.DistanceState.MID -> Constants.Shooter.MID_VELOCITY
              Vision.DistanceState.FAR -> Constants.Shooter.FAR_VELOCITY
            }
        Shooter.hoodState =
            when (distance) {
              Vision.DistanceState.LINE -> Shooter.HoodPosition.EXTENDED
              Vision.DistanceState.NEAR -> Shooter.HoodPosition.RETRACTED
              Vision.DistanceState.MID -> Shooter.HoodPosition.EXTENDED
              Vision.DistanceState.FAR -> Shooter.HoodPosition.EXTENDED
            }
      } else {
        Shooter.hoodState = Shooter.HoodPosition.EXTENDED
        Shooter.targetVelocity = Constants.Shooter.POWER_CELL_CHALLENGE_RPM
      }
    }
  }
  override fun isFinished(): Boolean {
    return false
  }
}
