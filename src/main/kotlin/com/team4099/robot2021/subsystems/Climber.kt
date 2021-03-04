package com.team4099.robot2021.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.ControlType
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.sparkMaxLinearMechanismSensor
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Climber : SubsystemBase() {
  private val climberRArm =
      CANSparkMax(
          Constants.Climber.CLIMBER_R_ARM_SPARKMAX_ID, CANSparkMaxLowLevel.MotorType.kBrushless)
  private val climberLArm =
      CANSparkMax(
          Constants.Climber.CLIMBER_L_ARM_SPARKMAX_ID, CANSparkMaxLowLevel.MotorType.kBrushless)
  private val climberRArmPIDController = climberRArm.pidController
  private val climberLArmPIDController = climberLArm.pidController
  private val climberRArmSensor =
      sparkMaxLinearMechanismSensor(
          climberRArm,
          Constants.Climber.CLIMBER_SENSOR_LINEARMECH_GEARRATIO,
          Constants.Climber
              .CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER) // diameter: .0508 meters = 2 in
  private val climberLArmSensor =
      sparkMaxLinearMechanismSensor(
          climberLArm,
          Constants.Climber.CLIMBER_SENSOR_LINEARMECH_GEARRATIO,
          Constants.Climber
              .CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER) // diameter: .0508 meters = 2 in
  private val pneumaticRBrake =
      Solenoid(
          Constants.Climber
              .CLIMBER_SOLENOID_ACTUATIONSTATE) // unactuated state is having the pneumatic extended
  // out to lock
  private val pneumaticLBrake =
      Solenoid(
          Constants.Climber
              .CLIMBER_SOLENOID_ACTUATIONSTATE) // unactuated state is having the pneumatic extended
  // out to lock
  var brakeApplied = false
    set(value) {
      field = value
      pneumaticRBrake.set(value)
      pneumaticLBrake.set(value)
    }
  var encoderR = climberRArm.encoder
  var encoderL = climberLArm.encoder

  init {
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Motor Power") {
      climberLArm.appliedOutput
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Output Current") {
      climberRArm.outputCurrent
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Motor Applied Voltage") {
      climberRArm.busVoltage
    } // idk if this correct
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Motor Velocity") {
      encoderR.velocity
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Current Position") {
      encoderR.position
    }

    Logger.addSource(Constants.Climber.TAB, "Climber Left Arm Motor Power") {
      climberLArm.appliedOutput
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Left Arm Output Current") {
      climberLArm.outputCurrent
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Left Arm Motor Applied Voltage") {
      climberLArm.busVoltage
    } // idk if this correct
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Motor Velocity") {
      encoderL.velocity
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Current Position") {
      encoderL.position
    }

    Logger.addSource(Constants.Climber.TAB, "Right Pneumatics State") { brakeApplied.toString() }
    Logger.addSource(Constants.Climber.TAB, "Left Pneumatics State") { brakeApplied.toString() }

    climberRArm.restoreFactoryDefaults()
    climberRArmPIDController.p = Constants.Climber.CLIMBER_P
    climberRArmPIDController.i = Constants.Climber.CLIMBER_I
    climberRArmPIDController.d = Constants.Climber.CLIMBER_D
    climberRArmPIDController.setSmartMotionMaxVelocity(
        climberRArmSensor.velocityToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_VEL), 0)
    climberRArmPIDController.setSmartMotionMaxAccel(
        climberRArmSensor.accelerationToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_ACC), 0)
    climberRArm.burnFlash()

    climberLArm.restoreFactoryDefaults()
    climberLArmPIDController.p = Constants.Climber.CLIMBER_P
    climberLArmPIDController.i = Constants.Climber.CLIMBER_I
    climberLArmPIDController.d = Constants.Climber.CLIMBER_D
    climberLArmPIDController.setSmartMotionMaxVelocity(
        climberLArmSensor.velocityToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_VEL), 0)
    climberLArmPIDController.setSmartMotionMaxAccel(
        climberLArmSensor.accelerationToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_ACC), 0)
    climberLArm.burnFlash()
  }

  fun setPosition(position: Constants.ClimberPosition) {
    climberRArmPIDController.setReference(
        climberRArmSensor.positionToRawUnits(position.length), ControlType.kSmartMotion)
    climberLArmPIDController.setReference(
        climberLArmSensor.positionToRawUnits(position.length), ControlType.kSmartMotion)
  }

  fun setOpenLoopPower(power: Double) {
    climberRArm.set(power)
    climberLArm.set(power)
  }
}
