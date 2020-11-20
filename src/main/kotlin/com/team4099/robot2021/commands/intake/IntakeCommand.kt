package com.team4099.robot2021.commands.intake

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.CommandBase

class IntakeCommand(var intakeState: Constants.Intake.IntakeState, var armState: Constants.Intake.ArmPosition): CommandBase() {
  init {
    addRequirements(Intake)
    Logger.addSource(Constants.Intake.TAB, "Intake State") { intakeState.toString()}
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Power") { Intake.intakePower() }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Current") { Intake.intakeCurrent() }

    when(intakeState) {
      Constants.Intake.IntakeState.DEFAULT ->
        Logger.addEvent(Constants.Intake.INTAKE_STATE, "Default")
      Constants.Intake.IntakeState.IN ->
        Logger.addEvent(Constants.Intake.INTAKE_STATE, "In")
      Constants.Intake.IntakeState.OUT ->
        Logger.addEvent(Constants.Intake.INTAKE_STATE, "Out")
    }

    when(armState) {
      Constants.Intake.ArmPosition.IN ->
        Logger.addEvent(Constants.Intake.ARM_STATE, "In")
      Constants.Intake.ArmPosition.OUT ->
        Logger.addEvent(Constants.Intake.ARM_STATE, "Out")
      Constants.Intake.ArmPosition.DEFAULT ->
        Logger.addEvent(Constants.Intake.ARM_STATE, "Default")
    }
  }

  override fun initialize() {
    Intake.setPosition(armState)
    Intake.setVelocity(intakeState)
  }

}
