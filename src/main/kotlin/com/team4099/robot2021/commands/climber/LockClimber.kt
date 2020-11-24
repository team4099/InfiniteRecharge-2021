package com.team4099.robot2021.commands.climber

import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class LockClimber : CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun initialize() {
    Climber.setOpenLoopPower(0.0)
    Climber.brakeApplied = false
  }
}
