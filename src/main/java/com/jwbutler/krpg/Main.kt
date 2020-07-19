package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.players.HumanPlayer
import java.lang.Thread.sleep

fun main()
{
    GameState.initialize()
    ImageLoader.initialize()
    GameWindow.initialize()

    val engine = GameEngine(GameState.getInstance(), GameWindow.getInstance())
    val player = HumanPlayer()
    val unit = PlayerUnit(player, Coordinates(2, 4), 100)

    while (true)
    {
        engine.doLoop()
        sleep(100)
    }
}