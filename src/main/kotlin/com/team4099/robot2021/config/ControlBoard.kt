package com.team4099.robot2021.config

import com.team4099.lib.around
import com.team4099.lib.joystick.XboxOneGamepad
import edu.wpi.first.wpilibj2.command.button.Trigger

/**
 * Maps buttons on the driver and operator controllers to specific actions with meaningful variable
 * names.
 */
object ControlBoard {
  private val driver = XboxOneGamepad(Constants.Joysticks.DRIVER_PORT)
  private val operator = XboxOneGamepad(Constants.Joysticks.SHOTGUN_PORT)
  private val technician = XboxOneGamepad(Constants.Joysticks.TECHNICIAN_PORT)

  val strafe: Double
    get() = -driver.leftXAxis

  val forward: Double
    get() = driver.leftYAxis

  val turn: Double
    get() = driver.rightXAxis

  val resetGyro = Trigger { driver.startButton && driver.selectButton }

  // what
  val wristVertical: Boolean
    get() = operator.leftShoulderButton

  val wristHorizontal: Boolean
    get() = operator.rightShoulderButton

  val enableVisionAlignment: Boolean
    get() = driver.aButton

  val runIntakeIn = Trigger { operator.aButton }
  val putIntakeUp = Trigger { operator.bButton }
  val runIntakeOut = Trigger { operator.xButton }

  // val runFeederIn = Trigger { operator.yButton }
  val runFeederIn = Trigger { operator.dPadUp }
  val runFeederOut = Trigger { operator.dPadDown }

  val shoot = Trigger { operator.yButton }
  // val shoot = Trigger { technician.yButton }
  val nearSpin = Trigger { operator.rightShoulderButton }
  val farSpin = Trigger { operator.leftShoulderButton }
  val visionButton = Trigger { driver.aButton }

  val prepareClimb = Trigger { operator.startButton }
  val unjam = Trigger { operator.selectButton }

  val climbPower: Double
    get() = operator.rightTriggerAxis - operator.leftTriggerAxis

  val moveClimber = Trigger { !climbPower.around(0.0, 0.1) }
  // val climberHigh = Trigger { operator.dPadRight }
  // val climberLow = Trigger { operator.dPadLeft }
  val lockClimber = Trigger { technician.dPadRight }
  val unlockClimber = Trigger { technician.dPadLeft }

  val spoolLeftClimber = Trigger { technician.leftShoulderButton }
  val spoolRightClimber = Trigger { technician.rightShoulderButton }

  // Infinite Recharge @ Home
  //  val nearSpin = Trigger { operator.aButton }
  //  val lineSpin = Trigger { operator.bButton }
  //  val midSpin = Trigger { operator.yButton }
  //  val farSpin = Trigger { operator.xButton }
}
