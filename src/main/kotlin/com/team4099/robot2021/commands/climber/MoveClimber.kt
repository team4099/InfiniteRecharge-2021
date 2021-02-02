package com.team4099.robot2021.commands.climber

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.commands.climber.UnlockClimber
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase
/**
 * Move climber
 *
 * @property pos Direct motor to rotate based on position
 */
class MoveClimber(val pos: Constants.ClimberPosition): CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun initialize() {
    Logger.addEvent("Climber", "Climber Moved to $pos")
  }

  override fun execute() {
    if (!Climber.brakeApplied) {
      Climber.setPosition(pos)
    }
  }
}
//check if climber is locked, if locked don't move
