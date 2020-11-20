package com.team4099.robot2021.commands

import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class MoveClimber(val pos: Constants.ClimberPosition): CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun execute() {
    Climber.setPosition(pos)
  }
}
