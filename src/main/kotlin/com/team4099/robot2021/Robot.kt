package com.team4099.robot2021

import com.team4099.lib.logging.Logger
import com.team4099.lib.smoothDeadband
import com.team4099.robot2021.auto.modes2021.AvoidBarCircularMode
import com.team4099.robot2021.commands.drivetrain.OpenLoopDriveCommand
import com.team4099.robot2021.commands.drivetrain.ResetGyroCommand
import com.team4099.robot2021.commands.feeder.FeederCommand
import com.team4099.robot2021.commands.feeder.FeederSerialize
import com.team4099.robot2021.commands.intake.IntakeCommand
import com.team4099.robot2021.commands.shooter.ShootCommand
import com.team4099.robot2021.commands.shooter.ShooterIdleCommand
import com.team4099.robot2021.commands.shooter.SpinUpCommand
import com.team4099.robot2021.commands.shooter.UnjamCommand
import com.team4099.robot2021.commands.shooter.VisionCommand
import com.team4099.robot2021.config.Constants
import com.team4099.robot2021.config.ControlBoard
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Feeder
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import com.team4099.robot2021.subsystems.Vision
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import kotlin.math.pow

object Robot : TimedRobot() {
  val robotName: Constants.Tuning.RobotName

  init {
    val robotId =
        Constants.Tuning
            .ROBOT_ID_PINS
            .withIndex()
            .map { (i, pin) -> if (DigitalInput(pin).get()) 0 else 2.0.pow(i).toInt() }
            .sum()
    robotName =
        Constants.Tuning.ROBOT_ID_MAP.getOrDefault(robotId, Constants.Tuning.RobotName.COMPETITION)
    Logger.addEvent("Robot", "Robot Construction (running on $robotName)")
    Logger.addSource("Robot", "Battery Voltage", RobotController::getBatteryVoltage)

    Logger.startLogging()

    // Link between feeder Trigger and Command
    // Feeder.defaultCommand = FeederSerialize()
    Feeder.defaultCommand = FeederCommand(Feeder.FeederState.NEUTRAL)

    ControlBoard.runFeederIn.whileActiveOnce(FeederCommand(Feeder.FeederState.FORWARD_ALL))
    ControlBoard.runFeederOut.whileActiveOnce(FeederCommand(Feeder.FeederState.BACKWARD))

    Intake.defaultCommand =
        IntakeCommand(Constants.Intake.IntakeState.IDLE, Constants.Intake.ArmPosition.OUT)
    ControlBoard.runIntakeIn
        .whileActiveContinuous(FeederSerialize())
        .whileActiveContinuous(
            IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT))
    ControlBoard.putIntakeUp
        .whileActiveContinuous(
            IntakeCommand(Constants.Intake.IntakeState.IDLE, Constants.Intake.ArmPosition.IN))
    ControlBoard.runIntakeOut
        .whileActiveContinuous(
            IntakeCommand(Constants.Intake.IntakeState.OUT, Constants.Intake.ArmPosition.OUT)
        // .alongWith(FeederCommand(Feeder.FeederState.BACKWARD))
        )

    // Climber.defaultCommand = LockClimber()
    // ControlBoard.climberHigh
    // .whileActiveOnce(UnlockClimber().andThen(MoveClimber(Constants.ClimberPosition.HIGH)))
    // ControlBoard.climberLow
    // .whileActiveOnce(UnlockClimber().andThen(MoveClimber(Constants.ClimberPosition.LOW)))

    Shooter.defaultCommand = ShooterIdleCommand()
    //    ControlBoard.shoot.whenActive(ParallelCommandGroup(ShootCommand(), VisionCommand()))
    //    ControlBoard.shoot.whileActiveOnce(VisionCommand().andThen(ShootCommand()))
    ControlBoard.shoot.whileActiveOnce(ShootCommand())
    //    ControlBoard.shoot.whileActiveOnce(VisionCommand())
    //    ControlBoard.stopShooting.whenActive(ShooterIdleCommand())
    //    ControlBoard.spinUpShooter.whenActive(SpinUpCommand(true))
    ControlBoard.nearSpin
        .whileActiveContinuous(SpinUpCommand(false, true, Vision.DistanceState.NEAR))
    ControlBoard.farSpin.whileActiveContinuous(SpinUpCommand(false, true, Vision.DistanceState.FAR))
    ControlBoard.unjam.whileActiveContinuous(UnjamCommand())

    ControlBoard.visionButton.whileActiveOnce(VisionCommand())

    Drivetrain.defaultCommand =
        OpenLoopDriveCommand(
            { ControlBoard.strafe.smoothDeadband(Constants.Joysticks.THROTTLE_DEADBAND) },
            { ControlBoard.forward.smoothDeadband(Constants.Joysticks.THROTTLE_DEADBAND) },
            { ControlBoard.turn.smoothDeadband(Constants.Joysticks.TURN_DEADBAND) })

    // BallVision

    ControlBoard.resetGyro.whileActiveOnce(ResetGyroCommand())

    //    ControlBoard.nearSpin
    //        .whileActiveOnce(SpinUpCommand(accuracy = true, distance = Vision.DistanceState.NEAR))
    //    ControlBoard.lineSpin
    //        .whileActiveOnce(SpinUpCommand(accuracy = true, distance = Vision.DistanceState.LINE))
    //    ControlBoard.midSpin
    //        .whileActiveOnce(SpinUpCommand(accuracy = true, distance = Vision.DistanceState.MID))
    //    ControlBoard.farSpin
    //        .whileActiveOnce(SpinUpCommand(accuracy = true, distance = Vision.DistanceState.FAR))
  }

  // private val autonomousCommand = GalacticSearch() // ResetZeroCommand()

  // private val autonomousCommand = AutoDriveCommand(PathStore.galacticSearchBRed)
  // private val autonomousCommand = AutoNavBounceMode()
  // private val autonomousCommand = AutoDriveCommand(PathStore.barrelPath)

  // private val autonomousCommand = DriveCharacterizeCommand()
  // private val autonomousCommand = LoopPathCommand(PathStore.driveForward,
  // PathStore.driveBackwards)

  // private val autonomousCommand = EightBallMode()
  private val autonomousCommand = AvoidBarCircularMode()

  override fun autonomousInit() {
    Drivetrain.zeroSensors()
    autonomousCommand.schedule()
  }

  override fun disabledInit() {
    autonomousCommand.cancel()
  }

  override fun teleopInit() {
    Drivetrain.zeroDirection()
    autonomousCommand.cancel()
  }

  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()
    Logger.saveLogs()
    Logger.updateShuffleboard()
  }
}
