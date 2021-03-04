package com.team4099.robot2021.commands.drivetrain

import com.team4099.lib.pathfollow.Trajectory
import edu.wpi.first.wpilibj2.command.CommandBase

class LoopPathCommand(private val trajectory: Trajectory) : CommandBase() {
  private val loopedDriveCommand = AutoDriveCommand(trajectory)

  init {
    addRequirements(*loopedDriveCommand.requirements.toTypedArray())
  }

  override fun initialize() {
    loopedDriveCommand.initialize()
  }

  override fun execute() {
    if (loopedDriveCommand.isFinished) loopedDriveCommand.initialize()
    loopedDriveCommand.execute()
  }
}
