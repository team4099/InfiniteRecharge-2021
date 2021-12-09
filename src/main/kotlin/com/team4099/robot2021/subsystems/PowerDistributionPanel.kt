package com.team4099.robot2021.subsystems

import com.team4099.lib.logging.Logger
import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj2.command.SubsystemBase

object PowerDistributionPanel : SubsystemBase() {
  val PDP: PowerDistributionPanel = PowerDistributionPanel(Constants.PowerDistributionPanel.PDP_ID)

  init {
    Logger.addSource("Power Distribution Panel", "PDP Power") { PDP.totalPower }
    Logger.addSource("Power Distribution Panel", "PDP Current") { PDP.totalCurrent }
    Logger.addSource("Power Distribution Panel", "Wheel One Direction") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_ONE_DIRECTION)
    }
    Logger.addSource("Power Distribution Panel", "Wheel One Drive") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_ONE_DRIVE)
    }
    Logger.addSource("Power Distribution Panel", "Wheel Two Direction") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_TWO_DIRECTION)
    }
    Logger.addSource("Power Distribution Panel", "Wheel Two Drive") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_TWO_DRIVE)
    }
    Logger.addSource("Power Distribution Panel", "Wheel Three Direction") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_THREE_DIRECTION)
    }
    Logger.addSource("Power Distribution Panel", "Wheel Three Drive") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_THREE_DRIVE)
    }
    Logger.addSource("Power Distribution Panel", "Wheel Four Direction") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_FOUR_DIRECTION)
    }
    Logger.addSource("Power Distribution Panel", "Wheel Four Drive") {
      PDP.getCurrent(Constants.PowerDistributionPanel.WHEEL_FOUR_DRIVE)
    }
  }
}
