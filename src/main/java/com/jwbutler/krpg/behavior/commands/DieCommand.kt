package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit

class DieCommand(override val source: Unit) : Command
{
    override val type = CommandType.DIE
    private var hasFallen = false

    override fun chooseActivity(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        if (!hasFallen)
        {
            hasFallen = true
            return Pair(RPGActivity.FALLING, source.getDirection())
        }
        else
        {
            error("Shouldn't get here!")
            return Pair(RPGActivity.DEAD, source.getDirection())
        }
    }

    override fun isPreemptible() = false
    override fun isComplete() = false

    override fun toString(): String
    {
        return "DieCommand{}"
    }
}