package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit

/**
 * Equipment are a special type of entity: they can either be worn by a unit, or free-standing at
 * particular coordinates.
 * TODO - differentiate between fallen equipment and standing equipment
 */
interface Equipment : GameObject
{
    val slot: EquipmentSlot
    fun getUnit(): Unit?
    var direction: Direction?

    override fun isBlocking() = false
}