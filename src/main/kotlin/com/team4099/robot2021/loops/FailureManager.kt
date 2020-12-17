package com.team4099.robot2021.loops

import com.team4099.lib.logging.Logger
import org.graalvm.compiler.core.common.cfg.Loop

object FailureManager {


  //Map of Error Flags
  private val errorFlags = mutableMapOf(Failures.PRESSURE_LEAK to false, Failures.INTAKE_SPEC_VIOLATION to false)

  enum class Severity {
    WARNING,
    ERROR
  }

  enum class Failures (val severity : Severity) {
    PRESSURE_LEAK(Severity.ERROR),
    INTAKE_SPEC_VIOLATION(Severity.WARNING)
  }

  //have the motor as a parameter so it can be controlled (eg. motor drawing too much current so shut it down)
  //Create a when (switch) statement in the teleopInit for the loop
  fun addFailure(failType: Failures, condition: () -> Boolean) {
    //Set the failure to true
    errorFlags[failType] = true;
  }

  //fun removeFailure?

  fun addTestFailure(failType: Failures, condition: () -> Boolean) {

  }





}
