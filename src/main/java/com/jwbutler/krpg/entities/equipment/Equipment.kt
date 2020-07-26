package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit

interface Equipment : GameObject
{
    val slot: EquipmentSlot
    fun getUnit(): Unit?
    fun setUnit(unit: Unit?)
    var direction: Direction?

    override fun isBlocking() = false
}