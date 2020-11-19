package com.team4099.robot2021.commands

import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class ShootCommand : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize(){
    Shooter.targetVelocity = Constants.Shooter.TARGET_VELOCITY
  }
  override fun execute(){

  }
  override fun isFinished():Boolean{
    // don't actually do this
    return Shooter.currentVelocity != Constants.Shooter.TARGET_VELOCITY
  }


}
