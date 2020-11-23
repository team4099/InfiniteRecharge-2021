package com.team4099.robot2021.config.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.commands.feeder.FeederCommand
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Feeder:SubsystemBase(){
  /**
   * The state of the feeder
   */
  enum class FeederState {
    FORWARD, BACKWARD, NEUTRAL
  }



  private val floorMotor = TalonSRX(Constants.Feeder.FLOOR_ID)
  private val verticalMotor = TalonSRX(Constants.Feeder.VERTICAL_ID)

  /**
   * Returns the DIO pin in Constants.kt state.
   * If there is no current going through the pin,
   * the output will be true.
   **/

  val topBeamState: Boolean
    get() = DigitalInput(Constants.Feeder.DIO_1).get();
  val bottomBeamState: Boolean
    get() = DigitalInput(Constants.Feeder.DIO_2).get();

  /**
   * Interacts with feeder State
   * @param state Feeder State
   * @return None
   **/

  var feederState = FeederState.NEUTRAL
    set(value) {
      field = value
      when(value) {
        FeederState.FORWARD -> {
          floorMotor.set(ControlMode.PercentOutput, Constants.Feeder.FEEDER_POWER.toDouble())
          verticalMotor.set(ControlMode.PercentOutput, Constants.Feeder.FEEDER_POWER.toDouble())
        }
        FeederState.NEUTRAL -> {
          floorMotor.set(ControlMode.PercentOutput, 0.0)
          verticalMotor.set(ControlMode.PercentOutput, 0.0)
        }
        FeederState.BACKWARD -> {
          floorMotor.set(ControlMode.PercentOutput, (-Constants.Feeder.FEEDER_POWER).toDouble())
          verticalMotor.set(ControlMode.PercentOutput, (-Constants.Feeder.FEEDER_POWER).toDouble())
        }
      }
    }
}
