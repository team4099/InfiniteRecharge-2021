package com.team4099.robot2021.commands.drivetrain

import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Drivetrain

class ZeroGyroCommand: CommandBase() {

  init{
    addRequirements(Drivetrain)
  }

  override fun initialize(){
    Drivetrain.zeroSensors()
  }

  override fun isFinished() : Boolean {
    return true
  }


}
