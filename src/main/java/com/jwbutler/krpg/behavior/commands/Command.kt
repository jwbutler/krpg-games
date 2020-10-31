package com.jwbutler.krpg.behavior.commands

import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit

interface Command
{
    val type: CommandType
    val source: Unit
    fun chooseActivity(): Pair<Activity, Direction>
    fun isPreemptible(): Boolean
    fun isComplete(): Boolean
}
