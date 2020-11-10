package com.team4099.robot2021.commands.intake

import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.CommandBase

class IntakeDefault: CommandBase() {
  init {
      addRequirements(Intake)
  }

  override fun initialize() {
    Intake.setPower(0.0)
  }
}
