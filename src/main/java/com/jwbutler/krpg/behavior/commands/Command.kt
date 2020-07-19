package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

interface Command
{
    val type: CommandType
    val source: Unit
    fun getActivity(): Pair<Activity, Direction>
    fun isPreemptible(): Boolean
    fun isComplete(): Boolean
}
