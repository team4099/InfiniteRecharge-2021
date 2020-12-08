package com.team4099.robot2021.commands

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.derived.tan
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj2.command.CommandBase
import kotlin.math.sign

class VisionCommand : CommandBase(){
  init {
    addRequirements(Vision)
  }

  override fun initialize() {
    Vision.pipeline = Constants.Vision.TARGETING_PIPELINE_ID
    Logger.addEvent("VisionCommand","Started vision command")
  }
  override fun execute() {
    /*Vision.steeringAdjust = when{
      Vision.onTarget -> Vision.onTargetController.calculate(Vision.tx.inDegrees, 0.0)
      else -> Vision.offTargetController.calculate(Vision.tx.inDegrees, 0.0)
    }*/
    if(Vision.tv != 0.0){
      Vision.steeringAdjust = Vision.visionPIDcontroller.calculate(Vision.tx.value, 0.0)
      Vision.steeringAdjust += sign(Vision.tx.value) * Constants.Vision.MIN_TURN_COMMAND
    }

  }
  override fun isFinished() : Boolean {
    return false
  }

}
