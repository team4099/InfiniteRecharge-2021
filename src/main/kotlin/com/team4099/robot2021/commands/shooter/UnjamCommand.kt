package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class UnjamCommand() : CommandBase() {
  init {
    addRequirements(Shooter, Feeder)
  }

  override fun initialize() {
    Logger.addEvent("UnjamCommand", "Started unjam command")
  }

  override fun execute() {
    Shooter.targetVelocity = -Constants.Shooter.UNJAM_RPM
    Feeder.feederState = Feeder.FeederState.BACKWARD
  }
}
