package com.team4099.lib.units.derived

import com.team4099.lib.units.Product
import com.team4099.lib.units.Value
import com.team4099.lib.units.atto
import com.team4099.lib.units.base.Ampere
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

typealias Coulomb = Product<Ampere, Second>

typealias Charge = Value<Coulomb>

val Double.coulombs
  get() = Charge(this)

val Number.coulombs
  get() = Charge(this.toDouble())

val Force.inCoulombs: Double
  get() = value

val Force.inYottacoulombs: Double
  get() = value.yotta

val Force.inZetacoulombs: Double
  get() = value.zeta

val Force.inExacoulombs: Double
  get() = value.exa

val Force.inPetacoulombs: Double
  get() = value.peta

val Force.inTeracoulombs: Double
  get() = value.tera

val Force.inGigacoulombs: Double
  get() = value.giga

val Force.inMegacoulombs: Double
  get() = value.mega

val Force.inKilocoulombs: Double
  get() = value.kilo

val Force.inHectocoulombs: Double
  get() = value.hecto

val Force.inDecacoulombs: Double
  get() = value.deca

val Force.inDecicoulombs: Double
  get() = value.deci

val Force.inCenticoulombs: Double
  get() = value.centi

val Force.inMillicoulombs: Double
  get() = value.milli

val Force.inMicrocoulombs: Double
  get() = value.micro

val Force.inNanocoulombs: Double
  get() = value.nano

val Force.inPicocoulombs: Double
  get() = value.pico

val Force.inFemtocoulombs: Double
  get() = value.femto

val Force.inAttocoulombs: Double
  get() = value.atto

val Force.inZeptocoulombs: Double
  get() = value.zepto

val Force.inYoctocoulombs: Double
  get() = value.yocto
