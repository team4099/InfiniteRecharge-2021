package com.team4099.robot2021.commands.feeder

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.subsystems.Feeder
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandBase

class FeederSerialize : CommandBase() {

  var lastBrokenTime = 0.0

  init {
    addRequirements(Feeder)
  }
  override fun initialize() {
    Logger.addEvent("Feeder", "Beam break started")
  }
  override fun execute() {
    if (Feeder.bottomBeamBroken) {
      lastBrokenTime = Timer.getFPGATimestamp()
    }
    Feeder.feederState =
        when {
          Feeder.topBeamBroken -> Feeder.FeederState.NEUTRAL
          //          (Timer.getFPGATimestamp() - lastBrokenTime < 0.2) ->
          // Feeder.FeederState.FORWARD_ALL
          else -> Feeder.FeederState.FORWARD_ALL
        }
  }
}
