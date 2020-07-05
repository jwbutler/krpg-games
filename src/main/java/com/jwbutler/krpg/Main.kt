package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.PlayerUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.ImageLoader
import java.lang.Thread.sleep

fun main()
{
    GameState.initialize()
    ImageLoader.initialize()
    GameWindow.initialize()

    val engine = GameEngine(GameState.getInstance(), GameWindow.getInstance())
    val unit = PlayerUnit(Coordinates(2, 4), 100)
    engine.doLoop()
    sleep(1000)
    unit.moveTo(Coordinates(3, 5))
    engine.doLoop()
    sleep(1000)
    unit.moveTo(Coordinates(4, 6))
    engine.doLoop()
    sleep(1000)
}