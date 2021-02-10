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

object Ampere : UnitKey

typealias Current = Value<Ampere>

val Double.amps: Current
  get() = Current(this)

val Number.amps: Current
  get() = this.toDouble().amps

val Current.inAmperes
  get() = value

val Current.inYottaamps
  get() = value.yotta

val Current.inZetaamps
  get() = value.zeta

val Current.inExaamps
  get() = value.exa

val Current.inPetaamps
  get() = value.peta

val Current.inTeraamps
  get() = value.tera

val Current.inGigaamps
  get() = value.giga

val Current.inMegaamps
  get() = value.mega

val Current.inKiloamps
  get() = value.kilo

val Current.inHectoamps
  get() = value.hecto

val Current.inDecaamps
  get() = value.deca

val Current.inDeciamps
  get() = value.deci

val Current.inCentiamps
  get() = value.centi

val Current.inMilliamps
  get() = value.milli

val Current.inMicroamps
  get() = value.micro

val Current.inNanoamps
  get() = value.nano

val Current.inPicoamps
  get() = value.pico

val Current.inFemtoamps
  get() = value.femto

val Current.inAttoamps
  get() = value.atto

val Current.inZeptoamps
  get() = value.zepto

val Current.inYoctoamps
  get() = value.yocto
