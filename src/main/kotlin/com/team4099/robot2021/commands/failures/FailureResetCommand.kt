package com.team4099.robot2021.commands.failures
import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.loops.FailureManager;

/**
 * Failure reset command
 *
 * @property FailureManager.reset Sets all errors back to normal
 */
class FailureResetCommand : CommandBase() {
  override fun initialize(){
    FailureManager.reset()
  }
}
