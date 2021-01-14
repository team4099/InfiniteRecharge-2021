package com.team4099.robot2021.commands.drivetrain

import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.Subsystem

class AutoDriveCommand(private val path: Trajectory) : CommandBase() {
  init {
    addRequirements(Drivetrain)
  }

  override fun initialize() {
    Drivetrain.path = this.path
  }

  override fun isFinished(): Boolean {
    return Drivetrain.isPathFinished(Timer.getFPGATimestamp())
  }
}