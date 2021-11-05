package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.commands.shooter.ShootCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand

class DelayedIntakeAutoToFarTrench: ParallelCommandGroup() {

  init {
    addRequirements(Drivetrain, Intake, Shooter)

    addCommands(
        ParallelCommandGroup(
          SequentialCommandGroup(AutoDriveCommand(PathStore.toFarTrench), WaitCommand(0.5)),
          SequentialCommandGroup(
            //put intake down
            IntakeCommand(
              Constants.Intake.IntakeState.IDLE,
              Constants.Intake.ArmPosition.OUT
              ),
            WaitCommand(0.0), //to-do: distance travelling over robot speed * 0.75 (figure out how PIDs work first)
            //
            IntakeCommand(
              Constants.Intake.IntakeState.IN,
              Constants.Intake.ArmPosition.OUT
              )
            ),
          AutoDriveCommand(PathStore.fromFarTrench),
          ShootCommand()
        )
    )
  }
}
