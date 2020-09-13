package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.utils.getPlayerUnits

object HUDRenderer
{
    const val HEIGHT = 90 // 25% of screen height
    private val WIDTH = GameRenderer.getInstance().gameWidth
    private val TOP = GameRenderer.getInstance().gameHeight - HEIGHT

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
