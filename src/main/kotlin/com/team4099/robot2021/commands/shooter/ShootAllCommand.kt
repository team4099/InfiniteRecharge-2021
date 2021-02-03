package com.team4099.robot2021.commands.shooter

import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase

class ShootAllCommand : CommandBase() {
  init {
      addRequirements(Shooter, Feeder)
  }

  override fun initialize() {
    while(Feeder.ballCount > 0) {
      ShootCommand()
    }
  }
}
