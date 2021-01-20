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
    const val STATUS_LENGTH = 0
    enum class Status(var h: Int, var s: Int, var v: Int) {
      //Green
      VISION_LOCK(52, 255, 255),

      // Blue
      SHOOTER_SPEED(120, 255, 255),

      // White
      INTAKE_EMPTY(0, 0, 255),

      // Magenta
      ONE_TWO_BALL(154, 255, 255),

      // Sky blue
      THREE_FOUR_BALL(90, 255, 255),

      //Weird yellow green
      DEFAULT(36, 255, 255)
    }

    enum class Health(var h: Int, var s: Int, var v: Int) {
      VISION(0, 255, 255),
      BEAM_BREAK(60, 255, 255),
      OTHER(17, 255, 255),
      DEFAULT(36, 255, 255)
    }
  }
}
