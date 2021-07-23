package com.team4099.robot2021.commands.intake

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class PrepareClimbCommand : CommandBase() {
  init {
    addRequirements(Intake, Shooter)
  }

  override fun initialize() {
    Intake.intakeState = Constants.Intake.IntakeState.IDLE
    Intake.armState = Constants.Intake.ArmPosition.IN
    Shooter.hoodState = Shooter.HoodPosition.RETRACTED
    Logger.addEvent("Intake", "Intake State: ${Intake.intakeState} Arm State: ${Intake.armState}")
    Logger.addEvent("Shooter","Hood Retracted")
  }
  override fun execute() {}
  override fun isFinished(): Boolean {
    return false
  }
}
