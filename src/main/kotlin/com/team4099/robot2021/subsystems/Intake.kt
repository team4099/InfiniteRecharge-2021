package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.Length
import com.team4099.lib.units.ctreLinearMechanismSensor
import com.team4099.lib.units.perSecond
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.loops.FailureManager
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase
/**
 *  Intake
 *
 *  @property intakeTalon Object representing the motor for the intake
 *  @property intakeDoubleSolenoid Object representing the solenoid for the arm
 *  @property armState Represents the stage of the arm
 *  @property intakeState Represents the stage of the intake
 *  @constructor Create empty Intake
 */
object Intake : SubsystemBase() {

  private val intakeTalon = TalonFX(Constants.Intake.INTAKE_MOTOR)
  private val intakeDoubleSolenoid = DoubleSolenoid(Constants.Intake.ARM_SOLENOID_PORT_1, Constants.Intake.ARM_SOLENOID_PORT_2)

  /**
   * Sets Intake state to the given argument in IntakeCommand
   */
  var intakeState = Constants.Intake.IntakeState.DEFAULT
    set(value) {
      intakeTalon.set(ControlMode.PercentOutput, value.speed)
      field = value
    }

  /**
   * Sets Arm state to the given argument in IntakeCommand
   */
  var armState = Constants.Intake.ArmPosition.DEFAULT
    set(value) {
      intakeDoubleSolenoid.set(value.position)
      field = value
    }

  /**
   * @property intakeTalon configurations reset to ensure no saved data
   */
  init {
    intakeTalon.configFactoryDefault()
    Logger.addSource(Constants.Intake.TAB, "Intake State") { intakeState.toString() }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Power") { intakeTalon.motorOutputPercent }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Stator Current") { intakeTalon.statorCurrent }
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Supply Current") { intakeTalon.supplyCurrent}
    Logger.addSource(Constants.Intake.TAB, "Intake Motor Voltage") { intakeTalon.motorOutputVoltage}
    Logger.addSource(Constants.Intake.TAB, "Arm State") { armState.toString()}
  }

}
