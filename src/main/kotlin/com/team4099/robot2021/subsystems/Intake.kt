package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Intake : SubsystemBase() {

  private val intakeTalon = TalonFX(Constants.Intake.INTAKE_MOTOR)
  private val intakeDoubleSolenoid =
      DoubleSolenoid(Constants.Intake.ARM_SOLENOID_FORWARD, Constants.Intake.ARM_SOLENOID_REVERSE)

  // Sets intake motor to desired speed
  var intakeState = Constants.Intake.IntakeState.IDLE
    set(value) {
      intakeTalon.set(ControlMode.PercentOutput, value.speed)
      field = value
    }

  // Sets arm pneumatic to desired stage
  var armState = Constants.Intake.ArmPosition.IN
    set(value) {
      intakeDoubleSolenoid.set(value.position)
      field = value
    }

  init {
    intakeTalon.configFactoryDefault()
    Logger.addSource(Constants.Intake.TAB, "Intake State") { intakeState.toString() }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Power") { intakeTalon.motorOutputPercent }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Stator Current") {
      intakeTalon.statorCurrent
    }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Supply Current") {
      intakeTalon.supplyCurrent
    }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Voltage") {
      intakeTalon.motorOutputVoltage
    }
    Logger.addSource(Constants.Intake.TAB, "Arm State") { armState.toString() }
  }
}
