package com.jwbutler.krpg.utils

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.DirectionalAttackCommand
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.players.HumanPlayer

fun getPlayerUnits(): Collection<Unit>
{
    val humanPlayer = GameState.getInstance()
        .getPlayers()
        .filterIsInstance<HumanPlayer>()
        .firstOrNull()
        ?: error("Could not find human player")
    return humanPlayer.getUnits()
}

fun getEnemyUnit(coordinates: Coordinates): Unit?
{
    return GameState.getInstance()
        .getUnit(coordinates)
        ?.takeUnless { it.getPlayer() is HumanPlayer }
}

fun getTargetedEnemies(units: Collection<Unit>): Collection<Unit>
{
    val targetedEnemies = mutableListOf<Unit>()
    for (unit in units)
    {
        val targetUnit = when (val command = unit.getCommand())
        {
            is AttackCommand -> command.target
            is DirectionalAttackCommand -> getEnemyUnit(command.target)
            else                        -> null
        }

        targetUnit?.let(targetedEnemies::add)
    }
    return targetedEnemies
}