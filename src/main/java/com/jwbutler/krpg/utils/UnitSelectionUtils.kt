package com.jwbutler.krpg.utils

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