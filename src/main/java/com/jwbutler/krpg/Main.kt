package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.MousePlayer

fun main()
{
    val imageLoader = ImageLoader.initialize()
    val state = GameState.initialize()
    val window = GameWindow.initialize()
    val renderer = GameRenderer.initialize()
    val engine = GameEngine.initialize()

    _createTileBox(15, 15)

    val paletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
        .put(Colors.GREEN, Colors.RED)
        .put(Colors.DARK_GREEN, Colors.DARK_RED)

    val humanPlayer = MousePlayer()
    state.addPlayer(humanPlayer)
    //val humanPlayer = KeyboardPlayer()
    val playerUnit = PlayerUnit(200, paletteSwaps)
    state.addUnit(playerUnit, Coordinates(4, 4), humanPlayer)
    playerUnit.addEquipment(Sword())
    playerUnit.addEquipment(Shield())
    playerUnit.addEquipment(MailArmor())
    val playerUnit2 = PlayerUnit(200, paletteSwaps)
    state.addUnit(playerUnit2, Coordinates(4, 6), humanPlayer)
    playerUnit2.addEquipment(Sword())
    playerUnit2.addEquipment(Shield())
    playerUnit2.addEquipment(MailArmor())

    for (coordinates in listOf(8 to 3, 8 to 4, 8 to 5, 8 to 6, 8 to 7).map { (x, y) -> Coordinates(x, y) })
    {
        val wall = Wall()
        state.addObject(wall, coordinates)
    }

    val enemyPlayer = EnemyPlayer()
    state.addPlayer(enemyPlayer)
    val enemyUnit = PlayerUnit(50)
    state.addUnit(enemyUnit, Coordinates(12, 5), enemyPlayer)
    enemyUnit.addEquipment(Sword())
    val enemyZombie = ZombieUnit(50)
    state.addUnit(enemyZombie, Coordinates(12, 7), enemyPlayer)
    val enemyWizard = WizardUnit(50)
    state.addUnit(enemyWizard, Coordinates(8, 9), enemyPlayer)

    engine.start()
}

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