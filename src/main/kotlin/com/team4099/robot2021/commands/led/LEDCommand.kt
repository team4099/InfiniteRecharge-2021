package com.team4099.robot2021.commands.led

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.LED
import edu.wpi.first.wpilibj2.command.CommandBase

class LEDCommand(var LEDstate: Constants.LED.Status) : CommandBase() {
  init {
    addRequirements(LED)
  }

  override fun initialize() {
    LED.statusState = LEDstate
    Logger.addEvent("LED", "LED State: $LEDstate")
  }
}
