package com.jwbutler.rpglib.players

import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Direction

/**
 * A player is an owner of units, and is responsible for updating their state when they finish an action
 */
interface Player
{
    val isHuman: Boolean
    fun getUnits(): List<Unit>
    fun chooseActivity(unit: Unit): Pair<Activity, Direction>
}