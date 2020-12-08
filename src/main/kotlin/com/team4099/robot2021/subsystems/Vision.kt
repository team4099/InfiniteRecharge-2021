package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.tan
import com.team4099.lib.units.inDegreesPerSecond
import com.team4099.lib.units.inRotationsPerMinute
import com.team4099.robot2021.config.Constants
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.ProfiledPIDController
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import java.lang.Math.tan

object Vision : SubsystemBase() {
  private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")

  val tx get() = table.getEntry("tx").getDouble(0.0).degrees
  private val ty get() = table.getEntry("ty").getDouble(0.0).degrees
  val tv get() = table.getEntry("tv").getDouble(0.0)
  private val ta get() = table.getEntry("ta").getDouble(0.0)

  /*val onTargetController = PIDController(
    Constants.Vision.ON_TARGET_KP,
    Constants.Vision.ON_TARGET_KI,
    Constants.Vision.ON_TARGET_KD
  )
  val offTargetController = PIDController(
    Constants.Vision.OFF_TARGET_KP,
    Constants.Vision.OFF_TARGET_KI,
    Constants.Vision.OFF_TARGET_KD
  )*/
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
    Logger.addSource("Vision","Pipeline"){ pipelineEntry }
    Logger.addSource("Vision","Distance (inches)"){ distance }
  }

  var steeringAdjust = 0.0
  var onTarget = false
    get() = /*tv != 0.0 &&*/ tx.absoluteValue < Constants.Vision.MAX_ANGLE_ERROR;

  private val pipelineEntry: NetworkTableEntry = table.getEntry("pipeline")

  var pipeline = Constants.Vision.DRIVER_PIPELINE_ID
    set(value) {
      pipelineEntry.setNumber(value)
      field = value
    }

  val distance: Length
    get() {
      return (Constants.Vision.TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) /
        (Constants.Vision.CAMERA_ANGLE + ty).tan
    }
}
