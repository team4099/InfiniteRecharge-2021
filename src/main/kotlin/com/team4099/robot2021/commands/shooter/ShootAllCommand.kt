package com.team4099.robot2021.commands.shooter

import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import edu.wpi.first.wpilibj2.command.WaitUntilCommand

class ShootAllCommand : ParallelRaceGroup() {
  init {
    addCommands(
        // SpinUpCommand(),
        WaitCommand(4.0),
        // WaitUntilCommand { Feeder.ballCount < 1 },
        ShootCommand())
  }
}
