package com.team4099.robot2021.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.ControlType
import com.team4099.lib.units.base.meters
import com.team4099.lib.units.perSecond
import com.team4099.lib.units.sparkMaxLinearMechanismSensor
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Climber: SubsystemBase() {
  private val climber = CANSparkMax(Constants.Climber.CLIMBER_SPARKMAX_ID, CANSparkMaxLowLevel.MotorType.kBrushless)
  private val climberPIDController = climber.pidController
  private val climberSensor = sparkMaxLinearMechanismSensor(climber, Constants.Climber.CLIMBER_SENSOR_LINEARMECH_RATIO, Constants.Climber.CLIMBER_SENSOR_LINEARMECH_PULLEYDIAMETER)//diameter: .0508 meters = 2 in
  private val pneumaticBrake = Solenoid(Constants.Climber.CLIMBER_SOLENOID_ACTUATIONSTATE) //unactuated state is having the pneumatic extended out to lock
  var brakeApplied = false
    set(value) {
      field = value
      pneumaticBrake.set(value)
    }

  init {
    climberPIDController.p = Constants.Climber.CLIMBER_CLIMBERPIDCONTROLLER_P
    climberPIDController.i = Constants.Climber.CLIMBER_CLIMBERPIDCONTROLLER_I
    climberPIDController.d = Constants.Climber.CLIMBER_CLIMBERPIDCONTROLLER_D
    climberPIDController.setSmartMotionMaxVelocity(climberSensor.velocityToRawUnits(0.5.meters.perSecond), Constants.Climber.CLIMBER_PID_SLOTID_SMARTMOTIIONVEL) //what is the slotID (second argument)
    climberPIDController.setSmartMotionMaxAccel(climberSensor.accelerationToRawUnits(0.5.meters.perSecond.perSecond), Constants.Climber.CLIMBER_PID_SLOTID_SMARTMOTIIONACC)
  }

  fun setPosition(position: Constants.ClimberPosition) {
    climberPIDController.setReference(climberSensor.positionToRawUnits(position.length), ControlType.kSmartMotion)
  }

  fun setOpenLoopPower(power: Double) {
    climber.set(power)
  }
}
