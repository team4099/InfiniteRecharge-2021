package com.team4099.robot2021.config

import com.team4099.lib.units.derived.Angle
import edu.wpi.first.wpilibj.DoubleSolenoid

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

  object Intake {
    const val INTAKE_MOTOR = 0 //temp
    const val ARM_SOLENOID_PORT_1 = 0 //temp
    const val ARM_SOLENOID_PORT_2 = 1 //temp
    const val TAB = "Intake"

    enum class IntakeState (val speed: Double){
      DEFAULT(0.0),
      IN(1.0),
      OUT(-1.0)
    }

    enum class ArmPosition (val position: DoubleSolenoid.Value?){
      OUT(DoubleSolenoid.Value.kReverse),
      IN(DoubleSolenoid.Value.kForward),
      DEFAULT(DoubleSolenoid.Value.kOff)
    }

  }

}
