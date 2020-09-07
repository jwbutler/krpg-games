package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.graphics.sprites.Sprite
import com.jwbutler.krpg.graphics.sprites.StaticSprite

class Corpse(unit: Unit) : AbstractObject()
{
    override val sprite = _getSprite(unit)

    override fun isBlocking() = false

    private fun _getSprite(unit: Unit): Sprite
    {
        unit.startActivity(Activity.DEAD, unit.getDirection())
        val renderable = unit.render()
        return StaticSprite(renderable.image, renderable.layer, unit.sprite.offsets)
    }
}