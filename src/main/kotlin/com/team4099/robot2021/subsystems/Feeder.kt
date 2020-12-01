package com.team4099.robot2021.config.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.commands.feeder.FeederCommand
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Feeder:SubsystemBase(){
  init {
    Logger.addSource(Constants.Feeder.TAB, "Feeder State") { feederState }

    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Power") { floorMotor.motorOutputPercent }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Stator Current") { floorMotor.statorCurrent }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Supply Current") { floorMotor.supplyCurrent }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Floor Motor Voltage") { floorMotor.motorOutputVoltage }

    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Power") { verticalMotor.motorOutputPercent }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Stator Current") { verticalMotor.statorCurrent }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Supply Current") { verticalMotor.supplyCurrent }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Vertical Motor Voltage") { verticalMotor.motorOutputVoltage }

    Logger.addSource(Constants.Feeder.TAB, "Feeder Top Beam DIO Broken") { topBeamBroken }
    Logger.addSource(Constants.Feeder.TAB, "Feeder Bottom Beam DIO Broken") { bottomBeamBroken }
  }
    /**
     * An enum representing the state of the feeder
     * floorMotor power, verticalMotor power
    */
    enum class FeederState(val floorMotorPower : Double, val verticalMotorPower : Double) {
        FORWARD_ALL(Constants.Feeder.FEEDER_POWER, Constants.Feeder.FEEDER_POWER),
        FORWARD_FLOOR(Constants.Feeder.FEEDER_POWER, 0.0),
        BACKWARD(-Constants.Feeder.FEEDER_POWER, -Constants.Feeder.FEEDER_POWER),
        NEUTRAL(0.0, 0.0)
    }

    // The motor for the floor of the feeder (the spinny wheel at the bottom)
    private val floorMotor = TalonSRX(Constants.Feeder.FLOOR_ID)

    // The motor for vertical part of the feeder (the poly cord)
    private val verticalMotor = TalonSRX(Constants.Feeder.VERTICAL_ID)

    /**
     * Interacts with feeder State
     * @param state Feeder State
     * @return None
     **/
    var feederState = FeederState.NEUTRAL
      get() {
        return field;
      }
        set(value) {
            field = value
            floorMotor.set(ControlMode.PercentOutput, feederState.floorMotorPower);
            verticalMotor.set(ControlMode.PercentOutput, feederState.verticalMotorPower);
        }

  private val topBeamDIO = DigitalInput(Constants.Feeder.TOP_DIO_PIN);
  private val bottomBeamDIO = DigitalInput(Constants.Feeder.BOTTOM_DIO_PIN);
  /**
   * Returns the DIO pin in Constants.kt state.
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
}
