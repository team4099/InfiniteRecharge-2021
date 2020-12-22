package com.team4099.robot2021.commands.failures

import com.team4099.lib.hal.Clock
import com.team4099.lib.units.base.Time
import com.team4099.lib.units.base.seconds
import com.team4099.robot2021.loops.FailureManager
import edu.wpi.first.wpilibj2.command.CommandBase

class ValidateCommand(val condition : () -> Boolean, val timeout : Time, val failure : FailureManager.Failures) : CommandBase() {
  private var startTime : Time = 0.seconds
  override fun initialize() {
    startTime = Clock.fpgaTime

  }

  override fun execute() {
    val elapseTime : Time = Clock.fpgaTime - startTime
    if(elapseTime > timeout) {
      FailureManager.errorFlags[failure] = true
    }
  }

  override fun isFinished(): Boolean {
    return condition()
  }

}
