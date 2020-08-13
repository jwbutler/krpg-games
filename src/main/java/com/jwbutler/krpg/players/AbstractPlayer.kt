package com.jwbutler.krpg.players
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit

abstract class AbstractPlayer : Player
{
    override fun getUnits(): List<Unit> = GameState.getInstance().getUnits(this)
}