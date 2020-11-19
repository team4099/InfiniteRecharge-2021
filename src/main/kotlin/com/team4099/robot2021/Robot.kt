package com.team4099.robot2021

import com.team4099.robot2021.commands.MoveClimber
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler

object Robot: TimedRobot() {
  init {
    Climber.defaultCommand = MoveClimber(Climber.ClimberPosition.LOW)//change this to set the default position as pneumatic pushed out and motor stopped
    ControlBoard.climberHigh.whileActiveOnce(MoveClimber(Climber.ClimberPosition.HIGH))
    ControlBoard.climberLow.whileActiveOnce(MoveClimber(Climber.ClimberPosition.LOW))
  }
  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()
  }
}
