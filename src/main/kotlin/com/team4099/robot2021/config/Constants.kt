package com.team4099.robot2021.config

import com.revrobotics.SparkMax
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.feet
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.base.seconds
import com.team4099.lib.units.derived.degrees
import com.team4099.lib.units.derived.radians
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.derived.volts
import com.team4099.lib.units.perMinute
import com.team4099.lib.units.perSecond
import edu.wpi.first.wpilibj.DoubleSolenoid
import com.team4099.lib.units.derived.div

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

    val ROBOT_ID_MAP =
        mapOf<Int, RobotName>(
            0 to RobotName.COMPETITION, 1 to RobotName.PRACTICE, 2 to RobotName.MULE)
  }

  object Joysticks {
    const val DRIVER_PORT = 0
    const val SHOTGUN_PORT = 1

    const val THROTTLE_DEADBAND = 0.05
    const val TURN_DEADBAND = 0.05
  }

  object Drivetrain {
    const val TICKS = 4096

    const val FRONT_LEFT_SPEED_ID = 11
    const val FRONT_LEFT_DIRECTION_ID = 21
    const val FRONT_LEFT_CANCODER_ID = 1

    const val FRONT_RIGHT_SPEED_ID = 12
    const val FRONT_RIGHT_DIRECTION_ID = 22
    const val FRONT_RIGHT_CANCODER_ID = 2

    const val BACK_RIGHT_SPEED_ID = 13
    const val BACK_RIGHT_DIRECTION_ID = 23
    const val BACK_RIGHT_CANCODER_ID = 3

    const val BACK_LEFT_SPEED_ID = 14
    const val BACK_LEFT_DIRECTION_ID = 24
    const val BACK_LEFT_CANCODER_ID = 4

    const val WHEEL_COUNT = 4
    val DRIVETRAIN_LENGTH = 22.173.inches
    val DRIVETRAIN_WIDTH = 22.173.inches

    val DRIVE_SETPOINT_MAX = 15.feet.perSecond
    val TURN_SETPOINT_MAX = 90.degrees.perSecond // TODO: Make sure this value is something good

    val DIRECTION_VEL_MAX = 900.degrees.perSecond
    val DIRECTION_ACCEL_MAX = 4500.degrees.perSecond.perSecond

    const val GYRO_RATE_COEFFICIENT = 0.0 // TODO: Change this value

    val MAX_VEL_METERS_PER_SEC = 4.0.meters.perSecond
    val SLOW_VEL_METERS_PER_SEC = 0.66.meters.perSecond
    val MAX_ACCEL_METERS_PER_SEC_SQ = 2.0.meters.perSecond.perSecond
    val SLOW_ACCEL_METERS_PER_SEC_SQ = 2.0.meters.perSecond.perSecond

    val MAX_VEL_ANGULAR_PER_SEC = 1.0.radians.perSecond
    val MAX_ACCEL_ANGULAR_PER_SEC_SQ = 1.0.radians.perSecond.perSecond

    const val CENTRIPETAL_ACCEL_METERS_PER_SEC_SQ = 1.0

    const val ABSOLUTE_GEAR_RATIO = 1.0
    const val DRIVE_SENSOR_GEAR_RATIO = (12.0/21.0)*(15.0/45.0)
    const val DIRECTION_SENSOR_GEAR_RATIO = (12.0/64.0)*(1.0/10.0)

    val ALLOWED_ANGLE_ERROR = 5.degrees
    const val DIRECTION_SMART_CURRENT_LIMIT = 20
    const val DRIVE_SMART_CURRENT_LIMIT = 80

    object Gains {
      const val RAMSETE_B = 2.0
      const val RAMSETE_ZETA = 0.7
    }

    object PID {
      const val DIRECTION_KP = 0.00001
      const val DIRECTION_KI = 0.0
      const val DIRECTION_KD = 12.0
      const val DIRECTION_KFF = 0.000078

      const val DRIVE_KP = 0.0
      const val DRIVE_KI = 0.0
      const val DRIVE_KD = 0.0
      const val DRIVE_KFF = 0.0
      val DRIVE_KS = 1.0.volts
      val DRIVE_KV = 1.0.volts / 1.0.meters.perSecond
      val DRIVE_KA = 1.0.volts / 1.0.meters.perSecond.perSecond

      const val DRIVE_X_PID_KP = 0.0
      const val DRIVE_X_PID_KI = 0.0
      const val DRIVE_X_PID_KD = 0.0

      const val DRIVE_Y_PID_KP = 0.0
      const val DRIVE_Y_PID_KI = 0.0
      const val DRIVE_Y_PID_KD = 0.0

      const val DRIVE_THETA_PID_KP = 0.0
      const val DRIVE_THETA_PID_KI = 0.0
      const val DRIVE_THETA_PID_KD = 0.0
      val DRIVE_THETA_PID_MAX_VEL = 0.0.meters.perSecond
      val DRIVE_THETA_PID_MAX_ACCEL = 0.0.meters.perSecond.perSecond
    }
  }

  object Feeder {
    const val FLOOR_ID = 0
    const val VERTICAL_ID = 0
    const val FEEDER_POWER = 1.0

    const val TOP_DIO_PIN = 4
    const val BOTTOM_DIO_PIN = 9
    const val TAB = "Feeder"
  }

  object Intake {
    const val INTAKE_MOTOR = 0 // temp
    const val ARM_SOLENOID_PORT_1 = 0 // temp
    const val ARM_SOLENOID_PORT_2 = 1 // temp
    const val TAB = "Intake"

    enum class IntakeState(val speed: Double) {
      DEFAULT(0.0),
      IN(1.0),
      OUT(-1.0)
    }

    enum class ArmPosition(val position: DoubleSolenoid.Value?) {
      OUT(DoubleSolenoid.Value.kReverse),
      IN(DoubleSolenoid.Value.kForward),
      DEFAULT(DoubleSolenoid.Value.kOff)
    }
  }

  object Shooter {
    const val SHOOTER_MOTOR_ID = 0
    const val SHOOTER_FOLLOWER_ID = 0

    const val SOLENOID_FORWARD_CHANNEL = 0
    const val SOLENOID_REVERSE_CHANNEL = 1

    val TARGET_VELOCITY = 0.rotations.perMinute
    val VELOCITY_TOLERANCE = 60.rotations.perMinute

    const val SHOOTER_KS = 0.0
    const val SHOOTER_KV = 0.0

    const val SHOOTER_KP = 0.0
    const val SHOOTER_KI = 0.0
    const val SHOOTER_KD = 0.0

    val LINE_VELOCITY = 4600.0.rotations.perMinute
    val NEAR_VELOCITY = 4700.0.rotations.perMinute
    val MID_VELOCITY = 5300.0.rotations.perMinute
    val FAR_VELOCITY = 5700.0.rotations.perMinute

    val LINE_DISTANCE = 100.0.inches
    val NEAR_DISTANCE = 130.0.inches
    val MID_DISTANCE = 249.0.inches

    // val HOOD_THRESHOLD = 0.0.inches
  }

  object Vision {
    const val DRIVER_PIPELINE_ID = 1
    const val TARGETING_PIPELINE_ID = 0
    val TARGET_HEIGHT = 98.25.inches

    val CLOSE_CAMERA_HEIGHT = 0.0.inches
    val CLOSE_CAMERA_ANGLE = 0.0.degrees
    val FAR_CAMERA_HEIGHT = 35.0.inches
    val FAR_CAMERA_ANGLE = 24.0.degrees

    val CAMERA_DIST_THRESHOLD = 55.0.inches

    val MAX_DIST_ERROR = 0.1.inches
    val MAX_ANGLE_ERROR = 1.0.degrees

    const val MIN_TURN_COMMAND = 0.0

    object TurnGains {
      const val KP = 0.0
      const val KI = 0.0
      const val KD = 0.0

      val MAX_VELOCITY = 0.0.degrees.perSecond
      val MAX_ACCEL = 0.0.degrees.perSecond.perSecond
    }
  }

  object Climber {
    val CLIMBER_R_ARM_SPARKMAX_ID = 6 // right arm
    val CLIMBER_L_ARM_SPARKMAX_ID = 5 // left arm
    val CLIMBER_SENSOR_LINEARMECH_GEARRATIO = 8.4
    val CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER = .0508.meters // diameter: .0508 meters = 2 in
    val CLIMBER_SOLENOID_ACTUATIONSTATE = 0 // this is prolly not the right name for this parameter
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
