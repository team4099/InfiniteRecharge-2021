package com.team4099.robot2021.commands.drivetrain

import com.team4099.lib.hal.Clock
import com.team4099.lib.pathfollow.Trajectory
import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Drivetrain

class AutoDriveCommand(private val path: Trajectory) : CommandBase() {

  init {
    addRequirements(Drivetrain)
  }

  override fun initialize() {
    Drivetrain.path = this.path
  }

  override fun execute() {
    Drivetrain.updatePathFollowing(Clock.fpgaTime)
  }

  override fun isFinished(): Boolean {
    return Drivetrain.isPathFinished(Clock.fpgaTime)
  }
}
