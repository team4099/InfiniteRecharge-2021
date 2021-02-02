package com.team4099.robot2021.loops

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.Robot
import edu.wpi.first.wpilibj.Sendable
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder

/**
 * Failure manager
 *
 * @constructor Create empty Failure manager
 */
object FailureManager: Sendable {
  /**
   * Failures
   *
   * @param failures Map of Error Flags
   * @param log String representing the output of failures
   */
  private val failures = mutableListOf<FailureSource>()
  private var log = ""

  /**
   * Error flags
   */
  val errorFlags = mutableMapOf(
    Failures.PRESSURE_LEAK to false,
    Failures.INTAKE_SPEC_VIOLATION to false,
    Failures.CLIMBER_FAILED_POS to false
  )

  /**
   * Severity
   *
   * @property Severity The levels of bad of the failure
   * @constructor Create empty Severity
   */
  enum class Severity {
    WARNING,
    ERROR
  }

  /**
   * Failures
   *
   * @property severity Enum above giving classification of the severity level of the failure
   * @property description Gives details about the given failure
   * @constructor Create empty Failures
   */
  enum class Failures (val severity : Severity, val description: String) {
    PRESSURE_LEAK(Severity.ERROR, "Pressure Leak, pressure under"),
    INTAKE_SPEC_VIOLATION(Severity.WARNING, "intake bad"),
    CLIMBER_FAILED_POS(Severity.ERROR, "Climber position is wrong");
  }

  /**
   * Add failure
   *
   * @param failType Name implies
   * @param latching If true failure can't be removed
   * @param condition Given the condition will determine if there is a failure
   * @receiver
   */
  fun addFailure(failType: Failures, latching: Boolean = false, condition: () -> Boolean) {
    failures.add(FailureSource(failType, latching, condition, false))
  }

  /**
   * Add test failure (test mode)
   *
   * @param failType Name implies
   * @param condition Given the condition will determine if there is a failure
   * @receiver
   */
  fun addTestFailure(failType: Failures, condition: () -> Boolean) {
    failures.add(FailureSource(failType, true, condition, true))
  }

  /**
   * Check failures
   *
   * Goes through each failure in the list and checks if it's true and if so logs it
   */
  fun checkFailures() {
    failures.forEach { failure ->
      if (!failure.testOnly || Robot.isTest) {
        if (!failure.latching) {
          if (failure.condition() && errorFlags[failure.failType] == false) logDashboard(failure.failType.description)
          errorFlags[failure.failType] = failure.condition()
        } else if (failure.condition()) {
          if (errorFlags[failure.failType] == false) logDashboard(failure.failType.description)
          errorFlags[failure.failType] = true
        }
      }
    }
  }

  /**
   * Reset
   *
   * Method that resets each failure to false
   */
  fun reset(){
    failures.forEach{failure -> errorFlags[failure.failType] = false}
    log = ""
  }

  /**
   * Log dashboard
   *
   * @param string Description of failure
   */
  fun logDashboard(string: String) {
    Logger.addEvent("FailureManager", string)
    log += "\n$string"
    log.trim()
  }

  /**
   * Init sendable
   *
   * @param builder
   */
  override fun initSendable(builder: SendableBuilder?) {
    builder?.addStringProperty("Failures", { log }) {}
  }
}
data class FailureSource(val failType: FailureManager.Failures, val latching: Boolean, val condition: () -> Boolean, val testOnly: Boolean)
