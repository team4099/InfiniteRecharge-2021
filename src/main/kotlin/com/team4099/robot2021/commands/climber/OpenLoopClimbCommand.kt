package com.team4099.robot2021.commands.climber

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class OpenLoopClimbCommand(private val velocity: Double) : CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun initialize() {
    Climber.climbVelocity = velocity
    Logger.addEvent("Climber", "Climber moving at $velocity")
  }
}
