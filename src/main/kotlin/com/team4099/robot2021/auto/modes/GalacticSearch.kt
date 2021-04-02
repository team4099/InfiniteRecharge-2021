package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.drivetrain.ResetZeroCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.BallVision
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class GalacticSearch : SequentialCommandGroup() {

  init {
    addRequirements(Drivetrain, Intake)

    addCommands(
        ResetZeroCommand(),
        ParallelRaceGroup(
            IntakeCommand(Constants.Intake.IntakeState.IDLE, Constants.Intake.ArmPosition.IN),
            // IntakeCommand(Constants.Intake.IntakeState.OUT, Constants.Intake.ArmPosition.OUT),
            AutoDriveCommand(BallVision.choosePath().path)))
  }
}
