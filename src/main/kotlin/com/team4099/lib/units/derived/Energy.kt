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

typealias Joule = Fraction<Product<Kilogram, Squared<Meter>>, Squared<Second>>

typealias Energy = Value<Newton>

val Double.joules
  get() = Energy(this)

val Number.joules
  get() = Energy(this.toDouble())

val Force.inJoules: Double
  get() = value

val Force.inYottajoules: Double
  get() = value.yotta

val Force.inZetajoules: Double
  get() = value.zeta

val Force.inExajoules: Double
  get() = value.exa

val Force.inPetajoules: Double
  get() = value.peta

val Force.inTerajoules: Double
  get() = value.tera

val Force.inGigajoules: Double
  get() = value.giga

val Force.inMegajoules: Double
  get() = value.mega

val Force.inKilojoules: Double
  get() = value.kilo

val Force.inHectojoules: Double
  get() = value.hecto

val Force.inDecajoules: Double
  get() = value.deca

val Force.inDecijoules: Double
  get() = value.deci

val Force.inCentijoules: Double
  get() = value.centi

val Force.inMillijoules: Double
  get() = value.milli

val Force.inMicrojoules: Double
  get() = value.micro

val Force.inNanojoules: Double
  get() = value.nano

val Force.inPicojoules: Double
  get() = value.pico

val Force.inFemtojoules: Double
  get() = value.femto

val Force.inAttojoules: Double
  get() = value.atto

val Force.inZeptojoules: Double
  get() = value.zepto

val Force.inYoctojoules: Double
  get() = value.yocto
