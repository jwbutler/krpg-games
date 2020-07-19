package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates

class MoveCommand(override val source: Unit, private val target: Coordinates) : Command
{
    override val type = CommandType.MOVE

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        val direction = Direction.closestBetween(target, source.getCoordinates())
        return Pair(Activity.WALKING, direction)
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