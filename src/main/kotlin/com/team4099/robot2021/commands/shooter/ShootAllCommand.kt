package com.team4099.robot2021.commands.shooter

import com.team4099.robot2021.commands.feeder.WaitUntilCommand
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.WaitCommand

class ShootAllCommand : ParallelRaceGroup() {
  init{
    addRequirements(Shooter)

    addCommands(
      ParallelRaceGroup(
        WaitCommand(5.0), //subject to change
        WaitUntilCommand(0),
        ShootCommand()
      )
    )
  }
}
