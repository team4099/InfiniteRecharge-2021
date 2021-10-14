package com.team4099.robot2021.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Intake : SubsystemBase() {

  private val intakeMotor =
      CANSparkMax(Constants.Intake.INTAKE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless)
  // private val intakeTalon = TalonFX(Constants.Intake.INTAKE_MOTOR)
  private val intakeDoubleSolenoid =
      DoubleSolenoid(Constants.Intake.ARM_SOLENOID_FORWARD, Constants.Intake.ARM_SOLENOID_REVERSE)

  // Sets intake motor to desired speed
  var intakeState = Constants.Intake.IntakeState.IDLE
    set(value) {
      intakeMotor.set(value.speed)
      field = value
    }

  // Sets arm pneumatic to desired stage
  var armState = Constants.Intake.ArmPosition.IN
    set(value) {
      intakeDoubleSolenoid.set(value.position)
      field = value
    }

  init {
    Logger.addSource(Constants.Intake.TAB, "Intake State") { intakeState.toString() }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Power") { intakeMotor.appliedOutput }
    /*Logger.addSource(Constants.Intake.TAB, "Intake Motor Stator Current") {
      intakeMotor.statorCurrent
    }*/
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Supply Current") {
      intakeMotor.outputCurrent
    }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Voltage") { intakeMotor.busVoltage }
    Logger.addSource(Constants.Intake.TAB, "Arm State") { armState.toString() }

    intakeMotor.restoreFactoryDefaults()
    intakeMotor.setOpenLoopRampRate(Constants.Intake.RAMP_TIME)

    // intakeMotor.enableVoltageCompensation(10.0)
    intakeMotor.setSmartCurrentLimit(Constants.Intake.CURRENT_LIMIT)
    intakeMotor.setIdleMode(CANSparkMax.IdleMode.kCoast)
    // intakeMotor.inverted = true

    intakeMotor.burnFlash()
  }
}
