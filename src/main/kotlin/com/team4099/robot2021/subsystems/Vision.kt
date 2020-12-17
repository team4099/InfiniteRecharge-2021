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

  enum class DistanceState() {
    LINE, NEAR, MID, FAR
  }

  init {
    Logger.addSource("Vision","Pipeline"){ pipelineEntry }
    Logger.addSource("Vision","Distance (inches)"){ distance }
  }

  var steeringAdjust = 0.0
  var onTarget = false
    get() = tx.absoluteValue < Constants.Vision.MAX_ANGLE_ERROR

  private val pipelineEntry: NetworkTableEntry = table.getEntry("pipeline")

  var pipeline = Constants.Vision.DRIVER_PIPELINE_ID
    set(value) {
      pipelineEntry.setNumber(value)
      field = value
    }

  private val distance: Length
    get() = (Constants.Vision.TARGET_HEIGHT - Constants.Vision.CAMERA_HEIGHT) / (Constants.Vision.CAMERA_ANGLE + ty).tan


  val currentDistance: DistanceState
    get() = when(distance.value) {
      in 0.0..100.0 -> DistanceState.LINE
      in 101.0..129.0 -> DistanceState.NEAR
      in 130.0..249.0 -> DistanceState.MID
      else -> DistanceState.FAR
    }

}
