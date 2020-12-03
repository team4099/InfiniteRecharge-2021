package com.team4099.robot2021.commands.feeder
import com.team4099.robot2021.config.subsystems.Feeder
import edu.wpi.first.wpilibj2.command.CommandBase

class FeederCommand(var dir: Feeder.FeederState): CommandBase(){
  init{
    addRequirements(Feeder)
    
  }
  override fun initialize() {
    Feeder.feederState = dir;
    Logger.addEvent("Feeder", "Feeder state: $feederState")
  }
}
