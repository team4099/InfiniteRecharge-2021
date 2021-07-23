package com.team4099.robot2021.commands.climber

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.inInches
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj2.command.CommandBase

class OpenLoopClimbCommand(private val power: () -> Double) : CommandBase() {

  init {
    addRequirements(Climber)
  }

  override fun initialize() {
    Logger.addEvent("Climber", "Climber power at ${power()}")
    // Intake.armState = Constants.Intake.ArmPosition.IN
  }

  override fun isFinished(): Boolean {
    return false
  }

  override fun execute() {
    val powerDifference =
        (Climber.climberLArmSensor.position - Climber.climberRArmSensor.position).inInches *
            Constants.Climber.POSITION_P

    if (!Climber.brakeApplied) {
      Climber.setOpenLoopPower(power() - powerDifference, power() + powerDifference)
    }
  }

  override fun end(interrupted: Boolean) {
    Climber.setOpenLoopPower(0.0, 0.0)
  }
}
