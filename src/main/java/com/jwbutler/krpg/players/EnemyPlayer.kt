package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit

class EnemyPlayer : AbstractPlayer()
{
    override fun chooseCommand(unit: Unit): Command
    {
        val playerUnit = _getPlayerUnit()
        return AttackCommand(unit, playerUnit)
    }

    private fun _getPlayerUnit(): Unit
    {
        return (GameState.getInstance()
            .getPlayers()
            .find { it is HumanPlayer }
            ?.getUnits()
            ?.first()
            ?: error("Could not find player unit"))
    }
}