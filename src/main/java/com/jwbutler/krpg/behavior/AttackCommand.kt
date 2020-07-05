package com.jwbutler.krpg.behavior

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Unit
import kotlin.math.abs

class AttackCommand(private val target: Unit) : Command
{
    override fun getType() = CommandType.ATTACK

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        if (isInRange(unit, target))
        {
            val direction = unit.getDirection() // TODO
            return Pair(Activity.ATTACKING, direction)
        }
        else
        {
            val direction = unit.getDirection() // TODO
            return Pair(Activity.WALKING, direction)
        }
    }

    private fun isInRange(unit: Unit, target: Unit): Boolean
    {
        val dx = unit.getCoordinates().x - target.getCoordinates().x
        val dy = unit.getCoordinates().y - target.getCoordinates().y
        return abs(dx) <= 1 && abs(dy) <= 1
    }
}