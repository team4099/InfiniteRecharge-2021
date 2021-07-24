package com.team4099.robot2021.commands.feeder

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Feeder
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandBase

class FeederSerialize : CommandBase() {

  var currentTime = 0.0
  var lastBrokenTime = 0.0
  var lastUnbrokenTime = 0.0

  init {
    addRequirements(Feeder)
  }
  override fun initialize() {
    Logger.addEvent("Feeder", "Beam break started")
  }
  override fun execute() {
    currentTime = Timer.getFPGATimestamp()
    if (Feeder.bottomBeamBroken) {
      lastBrokenTime = currentTime
    } else {
      lastUnbrokenTime = currentTime
    }
    Feeder.feederState =
        when {
          Feeder.topBeamBroken -> Feeder.FeederState.NEUTRAL
          (currentTime - lastBrokenTime < Constants.Feeder.BEAM_BREAK_BROKEN_TIME) ->
              Feeder.FeederState.FORWARD_ALL
          (lastUnbrokenTime - lastBrokenTime < Constants.Feeder.BEAM_BREAK_BACKWARDS_TIME) ->
              Feeder.FeederState.BACKWARD_VERTICAL
          else -> Feeder.FeederState.FORWARD_FLOOR
        }
  }
}
