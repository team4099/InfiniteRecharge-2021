package com.team4099.robot2021.config

import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds

object Constants {
  object Universal {
    const val CTRE_CONFIG_TIMEOUT = 0
    const val EPSILON = 1E-9
  }

  object Joysticks {
    const val DRIVER_PORT = 0
    const val SHOTGUN_PORT = 1

    const val QUICK_TURN_THROTTLE_TOLERANCE = 0.1
    const val THROTTLE_DEADBAND = 0.04
    const val TURN_DEADBAND = 0.035
  }

  object Climber {
    val CLIMBER_SPARKMAX_ID = 6
    val CLIMBER_SENSOR_LINEARMECH_RATIO = 6.0
    val CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER = .0508.meters //diameter: .0508 meters = 2 in
    val CLIMBER_SOLENOID_ACTUATIONSTATE = 0 //this is prolly not the right name for this parameter
    val CLIMBER_CLIMBERPIDCONTROLLER_P = 0.1
    val CLIMBER_CLIMBERPIDCONTROLLER_I = 0.1
    val CLIMBER_CLIMBERPIDCONTROLLER_D = 0.1
    val CLIMBER_PID_SLOTID_SMARTMOTIIONVEL = 0
    val CLIMBER_PID_SLOTID_SMARTMOTIIONACC = 0
    val BRAKE_RELEASE_TIMEOUT = 0.1.seconds
  }

  enum class ClimberPosition(val length: Length) {
    LOW(0.meters),
    HIGH(1.0414.meters) //Climber fulled extended: 1.0414 meters = 41 in
  }

}
