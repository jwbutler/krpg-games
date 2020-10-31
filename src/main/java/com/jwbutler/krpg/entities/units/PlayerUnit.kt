package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite

private val ACTIVITIES = setOf(
    RPGActivity.ATTACKING,
    RPGActivity.BASHING,
    RPGActivity.FALLING,
    RPGActivity.STANDING,
    RPGActivity.WALKING
)

class PlayerUnit
(
    hp: Int,
    paletteSwaps: PaletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
) : AbstractUnit(hp, ACTIVITIES)
{
    override val sprite = PlayerSprite(paletteSwaps)
    override fun getCooldown(activity: Activity): Int
    {
        return when(activity)
        {
            RPGActivity.BASHING -> 20
            else -> 0
        }
    }
}