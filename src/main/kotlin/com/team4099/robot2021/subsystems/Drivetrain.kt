package com.team4099.robot2021.subsystems

import com.kauailabs.navx.frc.AHRS
import com.revrobotics.SparkMax
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.SPI
import kotlin.math.hypot

// code "derived" from Strykeforce's SwerveDrive
/**
 * Control a Third Coast swerve drive.
 *
 *
 * Wheels are a array numbered 0-3 from front to back, with even numbers on the left side when
 * facing forward.
 *
 *
 * Derivation of inverse kinematic equations are from Ether's [Swerve Kinematics and Programming](https://www.chiefdelphi.com/media/papers/2426).
 *
 * @see Wheel
 */
object SwerveDrive : SubsystemBase() {
  /**
   * Get the gyro instance being used by the drive.
   *
   * @return the gyro instance
   */
  val gyro: AHRS
  
  /**
   * Unit testing
   *
   * @return length
   */
  val lengthComponent: Double
  
  /**
   * Unit testing
   *
   * @return width
   */
  val widthComponent: Double

  private var kGyroRateCorrection = 0.0
  private val modules: Array<SwerveModule>
  private val ws = DoubleArray(Constants.Drivetrain.WHEEL_COUNT)
  private val wa = DoubleArray(Constants.Drivetrain.WHEEL_COUNT)
  private var isFieldOriented = false


  /**
   * Set the drive mode.
   *
   * @param driveMode the drive mode
   */
  fun setDriveMode(driveMode: DriveMode?) {
    for (module in modules) {
      module.setDriveMode(driveMode)
    }
//    logger.info("drive mode = {}", driveMode)
  }
  
  /**
   * Set all four wheels to specified values.
   *
   * @param azimuth -0.5 to 0.5 rotations, measured clockwise with zero being the robot
   * straight-ahead position
   * @param drive 0 to 1 in the direction of the wheel azimuth
   */
  operator fun set(azimuth: Double, drive: Double) {
    for (wheel in modules) {
      wheel.set(azimuth, drive)
    }
  }
  
  /**
   * Drive the robot in given field-relative direction and with given rotation.
   *
   * @param forward Y-axis movement, from -1.0 (reverse) to 1.0 (forward)
   * @param strafe X-axis movement, from -1.0 (left) to 1.0 (right)
   * @param azimuth robot rotation, from -1.0 (CCW) to 1.0 (CW)
   */
  fun drive(forward: Double, strafe: Double, azimuth: Double) {
    
    // Use gyro for field-oriented drive. We use getAngle instead of getYaw to enable arbitrary
    // autonomous starting positions.
    var forward = forward
    var strafe = strafe
    if (isFieldOriented) {
      var angle = gyro!!.angle
      angle += gyro.rate * kGyroRateCorrection
      angle = Math.IEEEremainder(angle, 360.0)
      angle = Math.toRadians(angle)
      val temp = forward * Math.cos(angle) + strafe * Math.sin(angle)
      strafe = strafe * Math.cos(angle) - forward * Math.sin(angle)
      forward = temp
    }
    val a = strafe - azimuth * lengthComponent
    val b = strafe + azimuth * lengthComponent
    val c = forward - azimuth * widthComponent
    val d = forward + azimuth * widthComponent
    
    // wheel speed
    ws[0] = Math.hypot(b, d)
    ws[1] = Math.hypot(b, c)
    ws[2] = Math.hypot(a, d)
    ws[3] = Math.hypot(a, c)
    
    // wheel azimuth
    wa[0] = Math.atan2(b, d) * 0.5 / Math.PI
    wa[1] = Math.atan2(b, c) * 0.5 / Math.PI
    wa[2] = Math.atan2(a, d) * 0.5 / Math.PI
    wa[3] = Math.atan2(a, c) * 0.5 / Math.PI
    
    // normalize wheel speed
    val maxWheelSpeed = Math.max(Math.max(ws[0], ws[1]), Math.max(ws[2], ws[3]))
    if (maxWheelSpeed > 1.0) {
      for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
        ws[i] /= maxWheelSpeed
      }
    }
    
    // set wheels
    for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
      modules[i].set(wa[i], ws[i])
    }
  }
  
  /**
   * Stops all wheels' azimuth and drive movement. Calling this in the robots `teleopInit` and
   * `autonomousInit` will reset wheel azimuth relative encoders to the current position and
   * thereby prevent wheel rotation if the wheels were moved manually while the robot was disabled.
   */
  fun stop() {
    for (wheel in modules) {
      wheel.stop()
    }
//    logger.info("stopped all wheels")
  }
  
  /**
   * Save the wheels' azimuth current position as read by absolute encoder. These values are saved
   * persistently on the roboRIO and are normally used to calculate the relative encoder offset
   * during wheel initialization.
   *
   *
   * The wheel alignment data is saved in the WPI preferences data store and may be viewed using
   * a network tables viewer.
   *
   * @see .zeroAzimuthEncoders
   */
  fun saveAzimuthPositions() {
    saveAzimuthPositions(Preferences.getInstance())
  }
  
  fun saveAzimuthPositions(prefs: Preferences) {
    for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
      val position: Int = modules[i].getAzimuthAbsolutePosition()
      prefs.putInt(getPreferenceKeyForWheel(i), position)
//      logger.info("azimuth {}: saved zero = {}", i, position)
    }
  }
  
  /**
   * Set wheels' azimuth relative offset from zero based on the current absolute position. This uses
   * the physical zero position as read by the absolute encoder and saved during the wheel alignment
   * process.
   *
   * @see .saveAzimuthPositions
   */
  fun zeroAzimuthEncoders() {
    zeroAzimuthEncoders(Preferences.getInstance())
  }
  
  fun zeroAzimuthEncoders(prefs: Preferences) {
//    Errors.setCount(0)
    for (i in 0 until Constants.Drivetrain.WHEEL_COUNT) {
      val position = prefs.getInt(getPreferenceKeyForWheel(i), Constants.Drivetrain.DEFAULT_ABSOLUTE_AZIMUTH_OFFSET)
      modules[i].setAzimuthZero(position)
//      logger.info("azimuth {}: loaded zero = {}", i, position)
    }
//    val errorCount: Int = Errors.getCount()
//    if (errorCount > 0) logger.error("TalonSRX set azimuth zero error count = {}", errorCount)
  }
  
  /**
   * Returns the four wheels of the swerve drive.
   *
   * @return the Wheel array.
   */
  fun getWheels(): Array<SwerveModule> {
    return modules
  }
  
  /**
   * Get status of field-oriented driving.
   *
   * @return status of field-oriented driving.
   */
  fun isFieldOriented(): Boolean {
    return isFieldOriented
  }
  
  /**
   * Enable or disable field-oriented driving. Enabled by default if connected gyro is passed in via
   * `SwerveDriveConfig` during construction.
   *
   * @param enabled true to enable field-oriented driving.
   */
  fun setFieldOriented(enabled: Boolean) {
    isFieldOriented = enabled
//    logger.info("field orientation driving is {}", if (isFieldOriented) "ENABLED" else "DISABLED")
  }
  
  /** Swerve Drive drive mode  */
  enum class DriveMode {
    OPEN_LOOP, CLOSED_LOOP, TELEOP, TRAJECTORY, AZIMUTH
  }
  
  companion object {
    const val DEFAULT_ABSOLUTE_AZIMUTH_OFFSET = 200
//    private val logger: Logger = LoggerFactory.getLogger(SwerveDrive::class.java)
    private const val WHEEL_COUNT = 4
    
    /**
     * Return key that wheel zero information is stored under in WPI preferences.
     *
     * @param wheel the wheel number
     * @return the String key
     */
    fun getPreferenceKeyForWheel(wheel: Int): String {
      return String.format("%s/wheel.%d", SwerveDrive::class.java.simpleName, wheel)
    }
  }
  
  init {
    gyro = AHRS(SPI.Port.kMXP)
    modules = config.wheels
//    val summarizeErrors: Boolean = config.summarizeTalonErrors
//    Errors.setSummarized(summarizeErrors)
//    Errors.setCount(0)
//    logger.debug("TalonSRX configuration errors summarized = {}", summarizeErrors)
    val length: Double = Constants.Drivetrain.DRIVETRAIN_LENGTH
    val width: Double = Constants.Drivetrain.DRIVETRAIN_WIDTH
    val radius = hypot(length, width)
    lengthComponent = length / radius
    widthComponent = width / radius
//    logger.info("gyro is configured: {}", gyro != null)
//    logger.info("gyro is connected: {}", gyro != null && gyro.isConnected)
    setFieldOriented(gyro != null && gyro.isConnected)
    if (isFieldOriented) {
//      gyro.enableLogging(config.gyroLoggingEnabled)
      val robotPeriod: Double = config.robotPeriod
      val gyroRateCoeff: Double = config.gyroRateCoeff
      val rate = gyro.actualUpdateRate
      val gyroPeriod = 1.0 / rate
      kGyroRateCorrection = robotPeriod / gyroPeriod * gyroRateCoeff
//      logger.debug("gyro frequency = {} Hz", rate)
    } else {
//      logger.warn("gyro is missing or not enabled")
      kGyroRateCorrection = 0.0
    }
//    logger.debug("length = {}", length)
//    logger.debug("width = {}", width)
//    logger.debug("enableGyroLogging = {}", config.gyroLoggingEnabled)
//    logger.debug("gyroRateCorrection = {}", kGyroRateCorrection)
  }
}
