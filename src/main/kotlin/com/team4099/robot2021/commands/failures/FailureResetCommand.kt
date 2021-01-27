package com.team4099.robot2021.commands.failures
import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.loops.FailureManager;

/**
 * Failure reset command
 *
 * @constructor Create empty Failure reset command
 */
class FailureResetCommand : CommandBase() {
  override fun initialize(){
    FailureManager.reset();
  }
}
