package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit

class StayCommand(override val source: Unit) : Command
{
    override val type = CommandType.STAY

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        return Pair(RPGActivity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = true

    override fun toString(): String
    {
        return "StayCommand{}"
    }
}