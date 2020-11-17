package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.ctreLinearMechanismSensor
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Intake : SubsystemBase() {

  private val intakeTalon = TalonFX(Constants.Intake.INTAKE_MOTOR)

  private val intakeDoubleSolenoid = DoubleSolenoid(Constants.Intake.ARM_SOLENOID_PORT_1, Constants.Intake.ARM_SOLENOID_PORT_2)

  fun setVelocity(velocity: Constants.Intake.IntakeState) {
    intakeTalon.set(ControlMode.PercentOutput, velocity.speed)
  }

  fun setPosition(position: Constants.Intake.ArmPosition) {
    when(position) {
      Constants.Intake.ArmPosition.IN ->
        intakeDoubleSolenoid.set(DoubleSolenoid.Value.kReverse)
      Constants.Intake.ArmPosition.OUT ->
        intakeDoubleSolenoid.set(DoubleSolenoid.Value.kForward)
      Constants.Intake.ArmPosition.DEFAULT ->
        intakeDoubleSolenoid.set(DoubleSolenoid.Value.kOff)
    }
  }
}
