package com.team4099.robot2021.auto

import com.team4099.lib.hal.Clock
import com.team4099.lib.units.base.inSeconds
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandBase
import java.lang.Math.toRadians

class DriveCharacterizeCommand : CommandBase() {
  private val autoSpeedEntry: NetworkTableEntry = NetworkTableInstance.getDefault().getEntry("/robot/autospeed")
  private val telemetryEntry: NetworkTableEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry")
  private val rotateEntry: NetworkTableEntry = NetworkTableInstance.getDefault().getEntry("/robot/rotate")
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


    numberArray[0] = Clock.fpgaTime.inSeconds
    numberArray[1] = RobotController.getBatteryVoltage()
    numberArray[2] = autoSpeed
    // numberArray[3] = Drivetrain.leftMasterOutputVoltage
    // numberArray[4] = Drivetrain.rightMasterOutputVoltage
    // numberArray[5] = Drivetrain.leftDistanceMeters
    // numberArray[6] = Drivetrain.rightDistanceMeters
    // numberArray[7] = Drivetrain.leftVelocityMetersPerSec
    // numberArray[8] = Drivetrain.rightVelocityMetersPerSec
    // numberArray[9] = toRadians(Drivetrain.gyroAngle)

    telemetryEntry.setNumberArray(numberArray)
  }

  override fun end(interrupted: Boolean) {
    NetworkTableInstance.getDefault().setUpdateRate(0.1)
  }
}
/*
@Suppress("MagicNumber")
class DriveCharacterizeAction : Action {
    private val autoSpeedEntry: NetworkTableEntry = NetworkTableInstance.getDefault().getEntry("/robot/autospeed")
    private val telemetryEntry: NetworkTableEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry")
    private val rotateEntry: NetworkTableEntry = NetworkTableInstance.getDefault().getEntry("/robot/rotate")
    private var numberArray = arrayOfNulls<Number>(10)

    override fun isFinished(timestamp: Double): Boolean {
        return false
    }

    override fun onStart(timestamp: Double) {
        // Set the update rate instead of using flush because of a ntcore bug
        // -> probably don't want to do this on a robot in competition
        NetworkTableInstance.getDefault().setUpdateRate(0.010)
        Drive.zeroSensors()
    }

    override fun onLoop(timestamp: Double, dT: Double) {
        val autoSpeed = autoSpeedEntry.getDouble(0.0)
        Drive.setOpenLoop(DriveSignal(
            (if (rotateEntry.getBoolean(false)) -1 else 1) * autoSpeed,
            autoSpeed
        ))

        numberArray[0] = timestamp
        numberArray[1] = RobotController.getBatteryVoltage()
        numberArray[2] = autoSpeed
        numberArray[3] = Drive.leftMasterOutputVoltage
        numberArray[4] = Drive.rightMasterOutputVoltage
        numberArray[5] = Drive.leftDistanceMeters
        numberArray[6] = Drive.rightDistanceMeters
        numberArray[7] = Drive.leftVelocityMetersPerSec
        numberArray[8] = Drive.rightVelocityMetersPerSec
        numberArray[9] = toRadians(Drive.angle)

        telemetryEntry.setNumberArray(numberArray)
    }

    override fun onStop(timestamp: Double) {
        Drive.setOpenLoop(DriveSignal.NEUTRAL)
        NetworkTableInstance.getDefault().setUpdateRate(0.1)
    }
}

 */
