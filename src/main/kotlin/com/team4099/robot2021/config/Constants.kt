package com.team4099.robot2021.config

import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.perMinute
import com.team4099.lib.units.derived.Angle
import edu.wpi.first.wpilibj.DoubleSolenoid
import java.lang.Math.PI

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

  object Feeder {
    const val FLOOR_ID = 0
    const val VERTICAL_ID = 0
    const val FEEDER_POWER = 1.0;

    const val TOP_DIO_PIN = 4;
    const val BOTTOM_DIO_PIN = 9;
    const val TAB = "Feeder";
  }

  object Intake {
    const val INTAKE_MOTOR = 0 //temp
    const val ARM_SOLENOID_PORT_1 = 0 //temp
    const val ARM_SOLENOID_PORT_2 = 1 //temp
    const val TAB = "Intake"

    enum class IntakeState(val speed: Double) {
      DEFAULT(0.0),
      IN(1.0),
      OUT(-1.0)
    }

    enum class ArmPosition(val position: DoubleSolenoid.Value?) {
      OUT(DoubleSolenoid.Value.kReverse),
      IN(DoubleSolenoid.Value.kForward),
      DEFAULT(DoubleSolenoid.Value.kOff);
    }

  }
  object Shooter {
    const val SHOOTER_MOTOR_ID = 0
    const val SHOOTER_FOLLOWER_ID = 0

    val TARGET_VELOCITY = 0.rotations.perMinute
    val VELOCITY_TOLERANCE = 60.rotations.perMinute

    const val SHOOTER_KS = 0.0
    const val SHOOTER_KV = 0.0

    const val SHOOTER_KP = 0.0
    const val SHOOTER_KI = 0.0
    const val SHOOTER_KD = 0.0
  }

  object Vision {
    const val DRIVER_PIPELINE_ID = 1
    const val TARGETING_PIPELINE_ID = 0
    val TARGET_HEIGHT = 98.25.inches
    val CAMERA_HEIGHT = 35.0.inches
    val CAMERA_ANGLE = 24.0.degrees

    val MAX_DIST_ERROR = 0.1.inches
    val MAX_ANGLE_ERROR = 1.0.degrees
  }

}
