package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.subsystems.Drivetrain
import com.team4099.robot2021.subsystems.Intake
import com.team4099.robot2021.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class EightBallMode : SequentialCommandGroup() {

  init {
    addRequirements(Drivetrain, Intake, Shooter)

    addCommands(
        // ParallelRaceGroup(
        AutoDriveCommand(PathStore.toRendezvousPoint2Balls),
        // IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.OUT)
        // ),
        AutoDriveCommand(PathStore.fromRendezvousPoint2Balls),
        // ShootCommand(),
        // ParallelRaceGroup(
        // SequentialCommandGroup(
        AutoDriveCommand(PathStore.toNearTrench),
        AutoDriveCommand(PathStore.intakeInNearTrench),
        // ),
        // IntakeCommand(Constants.Intake.IntakeState.IN, Constants.Intake.ArmPosition.IN)
        // ),
        // AutoDriveCommand(PathStore.fromRendezvousPoint2Balls) // ,
        AutoDriveCommand(PathStore.fromFarTrench) // ,
    // ShootCommand()
    )
  }
}
