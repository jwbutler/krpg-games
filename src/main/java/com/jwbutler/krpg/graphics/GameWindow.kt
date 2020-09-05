package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.geometry.Dimensions
import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.geometry.INITIAL_WINDOW_HEIGHT
import com.jwbutler.krpg.geometry.INITIAL_WINDOW_WIDTH
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.input.DelegatingMouseListener
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.min
import kotlin.math.roundToInt
import java.awt.GraphicsEnvironment
import java.awt.event.MouseAdapter

class GameWindow private constructor()
{
    private val buffer: Image = Image(GAME_WIDTH, GAME_HEIGHT)
    private val frame: JFrame = JFrame()
    private val panel: JPanel = JPanel()
    private var maximized = false

    init
    {
        panel.setSize(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT)
        panel.setDoubleBuffered(true)

        frame.setVisible(true)
        val insets = frame.getInsets()
        val outerWidth = INITIAL_WINDOW_WIDTH + insets.left + insets.right
        val outerHeight = INITIAL_WINDOW_HEIGHT + insets.top + insets.bottom
        frame.setSize(outerWidth, outerHeight)
        frame.add(panel)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    }

    fun clearBuffer()
    {
        buffer.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT)
    }

    fun render(image: Image, pixel: Pixel)
    {
        buffer.drawImage(image, pixel.x, pixel.y)
    }

    fun redraw()
    {
        val (width, height) = _getScaledDimensions()
        val left = (panel.getWidth() - width) / 2
        val top = (panel.getHeight() - height) / 2
        buffer.draw(panel.getGraphics(), left, top, width, height)
    }

    /**
     * @param x X coordinate relative to [frame]
     * @param y Y coordinate relative to [frame]
     * @return coordinates relative to [buffer]
     */
    private fun _mapPixel(x: Int, y: Int): Pixel
    {
        val (width, height) = _getScaledDimensions()
        val scaleFactor = 1.0 * width / GAME_WIDTH // should get the same result with height / HEIGHT

        // coordinates of the scaled buffer relative to the panel
        val panelLeft = (panel.getWidth() - width) / 2
        val panelTop = (panel.getHeight() - height) / 2

        // coordinates of the pixel relative to the scaled buffer
        val insets = frame.getInsets()
        val bufferX = x - panelLeft - insets.left
        val bufferY = y - panelTop - insets.top

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

    fun addKeyListener(keyListener: KeyListener) = frame.addKeyListener(keyListener)
    fun addMouseListener(mouseListener: MouseAdapter)
    {
        val wrapper = DelegatingMouseListener(mouseListener) { event ->
            val mappedPixel = _mapPixel(event.x, event.y)
            event.translatePoint(mappedPixel.x - event.x, mappedPixel.y - event.y)
            event
        }
        frame.addMouseListener(wrapper)
        frame.addMouseMotionListener(wrapper)
    }

    private fun _getScaledDimensions(): Dimensions
    {
        val scaleFactor = min(
            1.0 * panel.getWidth() / GAME_WIDTH,
            1.0 * panel.getHeight() / GAME_HEIGHT
        )
        val width = min(
            (1.0 * GAME_WIDTH * scaleFactor).roundToInt(),
            panel.getWidth()
        )
        val height = min(
            (1.0 * GAME_HEIGHT * scaleFactor).roundToInt(),
            panel.getHeight()
        )
        return Dimensions(width, height)
    }

    companion object : SingletonHolder<GameWindow>(::GameWindow)
}