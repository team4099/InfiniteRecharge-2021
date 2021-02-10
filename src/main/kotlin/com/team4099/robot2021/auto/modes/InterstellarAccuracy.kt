package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.commands.shooter.ShootCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class InterstellarAccuracy: SequentialCommandGroup() {
  init {
    addRequirements(Drivetrain, Intake, Shooter)

    addCommands(
      ShootCommand(),
      AutoDriveCommand(PathStore.fromGreentoReintroduction),
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).withTimeout(15.0),

      AutoDriveCommand(PathStore.fromIntrotoYellow),
      ShootCommand(),
      AutoDriveCommand(PathStore.fromYellowtoReintroduction),
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).withTimeout(15.0),

      AutoDriveCommand(PathStore.fromIntrotoBlue),
      ShootCommand(),
      AutoDriveCommand(PathStore.fromBluetoReintroduction),
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).withTimeout(15.0),

      AutoDriveCommand(PathStore.fromIntrotoRed),
      ShootCommand(),
      AutoDriveCommand(PathStore.fromRedtoReintroduction),
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).withTimeout(15.0),

      //Change to most optiable zone
      AutoDriveCommand(PathStore.fromIntrotoGreen),
      ShootCommand(),
      AutoDriveCommand(PathStore.fromGreentoReintroduction),
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).withTimeout(15.0),
      AutoDriveCommand(PathStore.fromIntrotoGreen),
      ShootCommand(),
      AutoDriveCommand(PathStore.fromGreentoReintroduction),
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).withTimeout(15.0),
      AutoDriveCommand(PathStore.fromIntrotoGreen),
      ShootCommand()
    )
  }
}
