package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.levels.BitmapLevelCreator
import com.jwbutler.krpg.levels.LEVEL_ONE
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.MousePlayer

fun main()
{
    val imageLoader = ImageLoader.initialize()
    val state = GameState.initialize()
    val window = GameWindow.initialize()
    val renderer = GameRenderer.initialize()
    val engine = GameEngine.initialize()

    val humanPlayer = MousePlayer()
    state.addPlayer(humanPlayer)
    val enemyPlayer = EnemyPlayer()
    state.addPlayer(enemyPlayer)

    engine.startGame(LEVEL_ONE, _getInitialUnits())
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
        ),
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