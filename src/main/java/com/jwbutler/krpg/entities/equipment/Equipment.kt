package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.units.Unit

interface Equipment : Entity
{
    val slot: EquipmentSlot
    fun getUnit(): Unit
    fun setUnit(unit: Unit)
}