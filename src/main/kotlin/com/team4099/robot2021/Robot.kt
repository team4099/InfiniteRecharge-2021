package com.team4099.robot2021

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.commands.MoveClimber
import com.team4099.robot2021.commands.climber.LockClimber
import com.team4099.robot2021.commands.climber.UnlockClimber
import com.team4099.robot2021.commands.shooter.ShooterIdleCommand
import com.team4099.robot2021.commands.feeder.FeederBeamBreak
import com.team4099.robot2021.commands.feeder.FeederCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.commands.shooter.ShootCommand
import com.team4099.robot2021.commands.shooter.SpinUpCommand
import com.team4099.robot2021.commands.shooter.VisionCommand
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import com.team4099.robot2021.commands.drivetrain.TeleopDriveCommand
import com.team4099.robot2021.commands.drivetrain.ZeroSensorsCommand
import com.team4099.robot2021.subsystems.Climber
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
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
    Feeder.defaultCommand = FeederCommand(Feeder.FeederState.NEUTRAL)
    ControlBoard.runFeederIn.whileActiveOnce(FeederCommand(Feeder.FeederState.FORWARD_FLOOR));
    ControlBoard.runFeederOut.whileActiveOnce(FeederCommand(Feeder.FeederState.BACKWARD));

    Intake.defaultCommand = IntakeCommand(Constants.Intake.IntakeState.DEFAULT, Constants.Intake.ArmPosition.IN)
    ControlBoard.runIntakeIn.whileActiveContinuous(IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT).alongWith(FeederBeamBreak()));
    ControlBoard.runIntakeOut.whileActiveContinuous(IntakeCommand(Constants.Intake.IntakeState.OUT, Constants.Intake.ArmPosition.OUT).alongWith(FeederCommand(Feeder.FeederState.BACKWARD)));
    
    Climber.defaultCommand = LockClimber()
    ControlBoard.climberHigh.whileActiveOnce(UnlockClimber().andThen(MoveClimber(Constants.ClimberPosition.HIGH)))
    ControlBoard.climberLow.whileActiveOnce(UnlockClimber().andThen(MoveClimber(Constants.ClimberPosition.LOW)))

    Shooter.defaultCommand = ShooterIdleCommand()
    ControlBoard.shoot.whenActive(ParallelCommandGroup(ShootCommand(), VisionCommand()))
    ControlBoard.stopShooting.whenActive(ShooterIdleCommand())

    ControlBoard.spinUpShooter.whenActive(SpinUpCommand(true))
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
