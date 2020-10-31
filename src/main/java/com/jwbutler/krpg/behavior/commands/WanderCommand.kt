package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.krpg.utils.getAdjacentUnblockedCoordinates

class WanderCommand(override val source: Unit) : Command
{
    override val type = CommandType.WANDER

    override fun chooseActivity(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        return _tryWander()
            ?: _stand()
    }

    private fun _tryWander(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>?
    {
        if (source.isActivityReady(RPGActivity.WALKING))
        {
            val adjacentCoordinates = getAdjacentUnblockedCoordinates(source.getCoordinates())
            if (adjacentCoordinates.isNotEmpty())
            {
                val coordinates = adjacentCoordinates.random()
                return Pair(RPGActivity.WALKING, Direction.between(coordinates, source.getCoordinates()))
            }
        }
        return null
    }

    private fun _stand(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        return Pair(RPGActivity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = true
}