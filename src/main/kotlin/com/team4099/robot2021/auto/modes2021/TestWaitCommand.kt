package com.team4099.robot2021.auto.modes2021

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand

class TestWaitCommand : SequentialCommandGroup() {
  init {
    addRequirements(Drivetrain)

    addCommands(WaitCommand(2.0), AutoDriveCommand(PathStore.driveForward))
  }
}
