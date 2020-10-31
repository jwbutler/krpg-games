package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.krpg.utils.getAdjacentUnblockedCoordinates
import com.jwbutler.rpglib.behavior.Activity

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
        if (source.isActivityReady(RPGActivity.RESURRECTING))
        {
            if (target.getCoordinates() == source.getCoordinates())
            {
                startedCasting = true
                return Pair(RPGActivity.RESURRECTING, source.getDirection())
            }
        }
        return null
    }

    private fun _tryWalk(): Pair<Activity, Direction>?
    {
        if (source.isActivityReady(RPGActivity.RESURRECTING))
        {
            if (target.getCoordinates() != source.getCoordinates())
            {
                val direction = Direction.closestBetween(target.getCoordinates(), source.getCoordinates())
                if (
                    !(source.getCoordinates() + direction).isBlocked()
                    && source.isActivityReady(RPGActivity.WALKING)
                )
                {
                    return Pair(RPGActivity.WALKING, direction)
                }
            }
        }
        return null
    }

    private fun _tryWander(): Pair<Activity, Direction>?
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

    private fun _stand() = Pair(RPGActivity.STANDING, source.getDirection())

    override fun isPreemptible() = true
    override fun isComplete() = startedCasting
}
