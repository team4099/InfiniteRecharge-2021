package com.team4099.robot2021.commands

import com.team4099.lib.units.inRotationsPerMinute
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase
import java.lang.Math.abs

class ShootCommand : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize(){
    Shooter.targetVelocity = Constants.Shooter.TARGET_VELOCITY
  }
  override fun execute(){

  }
  override fun isFinished() : Boolean {
    return abs(Constants.Shooter.TARGET_VELOCITY.inRotationsPerMinute - Shooter.currentVelocity.inRotationsPerMinute) <
      Constants.Shooter.VELOCITY_OFFSET.inRotationsPerMinute
  }


}
