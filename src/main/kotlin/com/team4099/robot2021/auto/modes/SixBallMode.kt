package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.commands.shooter.ShootCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class SixBallMode : SequentialCommandGroup() {

  init {
    addRequirements(Shooter, Drivetrain, Intake)

    addCommands(
        ShootCommand(),
        ParallelRaceGroup(
            SequentialCommandGroup(
                AutoDriveCommand(PathStore.toNearTrench),
                AutoDriveCommand(PathStore.intakeInNearTrench)),
            IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT)),
        AutoDriveCommand(PathStore.fromNearTrench),
        ShootCommand())
  }
}
