package com.team4099.robot2021

import com.team4099.robot2021.commands.MoveClimber
import com.team4099.robot2021.commands.climber.LockClimber
import com.team4099.robot2021.commands.climber.UnlockClimber
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler

object Robot: TimedRobot() {
  init {
    Climber.defaultCommand = LockClimber()
    ControlBoard.climberHigh.whileActiveOnce(MoveClimber(Constants.ClimberPosition.HIGH))
    ControlBoard.climberLow.whileActiveOnce(MoveClimber(Constants.ClimberPosition.LOW))
    ControlBoard.pneumaticLocked.whenActive(LockClimber())
    ControlBoard.pneumaticUnlocked.whenActive(UnlockClimber())
  }
  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()
  }
}
