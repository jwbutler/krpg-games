package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.WizardSprite
import com.jwbutler.krpg.players.Player

private fun _getSprite() = WizardSprite(PaletteSwaps.WHITE_TRANSPARENT)

private val ACTIVITIES = setOf(
    Activity.APPEARING,
    Activity.FALLING,
    Activity.RESURRECTING,
    Activity.STANDING,
    Activity.WALKING,
    Activity.VANISHING
)

class WizardUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp, ACTIVITIES)
{
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            Activity.RESURRECTING -> 50
            Activity.VANISHING    -> 100
            else                  -> 0
        }
    }
}