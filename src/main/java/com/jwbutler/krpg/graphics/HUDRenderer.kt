package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.utils.getPlayerUnits
import kotlin.math.round
import kotlin.math.roundToInt

private const val WIDTH = GAME_WIDTH
private const val HEIGHT = 40 // If HEIGHT = 180, then this is ~22%
private const val TOP = GAME_HEIGHT - HEIGHT

object HUDRenderer
{
    private val image = Image(WIDTH, HEIGHT)

    fun render(): Pair<Image, Pixel>
    {
        val graphics = image.getGraphics()

        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH - 1, HEIGHT - 1)

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1)
        graphics.drawString("Chigz Jupsiz", 6, 12)
        val healthBar = _drawHealthBar(64, 8)
        healthBar.draw(graphics, 6, 18)
        val pixel = Pixel(0, TOP)
        return Pair(image, pixel)
    }

    private fun _drawHealthBar(width: Int, height: Int): Image
    {
        val image = Image(width, height)
        val graphics = image.getGraphics()

        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH, HEIGHT)

        val playerUnit = getPlayerUnits().first()

        val fullPercentage = 100.0 * playerUnit.getCurrentHP() / playerUnit.getMaxHP()
        val healthWidth = round((width - 2) * fullPercentage / 100.0).roundToInt()
        val healthHeight = height - 2

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, width - 1, height - 1) // TODO Why do we have to subtract here?
        graphics.color = Colors.GREEN
        graphics.fillRect(1, 1, healthWidth, healthHeight)

        return image
    }
}
