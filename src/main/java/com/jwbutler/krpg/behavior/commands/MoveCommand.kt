package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates

class MoveCommand(override val source: Unit, private val target: Coordinates) : Command
{
    override val type = CommandType.MOVE
    private var path: List<Coordinates>? = null;

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        return _tryWalk()
            ?: _stand()
    }

    private fun _tryWalk(): Pair<Activity, Direction>?
    {
        val direction = Direction.closestBetween(target, source.getCoordinates())
        if (source.isActivityReady(Activity.WALKING))
        {
            val isBlocked = (source.getCoordinates() + direction).isBlocked()
            if (!isBlocked)
            {
                return Pair(Activity.WALKING, direction)
            }
        }
        return null
    }

    private fun _stand(): Pair<Activity, Direction>
    {
        val direction = Direction.closestBetween(target, source.getCoordinates())
        return Pair(Activity.STANDING, direction)
    }

    override fun isPreemptible() = true

    override fun isComplete(): Boolean
    {
        return source.getCoordinates() == target
    }

    override fun toString(): String
    {
        return "MoveCommand{target=$target}"
    }
}