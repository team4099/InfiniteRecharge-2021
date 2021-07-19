package com.team4099.robot2021.config

import com.team4099.lib.joystick.XboxOneGamepad
import edu.wpi.first.wpilibj2.command.button.Trigger

/**
 * Maps buttons on the driver and operator controllers to specific actions with meaningful variable
 * names.
 */
object ControlBoard {
  private val driver = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
  private val operator = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

  val strafe: Double
    get() = -driver.leftXAxis

  val forward: Double
    get() = driver.leftYAxis

  val turn: Double
    get() = driver.rightXAxis

  val resetGyro = Trigger { driver.startButton && driver.selectButton }

  // what
  val sampleClimberVelocity: Double
    get() = operator.leftTriggerAxis - operator.rightTriggerAxis

  val wristVertical: Boolean
    get() = operator.leftShoulderButton

  val wristHorizontal: Boolean
    get() = operator.rightShoulderButton

  val enableVisionAlignment: Boolean
    get() = driver.aButton

  val runIntakeIn = Trigger { operator.aButton }
  val putIntakeUp = Trigger { operator.bButton }
  val runIntakeOut = Trigger { operator.xButton }

  // val unjamThroughIntake = Trigger { operator.dPadDown }
  // val unjamThroughShooter = Trigger { operator.dPadUp }
  val runFeederIn = Trigger { operator.dPadUp }
  val runFeederOut = Trigger { operator.dPadDown }

  // val unjam = Trigger { operator.dPadDown }

  val shoot = Trigger { operator.yButton }
  val nearSpin = Trigger { operator.rightShoulderButton }
  val farSpin = Trigger { operator.leftShoulderButton }
  val visionButton = Trigger { driver.aButton }

  val climberHigh = Trigger { operator.dPadRight }
  val climberLow = Trigger { operator.dPadLeft }

  // Infinite Recharge @ Home
  //  val nearSpin = Trigger { operator.aButton }
  //  val lineSpin = Trigger { operator.bButton }
  //  val midSpin = Trigger { operator.yButton }
  //  val farSpin = Trigger { operator.xButton }
}
