package com.jwbutler.krpg.behavior

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Unit
import com.jwbutler.krpg.geometry.Coordinates

/**
 * Used by the Wizard.  Teleport if the cooldown is ready, otherwise just walk.
 */
class TeleportingMoveCommand(val target: Coordinates) : Command
{
    override fun getType() = CommandType.MOVE

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        val direction = unit.getDirection() // TODO
        return Pair(Activity.STANDING, direction)
    }
}