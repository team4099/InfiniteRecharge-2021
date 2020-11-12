package com.team4099.robot2021.config.subsystems

import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Feeder:SubsystemBase(){

    /**
     * The state of the feeder 
    */
    enum class FeederState {
        FORWARD, BACKWARD, NEUTRAL
    }
    /** 
     * Interacts with feeder State 
     * @param state Feeder State
     * @return None
    **/
	
	private val floorMotor = TalonSRX(Constants.Feeder.FLOOR_ID)
	private val verticalMotor = TalonSRX(Constants.Feeder.VERTICAL_ID)
	
    var feederState = FeederState.NEUTRAL
        set(value) {
            field = value
			when(value) {
                FeederState.FORWARD -> {
                    floorMotor.set(ControlMode.PercentOutput, Constants.Feeder.FEEDER_POWER)
					verticalMotor.set(ControlMode.PercentOutput, Constants.Feeder.FEEDER_POWER)
                }
                FeederState.NEUTRAL -> {
                    floorMotor.set(ControlMode.PercentOutput, 0)
					verticalMotor.set(ControlMode.PercentOutput, 0)
                }
                FeederState.BACKWARD -> {
                    floorMotor.set(ControlMode.PercentOutput, -Constants.Feeder.FEEDER_POWER)
					verticalMotor.set(ControlMode.PercentOutput, -Constants.Feeder.FEEDER_POWER)
                }
            }
        }

    
}
