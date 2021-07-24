package com.team4099.robot2021.commands.climber

import com.team4099.lib.hal.Clock
import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class UnlockClimberCommand : CommandBase() {
  init {
    addRequirements(Climber)
  }
  private var initTime = Clock.fpgaTime

  override fun initialize() {
    initTime = Clock.fpgaTime
    Climber.setOpenLoopPower(0.0, 0.0)
    Climber.brakeApplied = false
  }

  override fun end(interrupted: Boolean) {
    if (!interrupted) {
      Logger.addEvent("Climber", "Climber Unlocked")
    }
  }

  override fun isFinished(): Boolean {
    return Clock.fpgaTime - initTime > Constants.Climber.BRAKE_RELEASE_TIMEOUT
  }
}
// check if climber has pressure, if not, don't unlock and add event that pressure failed
