package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import com.jwbutler.krpg.players.Player

private val ACTIVITIES = setOf(
    Activity.ATTACKING,
    Activity.FALLING,
    Activity.STANDING,
    Activity.WALKING
)

class PlayerUnit
(
    player: Player,
    coordinates: Coordinates,
    hp: Int,
    paletteSwaps: PaletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
) : AbstractUnit(player, coordinates, hp, ACTIVITIES)
{
    override val sprite = PlayerSprite(paletteSwaps)
    override fun getCooldown(activity: Activity): Int
    {
        return when(activity)
        {
            Activity.ATTACKING -> 2
            else -> 0
        }
    }
}