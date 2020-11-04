package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.entities.objects.AbstractObject
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.graphics.sprites.Sprite
import com.jwbutler.rpglib.graphics.sprites.StaticSprite

class Corpse(unit: Unit) : AbstractObject()
{
    override val sprite = _getSprite(unit)

    override fun isBlocking() = false

    private fun _getSprite(unit: Unit): Sprite
    {
        unit.startActivity(RPGActivity.DEAD, unit.getDirection())
        val renderable = unit.render()
        return StaticSprite(
            renderable.image,
            renderable.layer,
            unit.sprite.offsets
        )
    }
}