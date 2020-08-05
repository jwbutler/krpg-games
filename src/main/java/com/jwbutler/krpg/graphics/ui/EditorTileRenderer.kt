package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image

private const val WIDTH = GAME_WIDTH
private const val HEIGHT = 40

class EditorTileRenderer
{
    private val tiles = listOf(Tile(Coordinates(0, 0)))
    private val units = listOf(PlayerUnit(_getPlayer(), Coordinates(0, 0), 100))

    fun render(): Image
    {
        val image = Image(WIDTH, HEIGHT)
        image.fillRect(0, 0, WIDTH, HEIGHT, Colors.DARK_GRAY)
        var x = 3
        val y = 3
        for (tile in tiles)
        {
            val renderable = tile.render()
            image.drawImage(renderable.image, x, y)
            x += 30
        }
        return image
    }

    private fun _getPlayer() = GameState.getInstance().getPlayers().first()
}