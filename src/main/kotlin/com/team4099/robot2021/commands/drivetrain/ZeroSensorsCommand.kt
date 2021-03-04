package com.team4099.robot2021.commands.drivetrain

import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase

class ZeroSensorsCommand : CommandBase() {

  init {
    addRequirements(Drivetrain)
  }

  override fun initialize() {
    Drivetrain.zeroSensors()
  }

  override fun isFinished(): Boolean {
    return true
  }
}
