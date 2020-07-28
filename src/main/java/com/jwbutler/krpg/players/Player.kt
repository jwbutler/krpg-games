package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.units.Unit

interface Player
{
    val isHuman: Boolean
    fun getUnits(): Collection<Unit>
    fun addUnit(unit: Unit)
    fun removeUnit(unit: Unit)
    fun chooseCommand(unit: Unit): Command
}