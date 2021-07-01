package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class ShootCommand : CommandBase() {
  init {
    addRequirements(Shooter)
    addRequirements(Feeder)
  }

  var shooterReady = false

  override fun initialize() {
    // make distance not private if we want to use a different number for the threshold
    Shooter.hoodState = Shooter.HoodPosition.EXTENDED
    //        when (Vision.currentDistance) {
    //          Vision.DistanceState.LINE -> Shooter.HoodPosition.RETRACTED
    //          Vision.DistanceState.NEAR -> Shooter.HoodPosition.RETRACTED
    //          else -> Shooter.HoodPosition.EXTENDED
    //        }

    Shooter.targetVelocity = Constants.Shooter.POWER_CELL_CHALLENGE_RPM
    //        when (Vision.currentDistance) {
    //          Vision.DistanceState.LINE -> Constants.Shooter.LINE_VELOCITY
    //          Vision.DistanceState.NEAR -> Constants.Shooter.NEAR_VELOCITY
    //          Vision.DistanceState.MID -> Constants.Shooter.MID_VELOCITY
    //          Vision.DistanceState.FAR -> Constants.Shooter.FAR_VELOCITY
    //        }

    shooterReady = false
    Logger.addEvent("ShootCommand", "Started shoot command")
  }

  override fun execute() {
    shooterReady =
        shooterReady or
            ((Shooter.targetVelocity - Shooter.currentVelocity).absoluteValue <
                Constants.Shooter.VELOCITY_TOLERANCE)

    if (shooterReady) {
      // run feeder
      Feeder.feederState = Feeder.FeederState.SHOOT
      Logger.addEvent(
          "ShootCommand",
          "Running feeder to shoot " +
              "(error: ${(Shooter.targetVelocity - Shooter.currentVelocity).absoluteValue})")
    } else {
      // stop running feeder
      Feeder.feederState = Feeder.FeederState.NEUTRAL
      Logger.addEvent(
          "ShootCommand",
          "Preparing to shoot " +
              "(error: ${(Shooter.targetVelocity - Shooter.currentVelocity).absoluteValue}, " +
              "want ${Constants.Shooter.VELOCITY_TOLERANCE})")
    }
  }

  override fun isFinished(): Boolean {
    return false
  }
}
