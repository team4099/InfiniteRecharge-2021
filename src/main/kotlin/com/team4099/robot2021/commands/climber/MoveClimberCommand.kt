package com.team4099.robot2021.commands

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class MoveClimberCommand(val pos: Constants.ClimberPosition) : CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun initialize() {
    Logger.addEvent("Climber", "Climber Moved to $pos")
  }

  override fun execute() {
    Climber.setPosition(pos)
  }
}
// check if climber is locked, if locked don't move
