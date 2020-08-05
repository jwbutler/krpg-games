package com.jwbutler.krpg

import com.jwbutler.krpg.core.EditorEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.ui.EditorRenderer
import com.jwbutler.krpg.graphics.ui.GameWindow
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.players.EditorPlayer

fun main()
{
    ImageLoader.initialize()

    val mainWindow = GameWindow.initialize()
    val state = GameState.initialize()
    state.setTiles(_tileBox(5, 5))
    EditorPlayer()

    val mainRenderer = EditorRenderer(mainWindow)
    val engine = EditorEngine(mainRenderer)

    engine.start()
}

private fun _tileBox(width: Int, height: Int): Map<Coordinates, Tile>
{
    val tiles: MutableMap<Coordinates, Tile> = mutableMapOf()
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