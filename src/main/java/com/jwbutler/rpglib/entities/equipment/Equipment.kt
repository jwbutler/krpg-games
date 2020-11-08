package com.jwbutler.rpglib.entities.equipment

import com.jwbutler.rpglib.entities.objects.GameObject
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Direction

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