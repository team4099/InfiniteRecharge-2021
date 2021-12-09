package com.team4099.robot2021.subsystems

import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj2.command.SubsystemBase

object PowerDistributionPanel : SubsystemBase() {
  val PDP: PowerDistributionPanel = PowerDistributionPanel(Constants.PowerDistributionPanel.PDP_ID)

}
