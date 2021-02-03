package com.team4099.lib.units.derived

import com.team4099.lib.units.Fraction
import com.team4099.lib.units.Product
import com.team4099.lib.units.Squared
import com.team4099.lib.units.Value
import com.team4099.lib.units.atto
import com.team4099.lib.units.base.Kilogram
import com.team4099.lib.units.base.Meter
import com.team4099.lib.units.base.Second
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

typealias Newton = Fraction<Product<Kilogram, Meter>, Squared<Second>>

typealias Force = Value<Newton>

val Double.newtons
  get() = Force(this)

val Number.newtons
  get() = Force(this.toDouble())

val Force.inNewtons: Double
  get() = value

val Force.inYottanewtons: Double
  get() = value.yotta

val Force.inZetanewtons: Double
  get() = value.zeta

val Force.inExanewtons: Double
  get() = value.exa

val Force.inPetanewtons: Double
  get() = value.peta

val Force.inTeranewtons: Double
  get() = value.tera

val Force.inGiganewtons: Double
  get() = value.giga

val Force.inMeganewtons: Double
  get() = value.mega

val Force.inKilonewtons: Double
  get() = value.kilo

val Force.inHectonewtons: Double
  get() = value.hecto

val Force.inDecanewtons: Double
  get() = value.deca

val Force.inDecinewtons: Double
  get() = value.deci

val Force.inCentinewtons: Double
  get() = value.centi

val Force.inMillinewtons: Double
  get() = value.milli

val Force.inMicronewtons: Double
  get() = value.micro

val Force.inNanonewtons: Double
  get() = value.nano

val Force.inPiconewtons: Double
  get() = value.pico

val Force.inFemtonewtons: Double
  get() = value.femto

val Force.inAttonewtons: Double
  get() = value.atto

val Force.inZeptonewtons: Double
  get() = value.zepto

val Force.inYoctonewtons: Double
  get() = value.yocto
