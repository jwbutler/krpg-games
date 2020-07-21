package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.HumanPlayer
import java.lang.Thread.sleep

fun main()
{
    GameState.initialize()
    ImageLoader.initialize()
    GameWindow.initialize()

    val state = GameState.initialize()
    state.setTiles(_tileBox(20, 20))
    val engine = GameEngine(state, GameWindow.getInstance())

    val paletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
        .put(Colors.GREEN, Colors.RED)
        .put(Colors.DARK_GREEN, Colors.DARK_RED)

    val humanPlayer = HumanPlayer()
    val playerUnit = PlayerUnit(humanPlayer, Coordinates(2, 4), 1000, paletteSwaps)
    playerUnit.addEquipment(Sword())
    playerUnit.addEquipment(Shield())
    playerUnit.addEquipment(MailArmor())

    val enemyPlayer = EnemyPlayer()
    val enemyUnit = PlayerUnit(enemyPlayer, Coordinates(5, 5), 50)
    enemyUnit.addEquipment(Sword())
    val enemyZombie = ZombieUnit(enemyPlayer, Coordinates(5, 7), 50)
    val enemyWizard = WizardUnit(enemyPlayer, Coordinates(0, 0), 1)

    while (true)
    {
        engine.doLoop()
        sleep(100)
    }
}

fun _tileBox(width: Int, height: Int): Map<Coordinates, Tile?>
{
    val tiles: MutableMap<Coordinates, Tile?> = mutableMapOf()
    for (y in (0 until height))
    {
        for (x in (0 until width))
        {
            val coordinates = Coordinates(x, y)
            tiles.put(coordinates, Tile(coordinates))
        }
    }
    return tiles.toMap()
}
