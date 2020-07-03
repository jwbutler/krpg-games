package com.jwbutler.krpg.behavior

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Unit

interface Command
{
    fun getType(): CommandType
    fun chooseActivity(unit: Unit): Pair<Activity, Direction>
}
