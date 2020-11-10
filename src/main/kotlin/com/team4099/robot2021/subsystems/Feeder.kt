package com.team4099.robot2021.config.subsystems

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

    var feederState = FeederState.NEUTRAL
        set(value) {
            when(state) {
                state.FORWARD -> {
                    // TODO
                }
                state.BACKWARD -> {
                    // TODO
                }
                state.NUETRAL -> {
                    // TODO
                }
            }
        }

    
}
