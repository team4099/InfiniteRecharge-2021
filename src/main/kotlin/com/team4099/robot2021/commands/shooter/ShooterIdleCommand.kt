package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class ShooterIdleCommand : CommandBase() {
  init {
    addRequirements(Shooter)
    //    addRequirements(Vision)
  }

  override fun initialize() {
    Shooter.setOpenLoopPower(0.0)
    //    Vision.pipeline = Constants.Vision.DRIVER_PIPELINE_ID
    //    Vision.camera.setDriverMode(true)
    Logger.addEvent("ShooterIdleCommand", "Started shooter idle command")
  }
  override fun execute() {}
  override fun isFinished(): Boolean {
    return false
  }
}
