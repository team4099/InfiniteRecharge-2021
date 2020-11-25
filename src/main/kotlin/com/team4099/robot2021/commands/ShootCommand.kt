package com.team4099.robot2021.commands

import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj2.command.CommandBase

class ShootCommand : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize(){
    Shooter.targetVelocity = Constants.Shooter.TARGET_VELOCITY
  }
  override fun execute(){
    var shooterReady = (Constants.Shooter.TARGET_VELOCITY - Shooter.currentVelocity).absoluteValue < Constants.Shooter.VELOCITY_TOLERANCE
    if (shooterReady) { // && vision aligned on target
      //run feeder
    }
    else{
      //stop running feeder
    }
  }
  override fun isFinished() : Boolean {
    return false
  }


}
