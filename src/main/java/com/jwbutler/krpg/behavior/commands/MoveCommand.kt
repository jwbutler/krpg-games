package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Pathfinder

class MoveCommand(override val source: Unit, val target: Coordinates) : Command
{
    override val type = CommandType.MOVE
    private var path: List<Coordinates>? = Pathfinder.findPath(source.getCoordinates(), target)

    override fun chooseActivity(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        return _tryWalk()
            ?: _stand()
    }

    private fun _tryWalk(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>?
    {
        if (source.isActivityReady(RPGActivity.WALKING))
        {
            var next = Pathfinder.findNextCoordinates(path, source.getCoordinates())
            if (next == null)
            {
                path = Pathfinder.findPath(source.getCoordinates(), target)
                next = Pathfinder.findNextCoordinates(path, source.getCoordinates())
            }
            if (next != null)
            {
                val direction = Direction.between(next, source.getCoordinates())
                return Pair(RPGActivity.WALKING, direction)
            }
        }
        return null
    }

    private fun _stand(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        val direction = Direction.closestBetween(target, source.getCoordinates())
        return Pair(RPGActivity.STANDING, direction)
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