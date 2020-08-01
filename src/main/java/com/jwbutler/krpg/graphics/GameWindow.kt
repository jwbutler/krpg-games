package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Image
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class GameWindow private constructor()
{
    private val buffer: Image = Image(WIDTH, HEIGHT)
    private val frame: JFrame = JFrame()
    private val panel: JPanel = JPanel()

    init
    {
        panel.setSize(SCALED_WIDTH, SCALED_HEIGHT)
        val insets = frame.getInsets()
        val outerWidth = SCALED_WIDTH + insets.left + insets.right
        val outerHeight = SCALED_HEIGHT + insets.top + insets.bottom
        frame.setSize(outerWidth, outerHeight)
        frame.add(panel)
        frame.setVisible(true)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    }

    fun clearBuffer()
    {
        buffer.clearRect(0, 0, WIDTH, HEIGHT)
    }

    fun render(image: Image, pixel: Pixel)
    {
        buffer.drawImage(image, pixel.x, pixel.y)
    }

    fun redraw()
    {
        val scaled = buffer.scaleTo(SCALED_WIDTH, SCALED_HEIGHT)
        scaled.draw(panel.getGraphics(), 0, 0)
    }

    fun mapPixel(p: Pixel): Pixel
    {
        val insets = frame.getInsets()
        return Pixel(
            (p.x - insets.left) / SCALE_FACTOR,
            (p.y - insets.top) / SCALE_FACTOR
        )
    }

    fun addKeyListener(keyListener: KeyListener) = frame.addKeyListener(keyListener)
    fun addMouseListener(mouseListener: MouseListener) = frame.addMouseListener(mouseListener)

    companion object : SingletonHolder<GameWindow>(::GameWindow)
    {
        const val WIDTH = 320
        const val HEIGHT = 180

        const val SCALED_WIDTH = 1280
        const val SCALED_HEIGHT = 720

        const val SCALE_FACTOR = 4
    }
}