package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.geometry.Dimensions
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.min
import kotlin.math.roundToInt

class GameWindow private constructor()
{
    private val buffer: Image = Image(WIDTH, HEIGHT)
    private val frame: JFrame = JFrame()
    private val panel: JPanel = JPanel()

    init
    {
        panel.setSize(SCALED_WIDTH, SCALED_HEIGHT)
        panel.setDoubleBuffered(true)

        frame.setVisible(true)
        val insets = frame.getInsets()
        val outerWidth = SCALED_WIDTH + insets.left + insets.right
        val outerHeight = SCALED_HEIGHT + insets.top + insets.bottom
        frame.setSize(outerWidth, outerHeight)
        frame.add(panel)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH)
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
        val (width, height) = _getScaledDimensions()
        val scaled = buffer.scaleTo(width, height)
        val left = (panel.getWidth() - width) / 2
        val top = (panel.getHeight() - height) / 2

        val graphics = panel.getGraphics()
        graphics.setColor(Colors.BLACK)
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight())
        scaled.draw(graphics, left, top)
    }

    /**
     * @param p Coordinates relative to [frame]
     * @return coordinates relative to [buffer]
     */
    fun mapPixel(p: Pixel): Pixel
    {
        val (width, height) = _getScaledDimensions()
        val scaleFactor = 1.0 * width / WIDTH // should get the same result with height / HEIGHT

        // coordinates of the scaled buffer relative to the panel
        val panelLeft = (panel.getWidth() - width) / 2
        val panelTop = (panel.getHeight() - height) / 2

        // coordinates of the pixel relative to the scaled buffer
        val insets = frame.getInsets()
        val bufferX = p.x - panelLeft - insets.left
        val bufferY = p.y - panelTop - insets.top

        return Pixel(
            (bufferX / scaleFactor).roundToInt(),
            (bufferY / scaleFactor).roundToInt()
        )
    }

    fun addKeyListener(keyListener: KeyListener) = frame.addKeyListener(keyListener)
    fun addMouseListener(mouseListener: MouseListener) = frame.addMouseListener(mouseListener)

    fun _getScaledDimensions(): Dimensions
    {
        val scaleFactor = min(1.0 * panel.getWidth() / WIDTH, 1.0 * panel.getHeight() / HEIGHT)
        val width = min((1.0 * WIDTH * scaleFactor).roundToInt(), panel.getWidth())
        val height = min((1.0 * HEIGHT * scaleFactor).roundToInt(), panel.getHeight())
        return Dimensions(width, height)
    }

    companion object : SingletonHolder<GameWindow>(::GameWindow)
    {
        const val WIDTH = 320
        const val HEIGHT = 180

        private const val SCALED_WIDTH = 1280
        private const val SCALED_HEIGHT = 720
    }
}