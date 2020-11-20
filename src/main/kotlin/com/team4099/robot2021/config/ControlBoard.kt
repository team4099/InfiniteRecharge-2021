package com.team4099.robot2021.config

import com.team4099.lib.joystick.XboxOneGamepad
import edu.wpi.first.wpilibj2.command.button.Trigger

/**
 * Maps buttons on the driver and operator controllers to specific actions
 * with meaningful variable names.
 */
object ControlBoard {
  private val driver = XboxOneGamepad(0)
  private val operator = XboxOneGamepad(1)

  val climberHigh = Trigger { operator.dPadUp }
  val climberLow = Trigger { operator.dPadDown }

  val pneumaticLocked = Trigger { operator.dPadRight }
  val pneumaticUnlocked = Trigger { operator.dPadLeft }
}
