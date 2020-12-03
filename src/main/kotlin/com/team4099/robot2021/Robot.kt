package com.team4099.robot2021

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.commands.feeder.BeamBreak
import com.team4099.robot2021.commands.feeder.FeederCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.config.subsystems.Feeder
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.subsystems.Intake
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import kotlin.math.pow

object Robot : TimedRobot() {
  val robotName: Constants.Tuning.RobotName

  init {
    val robotId = Constants.Tuning.ROBOT_ID_PINS.withIndex().map { (i, pin) ->
      if (DigitalInput(pin).get()) 0 else 2.0.pow(i).toInt()
    }.sum()
    robotName = Constants.Tuning.ROBOT_ID_MAP.getOrDefault(robotId, Constants.Tuning.RobotName.COMPETITION)
    Logger.addEvent("Robot", "Robot Construction (running on $robotName)")
    Logger.addSource("Robot", "Battery Voltage", RobotController::getBatteryVoltage)

    Logger.startLogging()

    // Link between feeder Trigger and Command
    ControlBoard.runFeederIn.whileActiveOnce(FeederCommand(Feeder.FeederState.FORWARD_FLOOR));
    ControlBoard.runFeederOut.whileActiveOnce(FeederCommand(Feeder.FeederState.BACKWARD));

    Intake.defaultCommand = IntakeCommand(Constants.Intake.IntakeState.DEFAULT, Constants.Intake.ArmPosition.IN)
    ControlBoard.runIntakeIn.whileActiveContinuous(IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).alongWith(BeamBreak()));
    ControlBoard.runIntakeOut.whileActiveContinuous(IntakeCommand(Constants.Intake.IntakeState.OUT, Constants.Intake.ArmPosition.OUT).alongWith(FeederCommand(Feeder.FeederState.BACKWARD)));
  }

  private val autonomousCommand = InstantCommand()

  override fun autonomousInit() {
    autonomousCommand.schedule()
  }

  override fun teleopInit() {
    autonomousCommand.cancel()
  }

  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()
    Logger.saveLogs()
  }
}
