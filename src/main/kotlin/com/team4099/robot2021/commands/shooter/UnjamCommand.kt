package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class UnjamCommand( private val throughShooter : Boolean ) : CommandBase() {
  init {
      addRequirements(Shooter)
  }

  override fun initialize() {
    Logger.addEvent("UnjamCommand", "Started unjam command")
  }

  override fun execute() {
    if(throughShooter) {
      Shooter.targetVelocity = Constants.Shooter.UNJAM_RPM
    }
    else {
      Shooter.targetVelocity = -Constants.Shooter.UNJAM_RPM
    }
  }
}
