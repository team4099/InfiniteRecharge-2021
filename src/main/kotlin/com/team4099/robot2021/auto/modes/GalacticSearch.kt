package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.BallVision
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup

class GalacticSearch : ParallelRaceGroup() {

  init {
    addRequirements(Drivetrain, Intake)

    addCommands(
      IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT),
      AutoDriveCommand(BallVision.ballPath)
    )
  }

}
