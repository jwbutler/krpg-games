package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.ZombieSprite
import com.jwbutler.krpg.players.Player
import kotlin.random.Random

private val ACTIVITIES = setOf(
    Activity.ATTACKING,
    Activity.FALLING,
    Activity.STANDING,
    Activity.WALKING
)
class ZombieUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, coordinates, hp, ACTIVITIES)
{
    override val sprite = ZombieSprite(PaletteSwaps.WHITE_TRANSPARENT)
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            Activity.WALKING -> Random.nextInt(1, 11)
            Activity.ATTACKING -> Random.nextInt(1, 11)
            else             -> 0
        }
    }
}