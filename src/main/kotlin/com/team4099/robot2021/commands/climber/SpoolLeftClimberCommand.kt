package com.team4099.robot2021.commands.climber

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class SpoolLeftClimberCommand : CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun initialize() {}

  override fun isFinished(): Boolean {
    return false
  }

  override fun execute() {
    if (!Climber.brakeApplied) {
      Climber.setOpenLoopPower(-0.2, -0.0, false)
    }

    Logger.addEvent("Climber", "Left climber spooling")
  }

  override fun end(interrupted: Boolean) {
    Climber.setOpenLoopPower(0.0, 0.0)
    Climber.zeroLeftEncoder()
  }
}
