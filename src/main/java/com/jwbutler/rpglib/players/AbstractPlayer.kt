package com.jwbutler.rpglib.players
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.units.Unit

abstract class AbstractPlayer : Player
{
    override fun getUnits(): List<Unit> = GameState.getInstance().getUnits(this)
}