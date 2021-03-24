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

  val sampleClimberVelocity: Double
    get() = operator.leftTriggerAxis - operator.rightTriggerAxis

  // val wristVertical: Boolean
  //  get() = operator.leftShoulderButton

  // val wristHorizontal: Boolean
  //  get() = operator.rightShoulderButton

  // val enableVisionAlignment: Boolean
  //  get() = driver.aButton

  val runIntakeIn = Trigger { operator.aButton }
  val runIntakeOut = Trigger { operator.bButton }

  // val slowMode: Boolean
  // get() = driver.dPadDown

  val runFeederIn = Trigger { operator.dPadDown }
  val runFeederOut = Trigger { operator.dPadUp }

  val shoot = Trigger { operator.xButton }
  val stopShooting = Trigger { operator.yButton }
  val spinUpShooter = Trigger { operator.dPadRight }
  val visionButton = Trigger { driver.aButton }

  // val climberHigh = Trigger { driver.dPadUp }
  // val climberLow = Trigger { driver.dPadDown }
}
