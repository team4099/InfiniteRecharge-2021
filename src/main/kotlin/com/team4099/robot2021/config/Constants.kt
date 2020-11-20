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

  object Joysticks {
    const val DRIVER_PORT = 0
    const val SHOTGUN_PORT = 1

    const val QUICK_TURN_THROTTLE_TOLERANCE = 0.1
    const val THROTTLE_DEADBAND = 0.04
    const val TURN_DEADBAND = 0.035
  }

  object LED {
    const val PORT = 0
    const val LED_COUNT = 50
    enum class Color(var h: Int, var s: Int, var v: Int) {
      // Pink
      FIRING(227, 117, 128),

      // Turquoise
      READY_FIRE(124, 255, 118),

      // Orange
      INTAKE_EMPTY(18, 184, 128),

      // Magenta
      ONE_BALL(209, 242, 128),

      // Sky blue
      TWO_BALL(145, 242, 128),

      // Tan
      THREE_BALL(17, 122, 128),

      // Green
      FOUR_BALL(74, 207, 128),

      // Brown
      CLIMB(34, 255, 45),

      // Rainbow is for climbing and when 5 balls and to appease the team
      RAINBOW(0, 255, 128),
    }
  }
}
