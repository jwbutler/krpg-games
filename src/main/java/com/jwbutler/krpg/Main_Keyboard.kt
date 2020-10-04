package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.tiles.TileType
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.geometry.Dimensions
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.levels.BitmapLevelCreator
import com.jwbutler.krpg.levels.Level
import com.jwbutler.krpg.levels.VictoryCondition
import com.jwbutler.krpg.levels.generation.LevelGenerator
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.KeyboardPlayer

fun main()
{
    val imageLoader = ImageLoader.initialize()
    val state = GameState.initialize()
    val renderer = GameRenderer.initialize()
    val window = GameWindow.initialize()
    val engine = GameEngine.initialize()

    val humanPlayer = KeyboardPlayer()
    state.addPlayer(humanPlayer)
    val enemyPlayer = EnemyPlayer()
    state.addPlayer(enemyPlayer)

    /*val level = BitmapLevelCreator.loadLevel(
        filename = "levelA",
        baseTileType = TileType.STONE,
        victoryCondition = VictoryCondition.NONE
    )*/

    val level = LevelGenerator.create()
        .generate(Dimensions(40, 40), VictoryCondition.NONE)
    engine.startGame(level, _getInitialUnits())
}

private fun _getInitialUnits(): List<GameEngine.UnitData>
{
    val state = GameState.getInstance()
    val humanPlayer = state.getHumanPlayer()

    val paletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
        .put(Colors.GREEN, Colors.RED)
        .put(Colors.DARK_GREEN, Colors.DARK_RED)

    return listOf(
        GameEngine.UnitData(
            PlayerUnit(200, paletteSwaps),
            mapOf(
                EquipmentSlot.MAIN_HAND to Sword(),
                EquipmentSlot.OFF_HAND to Shield(),
                EquipmentSlot.CHEST to MailArmor()
            )
        )
    )
}