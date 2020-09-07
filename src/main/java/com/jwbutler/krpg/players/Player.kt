package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

interface Player
{
    val isHuman: Boolean
    fun getUnits(): List<Unit>
    fun chooseActivity(unit: Unit): Pair<Activity, Direction>
}