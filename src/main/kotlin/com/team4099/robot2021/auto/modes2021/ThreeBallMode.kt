package com.team4099.robot2021.auto.modes2021

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.shooter.ShootAllCommand
import com.team4099.robot2021.commands.shooter.VisionCommand
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand

class ThreeBallMode : SequentialCommandGroup() {
  init {
    addRequirements(Drivetrain, Shooter)

    addCommands(
        AutoDriveCommand(PathStore.toPowerPort),
        WaitCommand(0.5),
        // VisionCommand(),
        ShootAllCommand())
  }
}
