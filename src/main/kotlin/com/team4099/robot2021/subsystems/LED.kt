package com.team4099.robot2021.subsystems

import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj2.command.SubsystemBase

object LED: SubsystemBase() {
  private val led = AddressableLED(Constants.LED.PORT)
  private val ledBuffer = AddressableLEDBuffer(Constants.LED.LED_COUNT)
  init {
    led.setLength(ledBuffer.length)
    led.setData(ledBuffer)
    led.start()
  }

  override fun periodic() {

  }
}
