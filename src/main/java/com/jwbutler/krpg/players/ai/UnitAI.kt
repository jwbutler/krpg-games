package com.jwbutler.krpg.players.ai

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.players.HumanPlayer

enum class UnitAI
{
    NO_OP
    {
        override fun chooseCommand(unit: Unit): Command
        {
            return StayCommand(unit)
        }
    },
    SIMPLE_ATTACK
    {
        override fun chooseCommand(unit: Unit): Command
        {
            val playerUnit = _getPlayerUnit()
            return AttackCommand(unit, playerUnit)
        }
    },
    WANDER
    {
        override fun chooseCommand(unit: Unit): Command
        {
            val state = GameState.getInstance()
            val allCoordinates = state.getAllCoordinates()
            val target = allCoordinates.filter { !it.isBlocked() }.random()
            return MoveCommand(unit, target)
        }
    };

    abstract fun chooseCommand(unit: Unit): Command

    protected fun _getPlayerUnit(): Unit
    {
        return (GameState.getInstance()
            .getPlayers()
            .find { it is HumanPlayer }
            ?.getUnits()
            ?.first()
            ?: error("Could not find player unit"))
    }
}

