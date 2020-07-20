package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates

class DirectionalAttackCommand(override val source: Unit, private val target: Coordinates) : Command
{
    override val type = CommandType.ATTACK
    private var hasAttacked = false

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        val direction = Direction.closestBetween(target, source.getCoordinates())
        if (!hasAttacked)
        {
            hasAttacked = true
            return Pair(Activity.ATTACKING, direction)
        }
        else
        {
            return Pair(Activity.STANDING, direction)
        }
    }

    override fun isPreemptible() = true
    override fun isComplete() = false

    override fun toString(): String
    {
        return "AttackCommand{target=$target}"
    }
}