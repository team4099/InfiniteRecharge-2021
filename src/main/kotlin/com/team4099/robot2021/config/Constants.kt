package com.team4099.robot2021.config

import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute

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

  object Shooter {
    const val SHOOTER_MOTOR_ID = 0

    val TARGET_VELOCITY = 0.rotations.perMinute
    val VELOCITY_OFFSET = 60.rotations.perMinute

    const val SHOOTER_KS = 0.0
    const val SHOOTER_KV = 0.0
  }
}
