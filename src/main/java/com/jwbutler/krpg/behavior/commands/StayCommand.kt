package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

class StayCommand(override val source: Unit) : Command
{
    override val type = CommandType.STAY

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        return Pair(Activity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = true

    override fun toString(): String
    {
        return "StayCommand{}"
    }
}