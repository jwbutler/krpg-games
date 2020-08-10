package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.units.Unit

interface Player
{
    val isHuman: Boolean
    fun getUnits(): List<Unit>
    fun chooseCommand(unit: Unit): Command
}