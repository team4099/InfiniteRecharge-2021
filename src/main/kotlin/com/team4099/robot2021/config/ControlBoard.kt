package com.team4099.robot2021.config

import com.team4099.lib.joystick.XboxOneGamepad
import edu.wpi.first.wpilibj2.command.button.Trigger

/**
 * Maps buttons on the driver and operator controllers to specific actions
 * with meaningful variable names.
 */
object ControlBoard {
  private val driver = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
  private val operator = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)

  val strafe: Double
    get() = driver.leftXAxis

  val forward: Double
    get() = driver.leftYAxis

  val turn: Double
    get() = driver.rightXAxis

  val sampleClimberVelocity: Double
    get() = operator.leftTriggerAxis - operator.rightTriggerAxis

  val wristVertical: Boolean
    get() = operator.leftShoulderButton

  val wristHorizontal: Boolean
    get() = operator.rightShoulderButton

  val enableVisionAlignment: Boolean
    get() = driver.aButton

  val startShooter: Boolean
    get() = operator.xButton

  val stopShooter: Boolean
    get() = operator.yButton

  val climberUp: Boolean
    get() = driver.dPadUp

  val climberDown: Boolean
    get() = driver.dPadDown

  val runIntakeIn = Trigger {operator.aButton}

  val runIntakeOut = Trigger { operator.bButton}

  val slowMode: Boolean
    get() = driver.dPadDown

  val runFeederIn = Trigger{ operator.dPadDown};
  val runFeederOut = Trigger{ operator.dPadUp};
  
  val climberHigh = Trigger { driver.dPadUp }
  val climberLow = Trigger { driver.dPadDown }
}
