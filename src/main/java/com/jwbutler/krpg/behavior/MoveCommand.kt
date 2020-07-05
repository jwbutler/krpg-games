package com.jwbutler.krpg.behavior

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Unit
import com.jwbutler.krpg.geometry.Coordinates

class MoveCommand(private val target: Coordinates) : Command
{
    override fun getType() = CommandType.MOVE

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        if (unit.getCoordinates() == target)
        {
            return Pair(Activity.STANDING, unit.getDirection())
        }

        val direction = unit.getDirection() // TODO
        return Pair(Activity.WALKING, direction)
    }
}