package com.team4099.robot2021.commands.shooter

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.CommandBase
import kotlin.math.sign

class VisionCommand : CommandBase() {

  init {
    addRequirements(Vision)
    addRequirements(Drivetrain)

  }

  override fun initialize() {
    Vision.pipeline = Constants.Vision.TARGETING_PIPELINE_ID
    Vision.visionPIDcontroller.reset(0.0);
    // Vision.camera.setDriverMode(false)
    Logger.addEvent("VisionCommand", "Started vision command")
  }

  override fun execute() {
    // var yaw = Vision.yaw.inDegrees
    // could just choose one here instead of using yawToUse, but we would need an onTarget for both
    // cameras
    /*var yaw = when(Pair(Vision.hasTargets, Vision.hasFarTargets)){
      Pair(true,true) -> 0.0 //yaw from camera with higher area
      Pair(true,false) -> Vision.closeYaw
      Pair(false,true) -> Vision.farYaw
      else -> 0.0
    }*/

    if (!Vision.hasCloseTargets && !Vision.hasFarTargets) {
      Vision.steeringAdjust = 0.0
    } else {
      Vision.steeringAdjust = Vision.visionPIDcontroller.calculate(Vision.yawToUse.inDegrees, 0.0)
      Vision.steeringAdjust += -sign(Vision.yawToUse.inDegrees) * Constants.Vision.MIN_TURN_COMMAND
      // TODO: implement when shooter or drivetrain exists in master
      Drivetrain.set(Vision.steeringAdjust.degrees.perSecond, Pair(0.0.meters.perSecond, 0.0.meters.perSecond))
    }

  }

  override fun isFinished(): Boolean {
    return false
  }
}
