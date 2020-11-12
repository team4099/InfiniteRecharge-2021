package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Intake : SubsystemBase() {
  private val intakeTalon = TalonSRX(Constants.Intake.INTAKE_MOTOR)

  init {
      intakeTalon.config_kP(0, 0.1, 0)
  }
  private val intakeDoubleSolenoid = DoubleSolenoid(Constants.Intake.ARM_SOLENOID_PORT_1, Constants.Intake.ARM_SOLENOID_PORT_2)

  fun setPower(power: Double) {
    intakeTalon.set(ControlMode.PercentOutput, power)
  }
}
