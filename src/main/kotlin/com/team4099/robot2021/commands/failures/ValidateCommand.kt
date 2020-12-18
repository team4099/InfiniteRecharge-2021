package com.team4099.robot2021.commands.failures

import edu.wpi.first.wpilibj2.command.CommandBase

class ValidateCommand(val condition : () -> Boolean) : CommandBase() {
  init {

  }
}
