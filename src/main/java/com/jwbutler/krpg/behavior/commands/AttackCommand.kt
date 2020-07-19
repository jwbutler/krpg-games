package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import kotlin.math.abs

class AttackCommand(override val source: Unit, private val target: Unit) : Command
{
    override val type = CommandType.ATTACK
    private var hasAttacked = false

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        if (isInRange(source.getCoordinates(), target.getCoordinates()))
        {
            val direction = Direction.between(target.getCoordinates(), source.getCoordinates())
            hasAttacked = true
            return Pair(Activity.ATTACKING, direction)
        }
        else
        {
            val direction = Direction.closestBetween(target.getCoordinates(), source.getCoordinates())
            return Pair(Activity.WALKING, direction)
        }
    }

    override fun isPreemptible() = true
    override fun isComplete() = hasAttacked

    private fun isInRange(first: Coordinates, second: Coordinates): Boolean
    {
        val dx = second.x - first.x
        val dy = second.y - first.y
        return (abs(dx) <= 1 && abs(dy) <= 1)
    }

    override fun toString(): String
    {
        return "AttackCommand{target=$target}"
    }
}