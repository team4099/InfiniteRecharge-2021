package com.team4099.robot2021.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.team4099.lib.logging.Logger
import com.team4099.lib.units.ctreAngularMechanismSensor
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.simulation.FlywheelSim
import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.system.plant.DCMotor
import edu.wpi.first.wpilibj.RobotController
import com.team4099.lib.units.derived.rotations
import com.team4099.lib.units.inRotationsPerMinute
import com.team4099.lib.units.perMinute

object Intake : SubsystemBase() {
  private val simulated = Mechanism2D()

  private val intakeTalon = WPI_TalonSRX(Constants.Intake.INTAKE_MOTOR)
  private val intakeDoubleSolenoid =
      DoubleSolenoid(Constants.Intake.ARM_SOLENOID_FORWARD, Constants.Intake.ARM_SOLENOID_REVERSE)

  private lateinit var simPhysics: FlywheelSim
  private val intakeSensor = ctreAngularMechanismSensor(intakeTalon, 2048, Constants.Intake.GEAR_RATIO)

  var intakeState = Constants.Intake.IntakeState.DEFAULT
    set(value) {
      intakeTalon.set(ControlMode.PercentOutput, value.speed)
      field = value
    }

  var armState = Constants.Intake.ArmPosition.DEFAULT
    set(value) {
      intakeDoubleSolenoid.set(value.position)
      field = value
    }

  init {
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

    if (RobotBase.isSimulation()) {
      simPhysics = FlywheelSim(DCMotor.getFalcon500(1), Constants.Intake.GEAR_RATIO, Constants.Intake.INTAKE_MOI)
    }
  }

  override fun simulationPeriodic() {
    println("Intake commanded output: ${intakeTalon.motorOutputPercent} voltage: ${RobotController.getBatteryVoltage()}")
    println("Intake raw velocity: ${intakeSensor.velocityToRawUnits(simPhysics.angularVelocityRPM.rotations.perMinute)}")

    simPhysics.setInput(intakeTalon.motorOutputPercent * RobotController.getBatteryVoltage())
    simPhysics.update(0.02)

    intakeTalon.simCollection.setQuadratureVelocity(intakeSensor.velocityToRawUnits(simPhysics.angularVelocityRPM.rotations.perMinute).toInt())
    intakeTalon.simCollection.addQuadraturePosition(intakeSensor.velocityToRawUnits(simPhysics.angularVelocityRPM.rotations.perMinute * 0.02).toInt())
    intakeTalon.simCollection.setBusVoltage(12.0)
  }
}
