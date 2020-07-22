package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.entities.units.Unit

class ResurrectCommand(override val source: Unit, private val target: Corpse) : Command
{
    override val type = CommandType.RESURRECT
    private var startedCasting = false

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        if (target.getCoordinates() == source.getCoordinates())
        {
            startedCasting = true
            return Pair(Activity.RESURRECTING, source.getDirection())
        }
        else
        {
            val direction = Direction.closestBetween(target.getCoordinates(), source.getCoordinates())
            if (!(source.getCoordinates() + direction).isBlocked())
            {
                return Pair(Activity.WALKING, direction)
            }
        }
        return Pair(Activity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = startedCasting
}
