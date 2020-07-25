package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.GeometryUtils

class ResurrectCommand(override val source: Unit, private val target: Corpse) : Command
{
    override val type = CommandType.RESURRECT
    private var startedCasting = false

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        return _tryResurrect()
            ?: _tryWalk()
            ?: _tryWander()
            ?: _stand()
    }

    private fun _tryResurrect(): Pair<Activity, Direction>?
    {
        if (source.isActivityReady(Activity.RESURRECTING))
        {
            if (target.getCoordinates() == source.getCoordinates())
            {
                startedCasting = true
                return Pair(Activity.RESURRECTING, source.getDirection())
            }
        }
        return null
    }

    private fun _tryWalk(): Pair<Activity, Direction>?
    {
        if (source.isActivityReady(Activity.RESURRECTING))
        {
            if (target.getCoordinates() != source.getCoordinates())
            {
                val direction = Direction.closestBetween(target.getCoordinates(), source.getCoordinates())
                if (
                    !(source.getCoordinates() + direction).isBlocked()
                    && source.isActivityReady(Activity.WALKING)
                )
                {
                    return Pair(Activity.WALKING, direction)
                }
            }
        }
        return null
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

    private fun _stand() = Pair(Activity.STANDING, source.getDirection())

    override fun isPreemptible() = true
    override fun isComplete() = startedCasting
}
