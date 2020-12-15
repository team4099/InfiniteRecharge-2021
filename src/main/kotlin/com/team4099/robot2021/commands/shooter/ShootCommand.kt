package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
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
    Shooter.targetVelocity = Constants.Shooter.TARGET_VELOCITY
    Logger.addEvent("ShootCommand","Started shoot command")
  }
  override fun execute(){
    var shooterReady = (Constants.Shooter.TARGET_VELOCITY - Shooter.currentVelocity).absoluteValue < Constants.Shooter.VELOCITY_TOLERANCE
    // maybe implement a goal distance
    var visionReady = Vision.onTarget
    //   && Vision.distance < Constants.Vision.MAX_DIST_ERROR

    if (shooterReady && visionReady) {
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
