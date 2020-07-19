package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

/**
 * for the player sprite that only has three directions
 */
class DieCommand(override val source: Unit) : Command
{
    override val type = CommandType.DIE

    override fun getActivity(): Pair<Activity, Direction>
    {
        val direction = when (source.getDirection())
        {
            Direction.NE, Direction.E               -> Direction.NE
            Direction.SE, Direction.S, Direction.SW -> Direction.S
            Direction.W, Direction.NW, Direction.N  -> Direction.NW
        }

        return Pair(Activity.FALLING, direction)
    }

    override fun isPreemptible() = false
    override fun isComplete() = true

    override fun toString(): String
    {
        return "DieCommand{}"
    }
}