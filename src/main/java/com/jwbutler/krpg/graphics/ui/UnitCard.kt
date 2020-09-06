package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.players.HumanPlayer
import java.awt.Font
import kotlin.math.roundToInt

class UnitCard(val unit: Unit)
{
    companion object
    {
        const val WIDTH = 60
        private const val HEIGHT = HUDRenderer.HEIGHT - 10
        private val FONT = Font(Font.DIALOG, Font.PLAIN, 10)
    }

    val image = Image(WIDTH, HEIGHT)

    fun render(): Image
    {
        val color = if (_isSelected(unit)) Colors.DARK_GREEN else Colors.DARK_GRAY
        image.fillRect(0, 0, WIDTH - 1, HEIGHT - 1, color)
        image.drawRect(0, 0, WIDTH - 1, HEIGHT - 1, Colors.WHITE)
        image.drawText("Chigz Jupsiz", FONT, 3, 12)
        val healthBar = _drawHealthBar(WIDTH - 6, 5)
        image.drawImage(healthBar, 3, 18)
        return image
    }

    private fun _isSelected(unit: Unit): Boolean
    {
        check(unit.getPlayer() is HumanPlayer)
        return (unit.getPlayer() as HumanPlayer).getSelectedUnits().contains(unit)
    }

    private fun _drawHealthBar(width: Int, height: Int): Image
    {
        val image = Image(width, height)
        image.fillRect(0, 0, WIDTH, HEIGHT, Colors.BLACK)

        val fullPercentage = 100.0 * unit.getCurrentHP() / unit.getMaxHP()
        val healthWidth = ((width - 2) * fullPercentage / 100.0).roundToInt()
        val healthHeight = height - 2

        image.drawRect(0, 0, width - 1, height - 1, Colors.WHITE) // TODO Why do we have to subtract here?
        image.fillRect(1, 1, healthWidth, healthHeight, Colors.GREEN)

        return image
    }
}