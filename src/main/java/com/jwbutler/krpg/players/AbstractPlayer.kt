package com.jwbutler.krpg.players
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.players.Player

abstract class AbstractPlayer : Player
{
    override fun getUnits(): List<Unit> = GameState.getInstance().getUnits(this)
}