package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Intake : SubsystemBase() {
  private val intakeTalon = TalonSRX(5); //5 is temporary

  fun setPower(power: Double) {
    intakeTalon.set(ControlMode.PercentOutput, power)
  }
}
