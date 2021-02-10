package com.team4099.robot2021.commands.intake

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.CommandBase

class IntakeCommand(
  var intakeState: Constants.Intake.IntakeState,
  var armState: Constants.Intake.ArmPosition
) : CommandBase() {

  init {
    addRequirements(Intake)
  }

  override fun initialize() {
    Intake.armState = armState
    Intake.intakeState = intakeState
    Logger.addEvent("Intake", "Intake State: $intakeState Arm State: $armState")
  }
}
