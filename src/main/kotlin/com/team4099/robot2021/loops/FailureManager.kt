package com.team4099.robot2021.loops

import com.team4099.robot2021.Robot
import edu.wpi.first.wpilibj.Sendable
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder

object FailureManager: Sendable {
  //Map of Error Flags

  private val failures = mutableListOf<FailureSource>()
  private var log = ""

  val errorFlags = mutableMapOf(
    Failures.PRESSURE_LEAK to false,
    Failures.INTAKE_SPEC_VIOLATION to false
  )

  enum class Severity {
    WARNING,
    ERROR
  }

  enum class Failures (val severity : Severity, val description: String) {
    PRESSURE_LEAK(Severity.ERROR, "Pressure Leak"),
    INTAKE_SPEC_VIOLATION(Severity.WARNING, "intake bad"),
    CLIMBER_FAILED_POS(Severity.ERROR, "Climber position is wrong");
  }

  fun addFailure(failType: Failures, latching: Boolean = false, condition: () -> Boolean) {
    failures.add(FailureSource(failType, latching, condition, false))
  }

  fun addTestFailure(failType: Failures, condition: () -> Boolean) {
    failures.add(FailureSource(failType, true, condition, true))
  }

  fun checkFailures() {
    failures.forEach { failure ->
      if (!failure.testOnly || Robot.isTest) {
        if (!failure.latching) {
          errorFlags[failure.failType] = failure.condition()
        } else if (failure.condition()) {
          errorFlags[failure.failType] = true
        }
      }
    }
  }

  fun reset(){
    failures.forEach{failure -> errorFlags[failure.failType] = false}
    log = ""
  }

  fun logDashboard(string: String) {
    log += "$string\n"
    log.trim();
  }

  override fun initSendable(builder: SendableBuilder?) {
    builder?.addStringProperty("Failures", { log }) {}
  }
}
data class FailureSource(val failType: FailureManager.Failures, val latching: Boolean, val condition: () -> Boolean, val testOnly: Boolean)
