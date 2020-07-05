package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.geometry.Pixel
import java.awt.image.BufferedImage
import javax.swing.JFrame

private const val WIDTH = 640;
private const val HEIGHT = 360;

class GameWindow private constructor()
{
    private val buffer: Image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
    private val frame: JFrame = JFrame()

    fun render(image: Image, pixel: Pixel)
    {
        buffer.getGraphics().drawImage(image, pixel.x, pixel.y, null)
    }

    fun redraw()
    {
        frame.getGraphics().drawImage(buffer, 0, 0, null)
        frame.repaint()
    }

    companion object
    {
        private var INSTANCE: GameWindow? = null

        fun initialize()
        {
            INSTANCE = GameWindow()
        }

        fun getInstance(): GameWindow = INSTANCE ?: error("GameWindow was not initialized")
    }
}