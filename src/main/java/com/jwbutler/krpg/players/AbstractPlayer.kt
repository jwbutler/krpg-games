package com.jwbutler.krpg.players
import com.jwbutler.krpg.entities.units.Unit

abstract class AbstractPlayer : Player
{
    private val units = mutableListOf<Unit>()

    override fun getUnits(): Collection<Unit> = units

    override fun addUnit(unit: Unit)
    {
        check(!units.contains(unit))
        units.add(unit)
    }

    override fun removeUnit(unit: Unit)
    {
        check(units.contains(unit))
        units.remove(unit)
    }
}