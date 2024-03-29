package com.team4099.robot2021.auto.modesathome

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class SlalomMode : SequentialCommandGroup() {
  init {
    addRequirements(Drivetrain)

    addCommands(AutoDriveCommand(PathStore.slalomPath))
  }
}
