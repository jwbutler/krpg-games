package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.players.HumanPlayer
import java.awt.Font
import java.awt.font.TextAttribute
import kotlin.math.roundToInt

class UnitCard(val unit: Unit)
{
    companion object
    {
        const val WIDTH = 100
        private const val HEIGHT = HUDRenderer.HEIGHT - 10
        //private val FONT = Font(Font.MONOSPACED, Font.PLAIN, 12)
        private val FONT = Font.createFont(
            Font.TRUETYPE_FONT,
            //UnitCard::class.java.getResourceAsStream("/fonts/Perfect DOS VGA 437.ttf")
            UnitCard::class.java.getResourceAsStream("/fonts/Apple ][.ttf")
        ).deriveFont(mapOf(
            TextAttribute.SIZE to 8.0,
            TextAttribute.TRACKING to -0.2F,
            TextAttribute.KERNING to 0
        ))
    }

    val image = Image.create(WIDTH, HEIGHT)

    fun render(): Image
    {
        val color = if (_isSelected(unit)) Colors.DARK_GREEN else Colors.DARK_GRAY
        image.fillRect(0, 0, WIDTH - 1, HEIGHT - 1, color)
        image.drawRect(0, 0, WIDTH - 1, HEIGHT - 1, Colors.WHITE)
        image.drawText("Chigz Jupsiz", FONT, 3, 12)
        val healthBar = _drawHealthBar(WIDTH - 8, 5)
        image.drawImage(healthBar, 4, 18)
        return image
    }

    private fun _isSelected(unit: Unit): Boolean
    {
        check(unit.getPlayer() is HumanPlayer)
        return (unit.getPlayer() as HumanPlayer).getSelectedUnits().contains(unit)
    }

    private fun _drawHealthBar(width: Int, height: Int): Image
    {
        val healthBarImage = Image.create(width, height)
        healthBarImage.fillRect(0, 0, WIDTH, HEIGHT, Colors.BLACK)

        val fullPercentage = 100.0 * unit.getCurrentHP() / unit.getMaxHP()
        val healthWidth = ((width - 2) * fullPercentage / 100.0).roundToInt()
        val healthHeight = height - 2

        healthBarImage.drawRect(0, 0, width - 1, height - 1, Colors.WHITE) // TODO Why do we have to subtract here?
        healthBarImage.fillRect(1, 1, healthWidth, healthHeight, Colors.GREEN)

        return healthBarImage
    }
}