package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.ZombieSprite
import com.jwbutler.krpg.players.Player
import kotlin.random.Random

private fun _getSprite() = ZombieSprite(PaletteSwaps.WHITE_TRANSPARENT)

class ZombieUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp)
{
    override fun onActivityComplete(activity: Activity)
    {
        if (activity == Activity.WALKING)
        {
            if (Random.nextBoolean())
            {
                val waitDuration = Random.nextInt(1, 11)
                triggerCooldown(activity, waitDuration)
            }
        }
    }
}