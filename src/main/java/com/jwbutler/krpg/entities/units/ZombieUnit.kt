package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.ZombieSprite
import com.jwbutler.krpg.players.Player
import kotlin.random.Random

private fun _getSprite() = ZombieSprite(PaletteSwaps.WHITE_TRANSPARENT)

private val ACTIVITIES = setOf(Activity.STANDING, Activity.WALKING, Activity.ATTACKING, Activity.FALLING);

class ZombieUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp, ACTIVITIES)
{
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            Activity.WALKING -> Random.nextInt(1, 11)
            else             -> 0
        }
    }
}