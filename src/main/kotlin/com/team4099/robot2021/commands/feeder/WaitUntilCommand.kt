package com.team4099.robot2021.commands.feeder

import com.team4099.robot2021.subsystems.Feeder
import edu.wpi.first.wpilibj2.command.CommandBase

class WaitUntilCommand(var desiredBallCount : Int) : CommandBase() {
  init {
    addRequirements(Feeder)
  }

  override fun isFinished(): Boolean {
    return Feeder.ballCount == desiredBallCount
  }
}
