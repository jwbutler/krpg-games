package com.jwbutler.krpg.players

import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit

interface Player
{
    val isHuman: Boolean
    fun getUnits(): List<Unit>
    fun chooseActivity(unit: Unit): Pair<Activity, Direction>
}