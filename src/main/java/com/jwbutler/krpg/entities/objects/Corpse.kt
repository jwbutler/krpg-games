package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.Sprite

class Corpse(unit: Unit) : AbstractObject(unit.getCoordinates())
{
    private val delegate = unit.sprite
    private val initialCoordinates = unit.getCoordinates()
    override val sprite = object : Sprite
    {
        override fun render(entity: Entity) : Renderable
        {
            return delegate.render(Activity.DEAD, unit.getDirection(), 0, initialCoordinates)
        }
    }

    override fun isBlocking() = false
}