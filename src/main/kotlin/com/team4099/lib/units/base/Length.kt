package com.team4099.lib.units.base

import com.team4099.lib.units.UnitKey
import com.team4099.lib.units.Value
import com.team4099.lib.units.atto
import com.team4099.lib.units.centi
import com.team4099.lib.units.deca
import com.team4099.lib.units.deci
import com.team4099.lib.units.exa
import com.team4099.lib.units.femto
import com.team4099.lib.units.giga
import com.team4099.lib.units.hecto
import com.team4099.lib.units.kilo
import com.team4099.lib.units.mega
import com.team4099.lib.units.micro
import com.team4099.lib.units.milli
import com.team4099.lib.units.nano
import com.team4099.lib.units.peta
import com.team4099.lib.units.pico
import com.team4099.lib.units.tera
import com.team4099.lib.units.yocto
import com.team4099.lib.units.yotta
import com.team4099.lib.units.zepto
import com.team4099.lib.units.zeta

object Meter : UnitKey

typealias Length = Value<Meter>

internal const val METERS_PER_INCH = 0.0254

internal const val METERS_PER_THOU = METERS_PER_INCH / 1000

internal const val METERS_PER_FOOT = METERS_PER_INCH * 12

val Double.meters: Length
  get() = Length(this)

val Double.inches: Length
  get() = Length(this * METERS_PER_INCH)

val Double.thou: Length
  get() = Length(this * METERS_PER_THOU)

val Double.feet: Length
  get() = Length(this * METERS_PER_FOOT)

val Number.meters: Length
  get() = toDouble().meters

val Number.inches: Length
  get() = toDouble().inches

val Number.thou: Length
  get() = toDouble().thou

val Number.feet: Length
  get() = toDouble().feet

val Length.inMeters: Double
  get() = value

val Length.inInches: Double
  get() = value / METERS_PER_INCH

val Length.inThou: Double
  get() = value / METERS_PER_THOU

val Length.inFeet: Double
  get() = value / METERS_PER_FOOT

val Length.inYottameters: Double
  get() = value.yotta

val Length.inZetameters: Double
  get() = value.zeta

val Length.inExameters: Double
  get() = value.exa

val Length.inPetameters: Double
  get() = value.peta

val Length.inTerameters: Double
  get() = value.tera

val Length.inGigameters: Double
  get() = value.giga

val Length.inMegameters: Double
  get() = value.mega

val Length.inKilometers: Double
  get() = value.kilo

val Length.inHectometers: Double
  get() = value.hecto

val Length.inDecameters: Double
  get() = value.deca

val Length.inDecimeters: Double
  get() = value.deci

val Length.inCentimeters: Double
  get() = value.centi

val Length.inMillimeters: Double
  get() = value.milli

val Length.inMicrometers: Double
  get() = value.micro

val Length.inNanometers: Double
  get() = value.nano

val Length.inPicometers: Double
  get() = value.pico

val Length.inFemtometers: Double
  get() = value.femto

val Length.inAttometers: Double
  get() = value.atto

val Length.inZeptometers: Double
  get() = value.zepto

val Length.inYoctometers: Double
  get() = value.yocto
