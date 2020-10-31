package com.jwbutler.krpg.utils

import com.jwbutler.krpg.geometry.GeometryConstants.TILE_HEIGHT
import com.jwbutler.krpg.geometry.GeometryConstants.TILE_WIDTH
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.IntPair
import com.jwbutler.rpglib.geometry.Rectangle
import com.jwbutler.krpg.players.HumanPlayer
import com.jwbutler.rpglib.players.Player

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