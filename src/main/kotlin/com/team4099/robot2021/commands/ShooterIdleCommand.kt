package com.team4099.robot2021.commands

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Shooter
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj2.command.CommandBase

class ShooterIdleCommand : CommandBase() {
  init{
    addRequirements(Shooter)
  }

  override fun initialize(){
    Shooter.setOpenLoopPower(0.0)
    Vision.pipeline = Constants.Vision.DRIVER_PIPELINE_ID
    Logger.addEvent("ShooterIdleCommand","Started shooter idle command")
  }
  override fun execute(){

  }
  override fun isFinished() : Boolean {
    return false
  }

}
