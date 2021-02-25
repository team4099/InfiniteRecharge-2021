package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.shooter.ShootAllCommand
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class Shoot3Mode : SequentialCommandGroup() {
  init{
    addRequirements(Shooter)

    addCommands(
      ShootAllCommand(),
      AutoDriveCommand(PathStore.driveForward)
    )
  }
}
