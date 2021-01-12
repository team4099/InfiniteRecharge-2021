package com.team4099.robot2021.auto.modes

import com.team4099.robot2021.auto.PathStore
import com.team4099.robot2021.commands.drivetrain.AutoDriveCommand
import com.team4099.robot2021.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class EightBallMode : SequentialCommandGroup() {

  init {
    addRequirements(Drivetrain, Intake, Shooter)

    addCommands(
      ParallelRaceGroup(
        AutoDriveCommand(PathStore.toRendezvousPoint2Balls),
        IntakeCommand(IntakeState.IN)
      ),

      AutoDriveCommand(PathStore.fromRendezvousPoint2Balls),

      ShootCommand(),

      ParallelRaceGroup(
        SequentialCommandGroup(AutoDriveCommand(PathStore.toNearTrench),AutoDriveCommand(PathStore.intakeInNearTrench)),
        IntakeCommand()
      ),

      AutoDriveCommand(PathStore.fromRendezvousPoint2Balls),

      ShootCommand()

    )
  }
}
