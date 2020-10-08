package com.team4099.lib.units.derived

import com.team4099.lib.units.*

typealias ElectricalPotentialPerVelocity<T> = Value<Fraction<Volt, Velocity<T>>>
typealias ElectricalPotentialPerAcceleration<T> = Value<Fraction<Volt, Acceleration<T>>>

operator fun <T: UnitKey> ElectricalPotential.div(o: Value<T>): Value<Fraction<Volt, T>> =
  Value(value / o.value)
