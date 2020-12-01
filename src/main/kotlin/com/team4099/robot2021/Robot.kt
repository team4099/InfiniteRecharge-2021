package com.team4099.robot2021

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.commands.drivetrain.TeleopDriveCommand
import com.team4099.robot2021.commands.drivetrain.ZeroSensorsCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import kotlin.math.pow

object Robot: TimedRobot() {
  val robotName: Constants.Tuning.RobotName

  init {
    val robotId = Constants.Tuning.ROBOT_ID_PINS.withIndex().map { (i, pin) ->
      if (DigitalInput(pin).get()) 0 else 2.0.pow(i).toInt()
    }.sum()
    robotName = Constants.Tuning.ROBOT_ID_MAP.getOrDefault(robotId, Constants.Tuning.RobotName.COMPETITION)
    Logger.addEvent("Robot", "Robot Construction (running on $robotName)")
    Logger.addSource("Robot", "Battery Voltage", RobotController::getBatteryVoltage)

    Logger.startLogging()
  }

  private val autonomousCommand = InstantCommand()

  override fun autonomousInit() {
    autonomousCommand.schedule()
  }

  override fun teleopInit() {
    autonomousCommand.cancel()
    ZeroSensorsCommand()
  }

  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()
    Logger.saveLogs()
    TeleopDriveCommand({ ControlBoard.strafe }, { ControlBoard.forward }, { ControlBoard.turn })
  }
}
