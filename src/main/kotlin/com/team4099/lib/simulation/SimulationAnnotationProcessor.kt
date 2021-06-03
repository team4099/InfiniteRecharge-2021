package com.team4099.lib.simulation

import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Subsystem

object SimulationAnnotationProcessor {
  fun registerSubsystem(subsystem: Subsystem) {
    subsystem::class.members.forEach { s ->
      s.annotations.forEach {
        print(it)
      }
    }
  }
}
