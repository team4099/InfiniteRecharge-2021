package com.team4099.robot2021.commands.drivetrain

import edu.wpi.first.wpilibj2.command.CommandBase
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.config.Constants

class TeleopDriveCommand(
  val driveX: () -> Double,
  val driveY: () -> Double,
  val turn: () -> Double) : CommandBase() {

  init{
    addRequirements(Drivetrain)
  }

  override fun initialize(){
    // Zero Gyros?
  }
  override fun execute(){
    val speed = Pair(Constants.Drivetrain.DRIVE_SETPOINT_MAX * driveX(), Constants.Drivetrain.DRIVE_SETPOINT_MAX * driveY())
    val direction = Constants.Drivetrain.TURN_SETPOINT_MAX * turn()
    Drivetrain.set(direction,speed)
  }
  override fun isFinished() : Boolean {
    return false
  }


}
