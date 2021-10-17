package com.team4099.robot2021.auto.modes2021

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.commands.shooter.ShootAllCommand
import com.team4099.robot2021.commands.shooter.VisionCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand

class EnemyTrenchMode : SequentialCommandGroup() {
  init {
    addRequirements(Drivetrain, Shooter, Intake)

    addCommands(
        ParallelRaceGroup(
            // SequentialCommandGroup(
            AutoDriveCommand(PathStore.toEnemyTrench),
            // WaitCommand(0.5)
            // ),
            IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT)),
        WaitCommand(3.0),
        AutoDriveCommand(PathStore.fromEnemyTrench), // ,
        AutoDriveCommand(PathStore.fromMiddleEnemyTrench),
        VisionCommand(),
        ShootAllCommand())
  }
}
