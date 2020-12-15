package com.team4099.robot2021.loops

import com.team4099.lib.logging.Logger
import org.graalvm.compiler.core.common.cfg.Loop

object FailureManager {

  enum class Severity {
    WARNING,
    ERROR
  }

  enum class Failures (val severity : Severity) {
    PRESSURE_LEAK(Severity.ERROR)
  }

  //have the motor as a parameter so it can be controlled (eg. motor drawing too much current so shut it down)
  fun addFailure(failType: Failures, condition: () -> Boolean) {
    //what to do with error?
    when(failType) {

    }
  }

  //fun removeFailure?

  fun addTestFailure(failType: Failures, condition: () -> Boolean) {

  }





}
