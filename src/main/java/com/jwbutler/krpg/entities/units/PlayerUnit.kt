package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite

private val ACTIVITIES = setOf(
    Activity.ATTACKING,
    Activity.BASHING,
    Activity.FALLING,
    Activity.STANDING,
    Activity.WALKING
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
            Activity.ATTACKING -> 2
            Activity.BASHING -> 20
            else -> 0
        }
    }
}