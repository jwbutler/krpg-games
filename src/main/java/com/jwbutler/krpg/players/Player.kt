package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.units.Unit

interface Player
{
    fun getUnits(): Collection<Unit>
    fun chooseCommand(unit: Unit): Command
    fun addUnit(unit: Unit)
    fun removeUnit(unit: Unit)
}