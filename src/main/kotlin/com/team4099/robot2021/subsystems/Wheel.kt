import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.team4099.lib.units.LinearVelocity
import com.team4099.lib.units.derived.Angle

class Wheel(private val directionSpark: CANSparkMax, private val driveSpark: CANSparkMax, private val driveSetpointMax: LinearVelocity, private val encoder: CANCoder) {
  private var directionPID = directionSpark.pidController
  private var drivePID = driveSpark.pidController


  fun set(direction: Angle, speed: LinearVelocity){
    var
  }

  private fun setSpeed(setpoint: LinearVelocity){
    drivePID.setReference()
  }
}
