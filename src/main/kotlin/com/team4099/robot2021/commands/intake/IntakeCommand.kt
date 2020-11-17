package com.team4099.robot2021.commands.intake

import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.CommandBase

class IntakeCommand(var state: Constants.Intake.IntakeState): CommandBase() {
  init {
      addRequirements(Intake)
  }
  //var speed = state {
//
  //  else -> 0
 // }
 // override fun initialize() {
 //   Intake.setVelocity(1.0)
  //}


}
