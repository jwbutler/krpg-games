package com.jwbutler.krpg.players.ai

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.ResurrectCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.behavior.commands.WanderCommand
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.players.HumanPlayer
import kotlin.random.Random

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
    },
    WANDER_SLOW
    {
        override fun chooseCommand(unit: Unit): Command
        {
            if (Random.nextBoolean())
            {
                return WANDER.chooseCommand(unit)
            }
            return NO_OP.chooseCommand(unit)
        }
    },
    WIZARD
    {
        override fun chooseCommand(unit: Unit): Command
        {
            val state = GameState.getInstance()
            val corpse = state.getEntities()
                .filterIsInstance<Corpse>()
                .firstOrNull { it.getCoordinates() == unit.getCoordinates() || !it.getCoordinates().isBlocked() }

            if (corpse != null && unit.isActivityReady(Activity.RESURRECTING))
            {
                return ResurrectCommand(unit, corpse)
            }
            return WanderCommand(unit)
        }
    };

    abstract fun chooseCommand(unit: Unit): Command

    protected fun _getPlayerUnit(): Unit
    {
        return (GameState.getInstance()
            .getPlayers()
            .find { it is HumanPlayer }
            ?.getUnits()
            ?.firstOrNull()
            ?: error("Could not find player unit"))
    }
}

