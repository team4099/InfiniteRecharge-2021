package com.team4099.robot2021.commands.climber

import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Climber

class ClimberDefault: CommandBase() {
  init {
    addRequirements(Climber)
  }

  override fun initialize() {
    Climber.setPower(0.0)
  }
}
