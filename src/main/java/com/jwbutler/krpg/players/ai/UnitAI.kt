package com.jwbutler.krpg.players.ai

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.ResurrectCommand
import com.jwbutler.krpg.behavior.commands.SingleAttackCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.behavior.commands.WanderCommand
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.krpg.utils.manhattanDistance
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
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
            val targetUnit = getPlayerUnits()
                .minBy { manhattanDistance(it, unit) }!! // assume player units exist
            return SingleAttackCommand(unit, targetUnit)
        }
    },
    WANDER
    {
        override fun chooseCommand(unit: Unit): Command
        {
            val state = GameState.getInstance()
            val allCoordinates = state.getAllCoordinates()
            val target = allCoordinates.filterNot(Coordinates::isBlocked).random()
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

            if (corpse != null && unit.isActivityReady(RPGActivity.RESURRECTING))
            {
                return ResurrectCommand(unit, corpse)
            }
            return WanderCommand(unit)
        }
    };

    abstract fun chooseCommand(unit: Unit): Command
}

