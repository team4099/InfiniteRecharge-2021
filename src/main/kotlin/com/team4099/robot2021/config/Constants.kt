package com.team4099.robot2021.config

import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.perSecond
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

    enum class IntakeState (val speed: Double){
      DEFAULT(0.0),
      IN(1.0),
      OUT(-1.0)
    }

    enum class ArmPosition (val position: DoubleSolenoid.Value?){
      OUT(DoubleSolenoid.Value.kReverse),
      IN(DoubleSolenoid.Value.kForward),
      DEFAULT(DoubleSolenoid.Value.kOff);
    }
  }
  
  object Drivetrain {
    const val TICKS = 4096

    const val FRONT_LEFT_SPEED_ID = 2
    const val FRONT_LEFT_DIRECTION_ID = 3
    const val FRONT_LEFT_CANCODER_ID = 0 // TODO: Change this value

    const val FRONT_RIGHT_SPEED_ID = 4
    const val FRONT_RIGHT_DIRECTION_ID = 5
    const val FRONT_RIGHT_CANCODER_ID = 1 // TODO: Change this value

    const val BACK_LEFT_SPEED_ID = 6
    const val BACK_LEFT_DIRECTION_ID = 7
    const val BACK_LEFT_CANCODER_ID = 2 // TODO: Change this value

    const val BACK_RIGHT_SPEED_ID = 8
    const val BACK_RIGHT_DIRECTION_ID = 9
    const val BACK_RIGHT_CANCODER_ID = 3 // TODO: Change this value

    const val WHEEL_COUNT = 4
    val DRIVETRAIN_LENGTH = 29.0.inches
    val DRIVETRAIN_WIDTH = 29.0.inches

    const val DEFAULT_ABSOLUTE_AZIMUTH_OFFSET = 200
    val DRIVE_SETPOINT_MAX = 15.feet.perSecond
    val TURN_SETPOINT_MAX = 90.degrees.perSecond // TODO: Make sure this value is something good

    const val GYRO_RATE_COEFFICIENT = 0.0 // TODO: Change this value

    const val MAX_VEL_METERS_PER_SEC = 4.0
    const val SLOW_VEL_METERS_PER_SEC = 0.66
    const val MAX_ACCEL_METERS_PER_SEC_SQ = 2.0
    const val SLOW_ACCEL_METERS_PER_SEC_SQ = 2.0

    const val CENTRIPETAL_ACCEL_METERS_PER_SEC_SQ = 1.0

    object Gains {
      const val RAMSETE_B = 2.0
      const val RAMSETE_ZETA = 0.7
    }
  }

}

