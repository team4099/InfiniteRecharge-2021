package com.team4099.robot2021.commands.intake

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.CommandBase

class LiftIntakeCommand : CommandBase() {
  init {
    addRequirements(Intake)
  }

  override fun initialize() {
    Intake.armState = Constants.Intake.ArmPosition.IN
    Logger.addEvent("Intake", "Intake State: ${Intake.intakeState} Arm State: ${Intake.armState}")
  }
  override fun execute() {}
  override fun isFinished(): Boolean {
    return false
  }
  override fun end(interrupted: Boolean) {
    Intake.armState = Constants.Intake.ArmPosition.OUT
  }
}
