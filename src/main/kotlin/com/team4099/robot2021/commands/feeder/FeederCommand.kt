package com.team4099.robot2021.commands.feeder
import com.team4099.lib.logging.Logger
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Feeder.feederState
import edu.wpi.first.wpilibj2.command.CommandBase


/**
 * Feeder command
 *
 * @property dir Sets the direction the motors rotate
 * @constructor Create empty Feeder command
 */
class FeederCommand(var dir: Feeder.FeederState): CommandBase(){
  init{
    addRequirements(Feeder)
    
  }
  override fun initialize() {

    Feeder.feederState = dir;
    Logger.addEvent("Feeder", "Feeder state: $feederState")
  }
}
