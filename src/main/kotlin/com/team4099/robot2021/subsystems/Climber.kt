package com.team4099.robot2021.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.inInches
import com.team4099.lib.units.inInchesPerSecond
import com.team4099.lib.units.sparkMaxLinearMechanismSensor
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Climber : SubsystemBase() {
  private val climberRArm =
      CANSparkMax(Constants.Climber.R_ARM_ID, CANSparkMaxLowLevel.MotorType.kBrushless)
  private val climberLArm =
      CANSparkMax(Constants.Climber.L_ARM_ID, CANSparkMaxLowLevel.MotorType.kBrushless)

  // private val climberRArmPIDController = climberRArm.pidController
  // private val climberLArmPIDController = climberLArm.pidController
  val climberRArmSensor =
      sparkMaxLinearMechanismSensor(
          climberRArm,
          Constants.Climber.CLIMBER_SENSOR_LINEARMECH_GEARRATIO,
          Constants.Climber
              .CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER) // diameter: .0508 meters = 2 in
  val climberLArmSensor =
      sparkMaxLinearMechanismSensor(
          climberLArm,
          Constants.Climber.CLIMBER_SENSOR_LINEARMECH_GEARRATIO,
          Constants.Climber
              .CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER) // diameter: .0508 meters = 2 in

  // out to lock
  private val pneumaticBrake =
      Solenoid(
          Constants.Climber
              .CLIMBER_SOLENOID_ID) // unactuated state is having the pneumatic extended
  // out to lock
  var brakeApplied = true
    set(value) {
      field = value
      pneumaticBrake.set(!value)
    }

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
      climberRArmSensor.velocity.inInchesPerSecond
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Right Arm Current Position") {
      climberRArmSensor.position.inInches
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
    Logger.addSource(Constants.Climber.TAB, "Climber Left Arm Motor Velocity") {
      climberLArmSensor.velocity.inInchesPerSecond
    }
    Logger.addSource(Constants.Climber.TAB, "Climber Left Arm Current Position") {
      climberLArmSensor.position.inInches
    }

    Logger.addSource(Constants.Climber.TAB, "Right Pneumatics State") { brakeApplied.toString() }
    Logger.addSource(Constants.Climber.TAB, "Left Pneumatics State") { brakeApplied.toString() }

    climberRArm.restoreFactoryDefaults()
    climberRArm.inverted = true
    // climberRArmPIDController.p = Constants.Climber.CLIMBER_P
    // climberRArmPIDController.i = Constants.Climber.CLIMBER_I
    // climberRArmPIDController.d = Constants.Climber.CLIMBER_D
    // climberRArmPIDController.setSmartMotionMaxVelocity(
    //    climberRArmSensor.velocityToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_VEL), 0)
    // climberRArmPIDController.setSmartMotionMaxAccel(
    //    climberRArmSensor.accelerationToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_ACC), 0)
    climberRArm.idleMode = CANSparkMax.IdleMode.kBrake
    climberRArm.burnFlash()

    climberLArm.restoreFactoryDefaults()
    climberLArm.inverted = true
    // climberLArmPIDController.p = Constants.Climber.CLIMBER_P
    // climberLArmPIDController.i = Constants.Climber.CLIMBER_I
    // climberLArmPIDController.d = Constants.Climber.CLIMBER_D
    // climberLArmPIDController.setSmartMotionMaxVelocity(
    //    climberLArmSensor.velocityToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_VEL), 0)
    // climberLArmPIDController.setSmartMotionMaxAccel(
    //    climberLArmSensor.accelerationToRawUnits(Constants.Climber.CLIMBER_SPARKMAX_ACC), 0)
    climberLArm.idleMode = CANSparkMax.IdleMode.kBrake
    climberLArm.burnFlash()
  }

  fun setPosition(position: Constants.ClimberPosition) {
    //  climberRArmPIDController.setReference(
    //      climberRArmSensor.positionToRawUnits(position.length), ControlType.kSmartMotion)
    //  climberLArmPIDController.setReference(
    //      climberLArmSensor.positionToRawUnits(position.length), ControlType.kSmartMotion)
  }

  fun setOpenLoopPower(leftPower: Double, rightPower: Double, safetyEnabled: Boolean = true) {
    Logger.addEvent("Climber", "Left power: $leftPower Right power: $rightPower")
    if (safetyEnabled &&
        ((climberLArmSensor.position < Constants.Climber.BOTTOM_SAFETY_THRESHOLD &&
            leftPower < 0.0) ||
            (climberLArmSensor.position > Constants.Climber.TOP_SAFETY_THRESHOLD &&
                leftPower > 0.0))) {
      climberLArm.set(0.0)
    } else {
      climberLArm.set(leftPower)
    }
    if (safetyEnabled &&
        ((climberRArmSensor.position < Constants.Climber.BOTTOM_SAFETY_THRESHOLD &&
            rightPower < 0.0) ||
            (climberRArmSensor.position > Constants.Climber.TOP_SAFETY_THRESHOLD &&
                rightPower > 0.0))) {
      climberRArm.set(0.0)
    } else {
      climberRArm.set(rightPower)
    }
  }

  fun zeroLeftEncoder() {
    climberLArm.encoder.position = 0.0
  }

  fun zeroRightEncoder() {
    climberRArm.encoder.position = 0.0
  }
}
