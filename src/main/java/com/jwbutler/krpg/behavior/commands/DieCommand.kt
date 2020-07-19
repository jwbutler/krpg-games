package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

/**
 * for the player sprite that only has two directions
 */
class DieCommand(override val source: Unit) : Command
{
    override val type = CommandType.DIE
    var hasFallen = false

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        val direction = when (source.getDirection())
        {
            Direction.N, Direction.NE, Direction.E, Direction.SE -> Direction.NE
            else -> Direction.S
        }

        if (!hasFallen)
        {
            hasFallen = true
            return Pair(Activity.FALLING, direction)
        }
        else
        {
            return Pair(Activity.DEAD, direction)
        }
    }

    override fun isPreemptible() = false
    override fun isComplete() = false // TODO corpses

    override fun toString(): String
    {
        return "DieCommand{}"
    }
}