package com.team4099.robot2021.loops

import com.team4099.lib.logging.Logger

object FailureManager : Loop {
  var rio3v3Faults = 0
    set(faults) {
      if (field != faults) {
        Logger.addEvent("Fault Detector", "roboRIO 3.3V Fault")
      }
      field = faults
    }
  var rio5vFaults = 0
    set(faults) {
      if (field != faults) {
        Logger.addEvent("Fault Detector", "roboRIO 5V Fault")
      }
      field = faults
    }
  var rio6vFaults = 0
    set(faults) {
      if (field != faults) {
        Logger.addEvent("Fault Detector", "roboRIO 6V Fault")
      }
      field = faults
    }
}
