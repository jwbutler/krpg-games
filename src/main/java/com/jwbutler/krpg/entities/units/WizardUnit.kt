package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.WizardSprite
import com.jwbutler.krpg.players.Player
import kotlin.random.Random

private val ACTIVITIES = setOf(
    Activity.APPEARING,
    Activity.FALLING,
    Activity.RESURRECTING,
    Activity.STANDING,
    Activity.WALKING,
    Activity.VANISHING
)

class WizardUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, coordinates, hp, ACTIVITIES)
{
    override val sprite = WizardSprite(PaletteSwaps.WHITE_TRANSPARENT)
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            Activity.RESURRECTING -> 50
            Activity.VANISHING    -> 100
            Activity.WALKING      -> Random.nextInt(1, 6)
            else                  -> 0
        }
    }
}