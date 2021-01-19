package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Shooter
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj2.command.CommandBase

class ShootCommand : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize(){
    Shooter.targetVelocity = when(Vision.currentDistance) {
      Vision.DistanceState.LINE -> Constants.Shooter.LINE_VELOCITY
      Vision.DistanceState.NEAR -> Constants.Shooter.NEAR_VELOCITY
      Vision.DistanceState.MID -> Constants.Shooter.MID_VELOCITY
      Vision.DistanceState.FAR -> Constants.Shooter.FAR_VELOCITY
      }
    Logger.addEvent("ShootCommand","Started shoot command")
  }

  override fun execute(){
    var shooterReady = (Constants.Shooter.TARGET_VELOCITY - Shooter.currentVelocity).absoluteValue < Constants.Shooter.VELOCITY_TOLERANCE

    if (shooterReady && Vision.onTarget) {
      //run feeder
      Feeder.feederState = Feeder.FeederState.FORWARD_ALL
      Logger.addEvent("ShootCommand","Running feeder to shoot")
    }
    else{
      //stop running feeder
      Feeder.feederState = Feeder.FeederState.NEUTRAL
      Logger.addEvent("ShootCommand","Preparing to shoot")
    }
  }

  override fun isFinished() : Boolean {
    return false
  }

}
