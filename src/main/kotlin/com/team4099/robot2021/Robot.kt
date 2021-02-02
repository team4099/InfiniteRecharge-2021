package com.team4099.robot2021

import com.team4099.lib.logging.Logger
import com.team4099.lib.units.base.inches
import com.team4099.lib.units.base.seconds
import com.team4099.robot2021.commands.climber.MoveClimber
import com.team4099.robot2021.commands.climber.LockClimber
import com.team4099.robot2021.commands.climber.UnlockClimber
import com.team4099.robot2021.commands.failures.ValidateCommand
import com.team4099.robot2021.commands.feeder.FeederBeamBreak
import com.team4099.robot2021.commands.feeder.FeederCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.RobotController
import com.team4099.robot2021.loops.FailureManager
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import kotlin.math.pow

object Robot : TimedRobot() {
  val robotName: Constants.Tuning.RobotName

  private val pressureSensor = AnalogInput(Constants.PressureSensor.ANALOG_PIN)

  private val pneumaticsPressure: Double
    get() = 250*(pressureSensor.voltage/Constants.PressureSensor.ANALOG_VOLTAGE) - 25

  init {
    val robotId = Constants.Tuning.ROBOT_ID_PINS.withIndex().map { (i, pin) ->
      if (DigitalInput(pin).get()) 0 else 2.0.pow(i).toInt()
    }.sum()
    robotName = Constants.Tuning.ROBOT_ID_MAP.getOrDefault(robotId, Constants.Tuning.RobotName.COMPETITION)
    Logger.addEvent("Robot", "Robot Construction (running on $robotName)")
    Logger.addSource("Robot", "Battery Voltage", RobotController::getBatteryVoltage)

    Logger.startLogging()

    // Link between feeder Trigger and Command
    Feeder.defaultCommand = FeederCommand(Feeder.FeederState.NEUTRAL)
    ControlBoard.runFeederIn.whileActiveOnce(FeederCommand(Feeder.FeederState.FORWARD_FLOOR))
    ControlBoard.runFeederOut.whileActiveOnce(FeederCommand(Feeder.FeederState.BACKWARD))

    Intake.defaultCommand = IntakeCommand(Constants.Intake.IntakeState.DEFAULT, Constants.Intake.ArmPosition.IN)
    ControlBoard.runIntakeIn.whileActiveContinuous(IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).alongWith(FeederBeamBreak()))
    ControlBoard.runIntakeOut.whileActiveContinuous(IntakeCommand(Constants.Intake.IntakeState.OUT, Constants.Intake.ArmPosition.OUT).alongWith(FeederCommand(Feeder.FeederState.BACKWARD)))

    FailureManager.addFailure(FailureManager.Failures.PRESSURE_LEAK,false) { Constants.PressureSensor.PSI_THRESHOLD > pneumaticsPressure  }
    Logger.addSource("Pressure","Pressure Value"){ pneumaticsPressure }
  }

  private val autonomousCommand = InstantCommand()
  private val testCommand = SequentialCommandGroup(
    MoveClimber(Constants.ClimberPosition.HIGH),
    (ValidateCommand(({1.inches>(Constants.ClimberPosition.HIGH.length - Climber.climberRArmSensor.position).absoluteValue && (1.inches)>(Constants.ClimberPosition.HIGH.length - Climber.climberLArmSensor.position).absoluteValue}),5.seconds,FailureManager.Failures.CLIMBER_FAILED_POS)),
    MoveClimber(Constants.ClimberPosition.LOW), (ValidateCommand(({1.inches<(Constants.ClimberPosition.LOW.length - Climber.climberRArmSensor.position).absoluteValue && (1.inches)<(Constants.ClimberPosition.LOW.length - Climber.climberLArmSensor.position).absoluteValue}),5.seconds,FailureManager.Failures.CLIMBER_FAILED_POS)),
    IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT),
    FeederBeamBreak(),
    IntakeCommand(Constants.Intake.IntakeState.OUT, Constants.Intake.ArmPosition.OUT),
    FeederCommand(Feeder.FeederState.BACKWARD))

  override fun autonomousInit() {
    autonomousCommand.schedule()
  }

  override fun teleopInit() {
    autonomousCommand.cancel()
    Climber.defaultCommand = LockClimber()
    ControlBoard.climberHigh.whileActiveOnce(UnlockClimber().andThen(MoveClimber(Constants.ClimberPosition.HIGH)))
    ControlBoard.climberLow.whileActiveOnce(UnlockClimber().andThen(MoveClimber(Constants.ClimberPosition.LOW)))
  }

  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()
    FailureManager.checkFailures()
    Logger.saveLogs()
  }
}
