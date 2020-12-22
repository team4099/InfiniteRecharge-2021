package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.derived.tan
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.CommandBase
import kotlin.math.sign

class VisionCommand : CommandBase(){

  val visionPIDcontroller = ProfiledPIDController(
    Constants.Vision.TurnGains.KP,
    Constants.Vision.TurnGains.KI,
    Constants.Vision.TurnGains.KD,
    TrapezoidProfile.Constraints(
      Constants.Vision.TurnGains.MAX_VELOCITY.value,
      Constants.Vision.TurnGains.MAX_ACCEL.value
    )
  )

  init {
    addRequirements(Vision)
  }

  override fun initialize() {
    //Vision.camera.setPipelineIndex(TARGETING_PIPELINE_ID)
    //Vision.camera.setDriverMode(false)
    Vision.pipeline = Constants.Vision.TARGETING_PIPELINE_ID
    Logger.addEvent("VisionCommand","Started vision command")
  }

  override fun execute() {
    if(Vision.tv == 0.0) {
      Vision.steeringAdjust = 0.0
    }
    else {
      Vision.steeringAdjust = visionPIDcontroller.calculate(Vision.tx.value, 0.0)
      Vision.steeringAdjust += sign(Vision.tx.value) * Constants.Vision.MIN_TURN_COMMAND
      // TODO: implement when drivetrain exists in master
      // Drivetrain.set(Vision.steeringAdjust, Pair(0.0,0.0))
    }
  }

  override fun isFinished() : Boolean {
    return false
  }

}
