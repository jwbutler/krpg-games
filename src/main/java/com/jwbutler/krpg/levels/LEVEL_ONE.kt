package com.jwbutler.krpg.levels

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.geometry.Coordinates

val LEVEL_ONE = Level(
    _createTileBox(15, 15),
    _getUnits(),
    _getWalls(),
    Coordinates(0, 0),
    ::_checkVictory
)

private fun _createTileBox(width: Int, height: Int): Map<Coordinates, Tile>
{
    val state = GameState.getInstance()
    val tiles: MutableMap<Coordinates, Tile> = mutableMapOf()
    for (y in (0 until height))
    {
        for (x in (0 until width))
        {
            val coordinates = Coordinates(x, y)
            val tile = Tile(coordinates)
            tiles[coordinates] = tile
            state.addTile(tile, coordinates)
        }
    }
    return tiles.toMap()
}

private fun _getUnits(): Collection<Level.UnitData>
{
    val state = GameState.getInstance()
    val enemyPlayer = state.getPlayers().filter { !it.isHuman }.first()

    return listOf(
        Level.UnitData(
            PlayerUnit(50),
            Coordinates(12, 5),
            enemyPlayer,
            mapOf(EquipmentSlot.MAIN_HAND to Sword())
        ),

        Level.UnitData(
            ZombieUnit(50),
            Coordinates(12, 7),
            enemyPlayer,
            mapOf()
        ),

        Level.UnitData(
            WizardUnit(50),
            Coordinates(8, 9),
            enemyPlayer,
            mapOf()
        )
    )
}

private fun _getWalls(): Map<Coordinates, Collection<GameObject>>
{
    return listOf(8 to 3, 8 to 4, 8 to 5, 8 to 6, 8 to 7)
        .map { (x, y) -> Coordinates(x, y) to listOf(Wall()) }
        .toMap()
}

private fun _checkVictory(): Boolean
{
    val units = GameState.getInstance().getUnits()
    return units.none { !it.getPlayer().isHuman }
}
