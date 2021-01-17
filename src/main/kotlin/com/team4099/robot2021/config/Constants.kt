package com.team4099.robot2021.config

import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.perSecond

object Constants {
  object Universal {
    const val CTRE_CONFIG_TIMEOUT = 0
    const val EPSILON = 1E-9
  }

  object Tuning {
    const val TUNING_TOGGLE_PIN = 0
    val ROBOT_ID_PINS = 1..2

    enum class RobotName {
      COMPETITION,
      PRACTICE,
      MULE
    }

    val ROBOT_ID_MAP = mapOf<Int, RobotName>(
      0 to RobotName.COMPETITION,
      1 to RobotName.PRACTICE,
      2 to RobotName.MULE
    )
  }

  object Joysticks {
    const val DRIVER_PORT = 0
    const val SHOTGUN_PORT = 1

    const val QUICK_TURN_THROTTLE_TOLERANCE = 0.1
    const val THROTTLE_DEADBAND = 0.04
    const val TURN_DEADBAND = 0.035
  }

  object Climber {
    val CLIMBER_R_ARM_SPARKMAX_ID = 6 //right arm
    val CLIMBER_L_ARM_SPARKMAX_ID = 5 //left arm
    val CLIMBER_SENSOR_LINEARMECH_GEARRATIO = 8.4
    val CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER = .0508.meters //diameter: .0508 meters = 2 in
    val CLIMBER_SOLENOID_ACTUATIONSTATE = 0 //this is prolly not the right name for this parameter
    val CLIMBER_P = 0.1
    val CLIMBER_I = 0.1
    val CLIMBER_D = 0.1
    val CLIMBER_SPARKMAX_VEL = 0.5.meters.perSecond
    val CLIMBER_SPARKMAX_ACC = 0.5.meters.perSecond.perSecond
    val BRAKE_RELEASE_TIMEOUT = 0.1.seconds
    val TAB = "Climber"
  }

  enum class ClimberPosition(val length: Length) {
    LOW(0.meters),
    HIGH(1.0414.meters) //Climber fulled extended: 1.0414 meters = 41 in
  }

}
