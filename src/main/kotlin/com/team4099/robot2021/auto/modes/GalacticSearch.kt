package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.BallVision
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class GalacticSearch : CommandBase() {
  lateinit var command: Command

  init {
    addRequirements(Drivetrain, Intake)
  }

  override fun initialize() {
    command = ParallelCommandGroup(
      // IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT),
      AutoDriveCommand(BallVision.choosePath().path)
    )
    command.initialize()
  }

  override fun execute() {
    command.execute()
  }

  override fun isFinished(): Boolean {
    return command.isFinished
  }
  override fun end(interrupted: Boolean) {
    command.end(interrupted)
  }

}
