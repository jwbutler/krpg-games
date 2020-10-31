package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates

class DirectionalAttackCommand(override val source: Unit, val target: Coordinates) : Command
{
    override val type = CommandType.ATTACK

    private var hasAttacked = false

    override fun chooseActivity(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        val direction = Direction.closestBetween(target, source.getCoordinates())
        if (!hasAttacked && source.isActivityReady(RPGActivity.ATTACKING))
        {
            hasAttacked = true
            return Pair(RPGActivity.ATTACKING, direction)
        }
        else
        {
            return Pair(RPGActivity.STANDING, direction)
        }
    }

    override fun isPreemptible() = true
    override fun isComplete() = false

    override fun toString(): String
    {
        return "AttackCommand{target=$target}"
    }
}