package com.jwbutler.krpg.utils

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.geometry.Rectangle
import com.jwbutler.krpg.geometry.TILE_HEIGHT
import com.jwbutler.krpg.geometry.TILE_WIDTH
import com.jwbutler.krpg.players.HumanPlayer
import com.jwbutler.krpg.players.Player

fun getPlayerUnits(): List<Unit>
{
    val humanPlayer = GameState.getInstance()
        .getPlayers()
        .filterIsInstance<HumanPlayer>()
        .firstOrNull()
        ?: error("Could not find human player")
    return humanPlayer.getUnits() // TODO: Sorting?
}

fun getEnemyUnits(): Collection<Unit>
{
    return GameState.getInstance()
        .getPlayers()
        .filterNot { it is HumanPlayer }
        .flatMap(Player::getUnits)
}

fun getUnitsInPixelRect(rect: Rectangle): Collection<Unit>
{
    val selectedUnits = mutableListOf<Unit>()
    val allUnits = GameState.getInstance().getUnits()
    for (unit in allUnits)
    {
        val (x, y) = unit.getCoordinates().toPixel() + IntPair.of(TILE_WIDTH / 2, TILE_HEIGHT / 2)
        if (rect.contains(x, y))
        {
            selectedUnits.add(unit)
        }
    }
    return selectedUnits
}