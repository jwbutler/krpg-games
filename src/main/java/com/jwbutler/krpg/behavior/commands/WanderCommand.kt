package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.GeometryUtils

class WanderCommand(override val source: Unit) : Command
{
    override val type = CommandType.WANDER

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        return _tryWander()
            ?: _stand()
    }

    private fun _tryWander(): Pair<Activity, Direction>?
    {
        if (source.isActivityReady(Activity.WALKING))
        {
            val adjacentCoordinates = GeometryUtils.getAdjacentUnblockedCoordinates(source.getCoordinates())
            if (adjacentCoordinates.isNotEmpty())
            {
                val coordinates = adjacentCoordinates.random()
                return Pair(Activity.WALKING, Direction.between(coordinates, source.getCoordinates()))
            }
        }
        return null
    }

    private fun _stand(): Pair<Activity, Direction>
    {
        return Pair(Activity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = true
}