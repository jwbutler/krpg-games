package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.geometry.GeometryConstants.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GeometryConstants.GAME_WIDTH
import com.jwbutler.rpglib.geometry.Pixel
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.Image
import com.jwbutler.krpg.utils.getPlayerUnits

object HUDRenderer
{
    const val HEIGHT = 40 // If HEIGHT = 180, then this is ~22%
    private const val WIDTH = GAME_WIDTH
    private const val TOP = GAME_HEIGHT - HEIGHT

    private val image = Image.create(WIDTH, HEIGHT)

    fun render(): Pair<Image, Pixel>
    {
        _renderBackground()

        var x = 5
        for (unit in getPlayerUnits())
        {
            val card = UnitCard(unit)
            val cardImage = card.render()
            image.drawImage(cardImage, x, 5)
            x += UnitCard.WIDTH + 5
        }

        return Pair(image, Pixel(0, TOP))
    }

    private fun _renderBackground()
    {
        image.fillRect(0, 0, WIDTH - 1, HEIGHT - 1, Colors.BLACK)
        image.drawRect(0, 0, WIDTH - 1, HEIGHT - 1, Colors.WHITE)
    }
}
