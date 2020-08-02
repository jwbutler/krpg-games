package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.geometry.Dimensions
import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import java.awt.GraphicsEnvironment
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.min
import kotlin.math.roundToInt

open class Window
(
    renderWidth: Int,
    renderHeight: Int,
    actualWidth: Int,
    actualHeight: Int
)
{
    protected val buffer: Image = Image(renderWidth, renderHeight)
    protected val frame: JFrame = JFrame()
    protected val panel: JPanel = JPanel()
    private var maximized = false

    init
    {
        panel.setSize(actualWidth, actualHeight)
        panel.setDoubleBuffered(true)

        frame.setVisible(true)
        val insets = frame.getInsets()
        val outerWidth = actualWidth + insets.left + insets.right
        val outerHeight = actualHeight + insets.top + insets.bottom
        frame.setSize(outerWidth, outerHeight)
        frame.add(panel)
    }

    fun addKeyListener(keyListener: KeyListener) = frame.addKeyListener(keyListener)
    fun addMouseListener(mouseListener: MouseAdapter)
    {
        frame.addMouseListener(mouseListener)
        frame.addMouseMotionListener(mouseListener)
    }

    fun clearBuffer()
    {
        buffer.clearRect(0, 0, buffer.width, buffer.height)
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
        val scaleFactor = 1.0 * width / GAME_WIDTH // should get the same result with height / HEIGHT

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

    fun maximize()
    {
        // https://stackoverflow.com/a/35108575
        GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .setFullScreenWindow(frame)
    }

    fun restore()
    {
        // https://stackoverflow.com/a/35108575
        GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .setFullScreenWindow(null)
    }

    fun toggleMaximized()
    {
        if (maximized) restore() else maximize()
        maximized = !maximized
    }

    fun _getScaledDimensions(): Dimensions
    {
        val scaleFactor = min(
            1.0 * panel.getWidth() / GAME_WIDTH,
            1.0 * panel.getHeight() / GAME_HEIGHT
        )
        val width = min((1.0 * GAME_WIDTH * scaleFactor).roundToInt(), panel.getWidth())
        val height = min((1.0 * GAME_HEIGHT * scaleFactor).roundToInt(), panel.getHeight())
        return Dimensions(width, height)
    }
}