package org.strykeforce.thirdcoast.swerve

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.BaseTalon
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import com.revrobotics.SparkMax
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.Velocity
import com.team4099.lib.units.derived.Angle
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.subsystems.SwerveDrive.DriveMode
import java.util.*
import java.util.function.DoubleConsumer
import kotlin.math.IEEErem
import kotlin.math.abs

/**
 * Controls a swerve drive wheel azimuth and drive motors.
 *
 *
 * The swerve-drive inverse kinematics algorithm will always calculate individual wheel angles as
 * -0.5 to 0.5 rotations, measured clockwise with zero being the straight-ahead position. Wheel
 * speed is calculated as 0 to 1 in the direction of the wheel angle.
 *
 *
 * This class will calculate how to implement this angle and drive direction optimally for the
 * azimuth and drive motors. In some cases it makes sense to reverse wheel direction to avoid
 * rotating the wheel azimuth 180 degrees.
 *
 *
 * Hardware assumed by this class includes a CTRE magnetic encoder on the azimuth motor and no
 * limits on wheel azimuth rotation. Azimuth Talons have an ID in the range 0-3 with corresponding
 * drive Talon IDs in the range 10-13.
 */
class SwerveModule(private val directionSpark: CANSparkMax, private val driveSpark: CANSparkMax, private val driveSetpointMax: LinearVelocity, private val encoder: CANCoder) {

  /**
   * Get the drive Talon controller.
   *
   * @return drive Talon instance used by wheel
   */
  /**
   * Get the azimuth Talon controller.
   *
   * @return azimuth Talon instance used by wheel
   */

  private var driver: (Double) -> Unit = { setpoint: Double -> driveSpark.set(setpoint) }

  var isInverted = false
    private set

  /**
   * This method calculates the optimal driveTalon settings and applies them.
   *
   * @param direction -0.5 to 0.5 rotations, measured clockwise with zero being the wheel's zeroed
   * position
   * @param drive 0 to 1.0 in the direction of the wheel azimuth
   */
  operator fun set(direction: Angle, drive: Double) {
    // don't reset wheel azimuth direction to zero when returning to neutral
    var azimuth = direction
    var drive = drive
    if (drive == 0.0) {
      driver(0.0)
      return
    }
    val azimuthPosition = directionSpark.encoder.position
    var azimuthError = (azimuth - azimuthPosition).IEEErem(Constants.Drivetrain.TICKS.toDouble())

    // minimize azimuth rotation, reversing drive if necessary
    isInverted = abs(azimuthError) > 0.25 * Constants.Drivetrain.TICKS
    if (isInverted) {
      azimuthError -= Math.copySign(0.5 * Constants.Drivetrain.TICKS, azimuthError)
      drive = -drive
    }
    directionSpark.pidController.setReference(azimuthPosition + azimuthError, ControlType.kSmartMotion)
    driver(drive)
  }

  /**
   * Set azimuth to encoder position.
   *
   * @param position position in encoder ticks.
   */
  fun setAzimuthPosition(position: Int) {
    directionSpark.pidController.setReference(position.toDouble(), ControlType.kSmartMotion)
  }

  fun disableAzimuth() {
    directionSpark.disable()
  }

  /**
   * Set the operating mode of the wheel's drive motors. In this default wheel implementation `OPEN_LOOP` and `TELEOP` are equivalent and `CLOSED_LOOP`, `TRAJECTORY` and
   * `AZIMUTH` are equivalent.
   *
   *
   * In closed-loop modes, the drive setpoint is scaled by the drive Talon `driveSetpointMax` parameter.
   *
   *
   * This method is intended to be overridden if the open or closed-loop drive wheel drivers need
   * to be customized.
   *
   * @param driveMode the desired drive mode
   */
  fun setDriveMode(driveMode: DriveMode?) {
    when (driveMode) {
      DriveMode.OPEN_LOOP, DriveMode.TELEOP -> driver =
        { setpoint: Double ->
          driveSpark.set(setpoint)
        }
      DriveMode.CLOSED_LOOP, DriveMode.TRAJECTORY, DriveMode.AZIMUTH -> driver =
        { setpoint: Double ->
          driveSpark.pidController.setReference(setpoint, ControlType.kVelocity)
        }
    }
  }

  /**
   * Stop azimuth and drive movement. This resets the azimuth setpoint and relative encoder to the
   * current position in case the wheel has been manually rotated away from its previous setpoint.
   */
  fun stop() {
    directionSpark.pidController.setReference(directionSpark.encoder.position, ControlType.kSmartMotion)
    driver(0.0)
  }

  /**
   * Set the azimuthTalon encoder relative to wheel zero alignment position. For example, if current
   * absolute encoder = 0 and zero setpoint = 2767, then current relative setpoint = -2767.
   *
   * <pre>
   *
   * relative:  -2767                               0
   * ---|---------------------------------|-------
   * absolute:    0                               2767
   *
  </pre> *
   *
   * @param zero zero setpoint, absolute encoder position (in ticks) where wheel is zeroed.
   */
  fun setAzimuthZero(zero: Degrees) {
    encoder.configMagnetOffset(zero)
    val azimuthSetpoint = azimuthAbsolutePosition - zero
    val err = directionSpark.pidController.setS(azimuthSetpoint, 0, 10)
    Errors.check(err, logger)
    directionSpark[ControlMode.MotionMagic] = azimuthSetpoint.toDouble()
  }

  /**
   * Returns the wheel's azimuth absolute position in encoder ticks.
   *
   * @return 0 - 4095, corresponding to one full revolution.
   */
  val azimuthAbsolutePosition: Double
    get() = encoder.absolutePosition /*and 0xFFF*/

  override fun toString(): String {
    return ("Wheel{"
      + "azimuthTalon="
      + directionSpark
      + ", driveTalon="
      + driveSpark
      + ", driveSetpointMax="
      + driveSetpointMax
      + '}')
  }

  /**
   * This constructs a wheel with supplied azimuth and drive talons.
   *
   *
   * Wheels will scale closed-loop drive output to `driveSetpointMax`. For example, if
   * closed-loop drive mode is tuned to have a max usable output of 10,000 ticks per 100ms, set this
   * to 10,000 and the wheel will send a setpoint of 10,000 to the drive talon when wheel is set to
   * max drive output (1.0).
   *
   * @param azimuth the configured azimuth TalonSRX
   * @param drive the configured drive TalonSRX
   * @param driveSetpointMax scales closed-loop drive output to this value when drive setpoint = 1.0
   */
  init {
    setDriveMode(DriveMode.TELEOP)
//    logger.debug("azimuth = {} drive = {}", directionSpark.deviceID, driveSpark.deviceID)
//    logger.debug("driveSetpointMax = {}", driveSetpointMax)
//    if (driveSetpointMax == 0.0) logger.warn("driveSetpointMax may not have been configured")
  }
}
