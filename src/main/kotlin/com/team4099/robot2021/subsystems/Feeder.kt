package com.team4099.robot2021.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.Constants.Feeder.FLOOR_CURRENT_LIMIT
import com.team4099.robot2021.config.Constants.Feeder.VERTICAL_CURRENT_LIMIT
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Feeder : SubsystemBase() {

  /** An enum representing the state of the feeder floorMotor power, verticalMotor power */
  enum class FeederState(val floorMotorPower: Double, val verticalMotorPower: Double) {
    FORWARD_ALL(Constants.Feeder.FEEDER_POWER, -Constants.Feeder.FEEDER_POWER),
    FORWARD_FLOOR(Constants.Feeder.FEEDER_POWER, 0.0),
    BACKWARD(-Constants.Feeder.FEEDER_POWER, +Constants.Feeder.FEEDER_POWER),
    NEUTRAL(0.0, 0.0),
    SHOOT(Constants.Feeder.FEEDER_POWER, -Constants.Feeder.FAST_FEEDER_POWER)
  }

  // The motor for the floor of the feeder (the spinny wheel at the bottom)
  private val floorMotor =
      CANSparkMax(Constants.Feeder.FLOOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless)

  // The motor for vertical part of the feeder (the poly cord)
  private val verticalMotor =
      CANSparkMax(Constants.Feeder.VERTICAL_ID, CANSparkMaxLowLevel.MotorType.kBrushless)

  // The DIO pin state of the top Beam Break
  private val topBeamDIO = DigitalInput(Constants.Feeder.TOP_DIO_PIN)

  // The DIO pin state of the bottom Beam Break
  private val bottomBeamDIO = DigitalInput(Constants.Feeder.BOTTOM_DIO_PIN)

  /**
   * Returns the DIO pin in Constants state. If there is no current going through the pin, the
   * output will be true.
   */
  val topBeamBroken: Boolean
    get() = !topBeamDIO.get()
  val bottomBeamBroken: Boolean
    get() = !bottomBeamDIO.get()

  /**
   * Interacts with feeder State
   * @param state Feeder State
   * @return None
   */
  var feederState = FeederState.NEUTRAL
    set(value) {
      field = value
      floorMotor.set(feederState.floorMotorPower)
      verticalMotor.set(feederState.verticalMotorPower)
    }

  var ballCount: Int = 0
  private var bottomLastStage: Boolean = bottomBeamBroken
  private var topLastStage: Boolean = topBeamBroken
  override fun periodic() {
    if (bottomLastStage != bottomBeamBroken && !bottomBeamBroken) {
      if (floorMotor.appliedOutput > 0) {
        ballCount++
      } else if (floorMotor.appliedOutput < 0) {
        ballCount--
      }
    }
    if (topLastStage != topBeamBroken && !topBeamBroken && verticalMotor.appliedOutput > 0) {
      ballCount--
    }
    bottomLastStage = bottomBeamBroken
    topLastStage = topBeamBroken
  }

  init {
    Logger.addSource(Constants.Feeder.TAB, "Feeder State") { feederState.toString() }

    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Power") { floorMotor.appliedOutput }
    /*Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Stator Current") {
      floorMotor.statorCurrent
    }*/
    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Supply Current") {
      floorMotor.outputCurrent
    }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Voltage") { floorMotor.busVoltage }

    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Power") {
      verticalMotor.appliedOutput
    }
    /*Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Stator Current") {
      verticalMotor.statorCurrent
    }*/
    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Supply Current") {
      verticalMotor.outputCurrent
    }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Voltage") {
      verticalMotor.busVoltage
    }

    Logger.addSource(Constants.Feeder.TAB, "Feeder Top Beam DIO Broken") { topBeamBroken }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Bottom Beam DIO Broken") { bottomBeamBroken }

    floorMotor.restoreFactoryDefaults()
    verticalMotor.restoreFactoryDefaults()

    floorMotor.enableVoltageCompensation(10.0)
    verticalMotor.enableVoltageCompensation(10.0)

    floorMotor.setSmartCurrentLimit(FLOOR_CURRENT_LIMIT)
    verticalMotor.setSmartCurrentLimit(VERTICAL_CURRENT_LIMIT)

    floorMotor.burnFlash()
    verticalMotor.burnFlash()
  }
}
