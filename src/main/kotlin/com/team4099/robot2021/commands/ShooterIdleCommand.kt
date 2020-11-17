package com.team4099.robot2021.commands

import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class ShooterIdleCommand : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize(){
    Shooter.setVelocity(0.rotations.perMinute)
  }
  override fun execute(){

  }
  override fun isFinished() : Boolean {
    return false
  }

}
