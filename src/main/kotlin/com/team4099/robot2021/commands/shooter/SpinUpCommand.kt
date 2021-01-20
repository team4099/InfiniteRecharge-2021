package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class SpinUpCommand(var withVision : Boolean) : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize() {
    Logger.addEvent("SpinUpCommand","Started shooter spin-up command")
  }
  override fun execute() {
    Shooter.targetVelocity = when(withVision){
      true -> Constants.Shooter.LINE_VELOCITY
      false -> 5000.rotations.perMinute
    }
  }
  override fun isFinished() : Boolean {
    return false
  }
}
