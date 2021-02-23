package com.team4099.robot2021.auto

import com.team4099.lib.hal.Clock
import com.team4099.lib.units.base.inMeters
import com.team4099.lib.units.base.inSeconds
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.inRadians
import com.team4099.lib.units.derived.inVolts
import com.team4099.lib.units.inMetersPerSecond
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj2.command.CommandBase

class DriveCharacterizeCommand : CommandBase() {
  private val autoSpeedEntry: NetworkTableEntry =
      NetworkTableInstance.getDefault().getEntry("/robot/autospeed")
  private val telemetryEntry: NetworkTableEntry =
      NetworkTableInstance.getDefault().getEntry("/robot/telemetry")
  private val rotateEntry: NetworkTableEntry =
      NetworkTableInstance.getDefault().getEntry("/robot/rotate")
  private var numberArray = arrayOfNulls<Number>(10)

  override fun isFinished(): Boolean {
    return false
  }

  override fun initialize() {
    // Set the update rate instead of using flush because of a ntcore bug
    // -> probably don't want to do this on a robot in competition
    NetworkTableInstance.getDefault().setUpdateRate(0.010)
    Drivetrain.zeroSensors()
  }

  override fun execute() {
    val autoSpeed = autoSpeedEntry.getDouble(0.0)

    Drivetrain.wheels.forEach { it.setOpenLoop(0.degrees, autoSpeed) }

    numberArray[0] = Clock.fpgaTime.inSeconds
    numberArray[1] = RobotController.getBatteryVoltage()
    numberArray[2] = autoSpeed
    numberArray[3] = Drivetrain.wheels[0].driveOutputVoltage.inVolts
    numberArray[4] = Drivetrain.wheels[1].driveOutputVoltage.inVolts
    numberArray[5] = Drivetrain.wheels[0].driveDistance.inMeters
    numberArray[6] = Drivetrain.wheels[1].driveDistance.inMeters
    numberArray[7] = Drivetrain.wheels[0].driveVelocity.inMetersPerSecond
    numberArray[8] = Drivetrain.wheels[1].driveVelocity.inMetersPerSecond
    numberArray[9] = Drivetrain.gyroAngle.inRadians

    telemetryEntry.setNumberArray(numberArray)
  }

  override fun end(interrupted: Boolean) {
    NetworkTableInstance.getDefault().setUpdateRate(0.1)
  }
}
