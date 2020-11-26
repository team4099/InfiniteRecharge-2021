package com.team4099.robot2021.commands.feeder
import com.team4099.robot2021.config.subsystems.Feeder
import edu.wpi.first.wpilibj2.command.CommandBase

class BeamBreak(): CommandBase(){
  init{
    addRequirements(Feeder)
  }
  override fun execute() {
    Feeder.feederState = when{
      !Feeder.bottomBeamBroken && Feeder.topBeamBroken -> Feeder.FeederState.FORWARD_ALL;
      Feeder.bottomBeamBroken && !Feeder.topBeamBroken -> Feeder.FeederState.FORWARD_FLOOR;
      else -> Feeder.FeederState.NEUTRAL;
    }
  }
}
