package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

interface Player
{
    val isHuman: Boolean
    fun getUnits(): List<Unit>
    /**
     * Decide on the next activity for each unit controlled by this player, for this turn only.
     * This is usually a no-op unless a unit's animation has just completed.
     */
    fun update()
    fun getNextActivity(unit: Unit): Pair<Activity, Direction>
}