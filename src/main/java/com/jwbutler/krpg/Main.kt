package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
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

    val state = GameState.initialize()
    state.setTiles(_tileBox(20, 20))
    val engine = GameEngine(state, GameWindow.getInstance())
    val player = HumanPlayer()

    val unit = PlayerUnit(player, Coordinates(2, 4), 100)

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
