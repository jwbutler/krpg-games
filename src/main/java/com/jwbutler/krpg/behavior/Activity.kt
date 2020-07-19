package com.jwbutler.krpg.behavior
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates

enum class Activity
{
    STANDING,
    WALKING
    {
        override fun onComplete(unit: Unit)
        {
            val x = unit.getCoordinates().x + unit.getDirection().dx
            val y = unit.getCoordinates().y + unit.getDirection().dy
            unit.moveTo(Coordinates(x, y))
        }
    },
    ATTACKING,
    FALLING;

    override fun toString() = name.toLowerCase()
    open fun onComplete(unit: Unit) {}
}