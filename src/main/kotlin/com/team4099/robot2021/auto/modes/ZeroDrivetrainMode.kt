package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.commands.drivetrain.ResetZeroCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class ZeroDrivetrainMode : SequentialCommandGroup() {
  init {
    addCommands(ResetZeroCommand())
  }
}
