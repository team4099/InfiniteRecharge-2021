package com.team4099.robot2021.commands.drivetrain

import com.team4099.lib.hal.Clock
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.CommandBase

class AutoDriveCommand(private val path: Trajectory) : CommandBase() {
  init {
    addRequirements(Drivetrain)
  }

  override fun initialize() {
    Drivetrain.path = this.path
  }

  override fun execute() {
    Drivetrain.updatePathFollowing(Clock.fpgaTime)
    Drivetrain.updateOdometry()
  }

  override fun isFinished(): Boolean {
    return Drivetrain.isPathFinished(Clock.fpgaTime)
  }
}
