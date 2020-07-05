package com.jwbutler.krpg.behavior

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Unit

/**
 * for the player sprite that only has three directions
 */
class ThreeDirectionDieCommand : Command
{
    override fun getType() = CommandType.DIE

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        val direction = when (unit.getDirection())
        {
            Direction.NE, Direction.E               -> Direction.NE
            Direction.SE, Direction.S, Direction.SW -> Direction.S
            Direction.W, Direction.NW, Direction.N  -> Direction.NW
        }

        return Pair(Activity.FALLING, direction)
    }
}