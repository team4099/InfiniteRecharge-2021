package com.team4099.robot2021.subsystems

import com.team4099.robot2021.config.Constants
import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj2.command.SubsystemBase

object LED: SubsystemBase() {
  //using https://www.amazon.com/ALITOVE-Individually-Addressable-Flexible-Waterproof/dp/B018X04ES2

  private val led = AddressableLED(Constants.LED.PORT)
  private val ledBuffer = AddressableLEDBuffer(Constants.LED.LED_COUNT)
  private var rainbowFirstPixelHue = 0

  init {
    led.setLength(ledBuffer.length)
    led.setData(ledBuffer)
    led.start()

  }

  override fun periodic() {

  }

  private fun rainbow() {
    // For every pixel
    for (i in 0 until ledBuffer.getLength()) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      val hue = (rainbowFirstPixelHue + i * 180 / ledBuffer.getLength()) % 180
      // Set the value
      ledBuffer.setHSV(i, hue, 255, 128)
    }
    // Increase by to make the rainbow "move"
    rainbowFirstPixelHue += 3
    // Check bounds
    rainbowFirstPixelHue %= 180
  }
}
