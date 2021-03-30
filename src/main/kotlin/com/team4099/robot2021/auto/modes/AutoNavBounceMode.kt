package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class AutoNavBounceMode : SequentialCommandGroup() {
  init {
    addRequirements(Drivetrain)

    addCommands(
      AutoDriveCommand(PathStore.autonavBounce1),
      AutoDriveCommand(PathStore.autonavBounce2),
      AutoDriveCommand(PathStore.autonavBounce3),
      AutoDriveCommand(PathStore.autonavBounce4)
    )

  }
}
