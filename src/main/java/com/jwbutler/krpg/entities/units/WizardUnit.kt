package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.WizardSprite

private val ACTIVITIES = setOf(
    Activity.APPEARING,
    Activity.FALLING,
    Activity.RESURRECTING,
    Activity.STANDING,
    Activity.WALKING,
    Activity.VANISHING
)

class WizardUnit(hp: Int) : AbstractUnit(hp, ACTIVITIES)
{
    override val sprite = WizardSprite(PaletteSwaps.WHITE_TRANSPARENT)
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            Activity.RESURRECTING -> 50
            Activity.VANISHING    -> 100
            Activity.WALKING      -> 2
            else                  -> 0
        }
    }
}