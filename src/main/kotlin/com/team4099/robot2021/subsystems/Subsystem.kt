package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.units.ctreAngularMechanismSensor
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Subsystem : SubsystemBase() {
  private val motor = TalonFX(20);
  private val sensor = ctreAngularMechanismSensor()
}
