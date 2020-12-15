package com.team4099.lib.units

import kotlin.math.absoluteValue

inline class Value<T: UnitKey>(internal val value: Double) : Comparable<Value<T>> {
  val absoluteValue: Value<T> get() = Value(value.absoluteValue)

  operator fun plus(o: Value<T>): Value<T> = Value(value + o.value)
  operator fun minus(o: Value<T>): Value<T> = Value(value - o.value)

  operator fun times(k: Double): Value<T> = Value(value * k)
  operator fun times(k: Number): Value<T> = div(k.toDouble())
  operator fun <K: UnitKey> times(o: Value<Fraction<K, T>>): Value<K> = Value(value * o.value)

  operator fun unaryMinus(): Value<T> = Value(-value)

  operator fun div(k: Double): Value<T> = Value(value / k)
  operator fun div(k: Number): Value<T> = div(k.toDouble())
  operator fun div(o: Value<T>): Double = value / o.value

  override operator fun compareTo(o: Value<T>): Int = value.compareTo(o.value)
}
