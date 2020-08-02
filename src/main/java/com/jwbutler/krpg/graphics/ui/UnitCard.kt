package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import java.awt.Font
import kotlin.math.round
import kotlin.math.roundToInt

class UnitCard(val unit: Unit)
{
    companion object
    {
        const val WIDTH = 60
        private const val HEIGHT = HUDRenderer.HEIGHT - 10
        private val FONT = Font("Dialog", Font.PLAIN, 10)
    }

    val image = Image(WIDTH, HEIGHT)
    fun render(): Image
    {
        val graphics = image.getGraphics()

        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH - 1, HEIGHT - 1)

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1)
        graphics.font = FONT
        graphics.drawString("Chigz Jupsiz", 3, 12)
        val healthBar = _drawHealthBar(WIDTH - 6, 5)
        healthBar.draw(graphics, 3, 18)
        return image
    }

    private fun _drawHealthBar(width: Int, height: Int): Image
    {
        val image = Image(width, height)
        val graphics = image.getGraphics()

        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH, HEIGHT)

        val fullPercentage = 100.0 * unit.getCurrentHP() / unit.getMaxHP()
        val healthWidth = round((width - 2) * fullPercentage / 100.0).roundToInt()
        val healthHeight = height - 2

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, width - 1, height - 1) // TODO Why do we have to subtract here?
        graphics.color = Colors.GREEN
        graphics.fillRect(1, 1, healthWidth, healthHeight)

        return image
    }
}