package com.team4099.robot2021.commands.feeder

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.subsystems.Feeder
import edu.wpi.first.wpilibj2.command.CommandBase

class FeederBeamBreak : CommandBase() {
  init {
    addRequirements(Feeder)
  }
  override fun initialize() {
    Logger.addEvent("Feeder", "Beam break started")
  }
  override fun execute() {
    Feeder.feederState =
        when {
          Feeder.topBeamBroken -> Feeder.FeederState.NEUTRAL
          Feeder.bottomBeamBroken -> Feeder.FeederState.FORWARD_ALL
          else -> Feeder.FeederState.FORWARD_FLOOR
        }
  }
}
