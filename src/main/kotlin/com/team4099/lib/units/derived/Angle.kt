package com.team4099.lib.units.derived

import com.team4099.lib.units.*
import com.team4099.lib.units.base.Length
import kotlin.math.PI

object Radian : UnitKey

typealias Angle = Value<Radian>

val Double.radians : Angle get() = Angle(this)
val Double.degrees : Angle get() = Angle(Math.toRadians(this))
val Double.rotations : Angle get() = Angle(this * 2 * Math.PI)

val Number.radians : Angle get() = toDouble().radians
val Number.degrees : Angle get() = toDouble().degrees
val Number.rotations : Angle get() = toDouble().rotations

val Angle.inDegrees : Double get() = value
val Angle.inRadians : Double get() = Math.toDegrees(value)
val Angle.inRotations : Double get() = value / (2 * PI)

val Angle.sin : Double get() = kotlin.math.sin(value)
val Angle.cos : Double get() = kotlin.math.cos(value)
val Angle.tan : Double get() = kotlin.math.tan(value)

operator fun AngularVelocity.times(o: Length): LinearVelocity = (o * inRadiansPerSecond).perSecond
