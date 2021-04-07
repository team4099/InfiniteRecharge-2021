package com.team4099.robot2021.commands.shooter

import com.team4099.lib.around
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inDegrees
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj.MedianFilter
import edu.wpi.first.wpilibj2.command.CommandBase
import kotlin.math.sign

class VisionCommand : CommandBase() {

  val filter = MedianFilter(20)
  var medianYaw = 10000.0

  var contCloseIterations = 0

  init {
    addRequirements(Vision)
    addRequirements(Drivetrain)
    Logger.addSource("Vision", "Median Yaw") { medianYaw }
  }

  override fun initialize() {
    Vision.pipeline = Constants.Vision.TARGETING_PIPELINE_ID
    Vision.visionPIDcontroller.reset(0.0)
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
      Drivetrain.setOpenLoop(
          -Vision.steeringAdjust.degrees.perSecond, Pair(0.0.meters.perSecond, 0.0.meters.perSecond))
    }

    if (Vision.yawToUse.inDegrees.around(0.0, Constants.Vision.MAX_ANGLE_ERROR.inDegrees)) {
      contCloseIterations++
    } else {
      contCloseIterations = 0
    }
    medianYaw = filter.calculate(Vision.yawToUse.inDegrees)
  }

  override fun isFinished(): Boolean {
    //    return medianYaw.around(0.0, Constants.Vision.MAX_ANGLE_ERROR.inDegrees)
    return contCloseIterations > 10
  }

  override fun end(interrupted: Boolean) {
    Drivetrain.setOpenLoop(0.degrees.perSecond, Pair(0.0.meters.perSecond, 0.0.meters.perSecond))
  }
}
