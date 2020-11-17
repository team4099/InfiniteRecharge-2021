package com.team4099.robot2021.config

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
  
  object Drivetrain {
    const val FRONT_LEFT_SPEED_ID = 2
    const val FRONT_LEFT_DIRECTION_ID = 3

    const val FRONT_RIGHT_SPEED_ID = 4
    const val FRONT_RIGHT_DIRECTION_ID = 5

    const val BACK_LEFT_SPEED_ID = 6
    const val BACK_LEFT_DIRECTION_ID = 7

    const val BACK_RIGHT_SPEED_ID = 8
    const val BACK_RIGHT_DIRECTION_ID = 9

    const val WHEEL_COUNT = 4
    const val DRIVETRAIN_LENGTH = 29.0
    const val DRIVETRAIN_WIDTH = 29.0

    const val DEFAULT_ABSOLUTE_AZIMUTH_OFFSET = 200
    const val DRIVE_SETPOINT_MAX = 10000




  }

  object Joysticks {
    const val DRIVER_PORT = 0
    const val SHOTGUN_PORT = 1

    const val QUICK_TURN_THROTTLE_TOLERANCE = 0.1
    const val THROTTLE_DEADBAND = 0.04
    const val TURN_DEADBAND = 0.035
  }
}
