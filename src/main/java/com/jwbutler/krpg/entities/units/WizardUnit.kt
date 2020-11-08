package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.krpg.graphics.sprites.units.WizardSprite
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.graphics.images.PaletteSwaps

private val ACTIVITIES = setOf(
    RPGActivity.APPEARING,
    RPGActivity.FALLING,
    RPGActivity.RESURRECTING,
    RPGActivity.STANDING,
    RPGActivity.WALKING,
    RPGActivity.VANISHING
)

class WizardUnit(hp: Int) : AbstractUnit(hp, ACTIVITIES)
{
    override val sprite = WizardSprite(PaletteSwaps.WHITE_TRANSPARENT)
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            RPGActivity.RESURRECTING -> 50
            RPGActivity.VANISHING    -> 100
            RPGActivity.WALKING      -> 2
            else                  -> 0
        }
    }
}