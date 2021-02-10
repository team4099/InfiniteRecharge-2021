package com.team4099.lib.units

object Magnitude {
  const val YOTTA = 10E24
  const val ZETA = 10E21
  const val EXA = 10E18
  const val PETA = 10E15
  const val TERA = 10E12
  const val GIGA = 10E9
  const val MEGA = 10E6
  const val KILO = 10E3
  const val HECTO = 10E2
  const val DECA = 10
  const val DECI = 10E-1
  const val CENTI = 10E-2
  const val MILLI = 10E-3
  const val MICRO = 10E-6
  const val NANO = 10E-9
  const val PICO = 10E-12
  const val FEMTO = 10E-15
  const val ATTO = 10E-18
  const val ZEPTO = 10E-21
  const val YOCTO = 10E-24
}

val Double.yotta: Double
  get() = this * Magnitude.YOTTA

val Double.zeta: Double
  get() = this * Magnitude.ZETA

val Double.exa: Double
  get() = this * Magnitude.EXA

val Double.peta: Double
  get() = this * Magnitude.PETA

val Double.tera: Double
  get() = this * Magnitude.TERA

val Double.giga: Double
  get() = this * Magnitude.GIGA

val Double.mega: Double
  get() = this * Magnitude.MEGA

val Double.kilo: Double
  get() = this * Magnitude.KILO

val Double.hecto: Double
  get() = this * Magnitude.HECTO

val Double.deca: Double
  get() = this * Magnitude.DECA

val Double.deci: Double
  get() = this * Magnitude.DECI

val Double.centi: Double
  get() = this * Magnitude.CENTI

val Double.milli: Double
  get() = this * Magnitude.MILLI

val Double.micro: Double
  get() = this * Magnitude.MICRO

val Double.nano: Double
  get() = this * Magnitude.NANO

val Double.pico: Double
  get() = this * Magnitude.PICO

val Double.femto: Double
  get() = this * Magnitude.FEMTO

val Double.atto: Double
  get() = this * Magnitude.ATTO

val Double.zepto: Double
  get() = this * Magnitude.ZEPTO

val Double.yocto: Double
  get() = this * Magnitude.YOCTO

val Number.yotta: Double
  get() = this.toDouble() * Magnitude.YOTTA

val Number.zeta: Double
  get() = this.toDouble() * Magnitude.ZETA

val Number.exa: Double
  get() = this.toDouble() * Magnitude.EXA

val Number.peta: Double
  get() = this.toDouble() * Magnitude.PETA

val Number.tera: Double
  get() = this.toDouble() * Magnitude.TERA

val Number.giga: Double
  get() = this.toDouble() * Magnitude.GIGA

val Number.mega: Double
  get() = this.toDouble() * Magnitude.MEGA

val Number.kilo: Double
  get() = this.toDouble() * Magnitude.KILO

val Number.hecto: Double
  get() = this.toDouble() * Magnitude.HECTO

val Number.deca: Double
  get() = this.toDouble() * Magnitude.DECA

val Number.deci: Double
  get() = this.toDouble() * Magnitude.DECI

val Number.centi: Double
  get() = this.toDouble() * Magnitude.CENTI

val Number.milli: Double
  get() = this.toDouble() * Magnitude.MILLI

val Number.micro: Double
  get() = this.toDouble() * Magnitude.MICRO

val Number.nano: Double
  get() = this.toDouble() * Magnitude.NANO

val Number.pico: Double
  get() = this.toDouble() * Magnitude.PICO

val Number.femto: Double
  get() = this.toDouble() * Magnitude.FEMTO

val Number.atto: Double
  get() = this.toDouble() * Magnitude.ATTO

val Number.zepto: Double
  get() = this.toDouble() * Magnitude.ZEPTO

val Number.yocto: Double
  get() = this.toDouble() * Magnitude.YOCTO
