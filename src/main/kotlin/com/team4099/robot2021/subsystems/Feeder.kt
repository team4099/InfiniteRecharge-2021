package com.team4099.robot2021.config.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.commands.feeder.FeederCommand
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Feeder : SubsystemBase() {

  /**
   * An enum representing the state of the feeder
   * floorMotor power, verticalMotor power
   */

  enum class FeederState(val floorMotorPower: Double, val verticalMotorPower: Double) {
    FORWARD_ALL(Constants.Feeder.FEEDER_POWER, Constants.Feeder.FEEDER_POWER),
    FORWARD_FLOOR(Constants.Feeder.FEEDER_POWER, 0.0),
    BACKWARD(-Constants.Feeder.FEEDER_POWER, -Constants.Feeder.FEEDER_POWER),
    NEUTRAL(0.0, 0.0)
  }

  // The motor for the floor of the feeder (the spinny wheel at the bottom)
  private val floorMotor = TalonSRX(Constants.Feeder.FLOOR_ID)

  // The motor for vertical part of the feeder (the poly cord)
  private val verticalMotor = TalonSRX(Constants.Feeder.VERTICAL_ID)


  //The DIO pin state of the top Beam Break
  private val topBeamDIO = DigitalInput(Constants.Feeder.TOP_DIO_PIN);

  //The DIO pin state of the bottom Beam Break
  private val bottomBeamDIO = DigitalInput(Constants.Feeder.BOTTOM_DIO_PIN);

  /**
   * Returns the DIO pin in Constants state.
   * If there is no current going through the pin,
   * the output will be true.
   **/

  val topBeamBroken: Boolean
    get() = !topBeamDIO.get();
  val bottomBeamBroken: Boolean
    get() = !bottomBeamDIO.get();

  /**
   * Interacts with feeder State
   * @param state Feeder State
   * @return None
   **/

  var feederState = FeederState.NEUTRAL
    set(value) {
      field = value
      floorMotor.set(ControlMode.PercentOutput, feederState.floorMotorPower)
      verticalMotor.set(ControlMode.PercentOutput, feederState.verticalMotorPower)
    }
}
