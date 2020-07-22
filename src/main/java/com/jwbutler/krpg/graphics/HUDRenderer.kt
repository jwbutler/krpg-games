package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.players.HumanPlayer
import com.jwbutler.krpg.players.Player
import java.awt.Graphics2D
import kotlin.math.round

private const val WIDTH = GameWindow.WIDTH
private const val HEIGHT = 40 // If HEIGHT = 180, then this is ~22%
private const val TOP = GameWindow.HEIGHT - HEIGHT

object HUDRenderer
{
    private val image: Image = Image(WIDTH, HEIGHT, Image.TYPE_INT_ARGB)

    fun render(): Pair<Image, Pixel>
    {
        val state = GameState.getInstance()

        val graphics = image.graphics as Graphics2D

        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH, HEIGHT)

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, WIDTH, HEIGHT)
        graphics.drawString("Chigz Jupsiz", 6, 12)
        val healthBar = _drawHealthBar(64, 8)
        graphics.drawImage(healthBar, 6, 18, null)
        val pixel = Pixel(0, TOP)
        return Pair(image, pixel)
    }

    private fun _drawHealthBar(width: Int, height: Int): Image
    {
        val image = Image(width, height, Image.TYPE_INT_ARGB)
        val graphics = image.graphics as Graphics2D

        graphics.color = Colors.BLACK
        graphics.fillRect(0, 0, WIDTH, HEIGHT)
        val playerUnit: Unit = GameState.getInstance().getPlayers()
            .filterIsInstance<HumanPlayer>()
            .flatMap(Player::getUnits)
            .firstOrNull()
            ?: error("No player unit found")

        val fullPercentage = 100.0 * playerUnit.getCurrentHP() / playerUnit.getMaxHP()
        val healthWidth = round((width - 2) * fullPercentage / 100.0).toInt()
        val healthHeight = height - 2

        graphics.color = Colors.WHITE
        graphics.drawRect(0, 0, width - 1, height - 1) // TODO Why do we have to subtract here?
        graphics.color = Colors.GREEN
        graphics.fillRect(1, 1, healthWidth, healthHeight)

        return image
    }
}
