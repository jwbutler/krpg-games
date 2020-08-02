package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.utils.getPlayerUnits
import java.awt.Graphics2D

object HUDRenderer
{
    const val HEIGHT = 40 // If HEIGHT = 180, then this is ~22%
    private const val WIDTH = GAME_WIDTH
    private const val TOP = GAME_HEIGHT - HEIGHT

    private val image = Image(WIDTH, HEIGHT)

    fun render(): Pair<Image, Pixel>
    {
        val graphics = image.getGraphics()
        _renderBackground(graphics)

        var x = 5
        for (unit in getPlayerUnits())
        {
            val card = UnitCard(unit)
            val image = card.render()

            image.draw(graphics, x, 5)
            x += UnitCard.WIDTH + 5
        }

        return Pair(image, Pixel(0, TOP))
    }

    private fun _renderBackground(graphics: Graphics2D)
    {
        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH - 1, HEIGHT - 1)

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1)
    }
}
