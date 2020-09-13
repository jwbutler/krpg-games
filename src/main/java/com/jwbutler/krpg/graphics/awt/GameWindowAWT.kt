package com.jwbutler.krpg.graphics.awt

import com.jwbutler.krpg.geometry.Dimensions
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.input.DelegatingMouseListener
import java.awt.Graphics
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.min
import kotlin.math.roundToInt
import java.awt.GraphicsEnvironment
import java.awt.event.MouseAdapter

/**
 * See https://stackoverflow.com/a/17865740 for the general approach here
 */
class GameWindowAWT
(
    private val gameWidth: Int,
    private val gameHeight: Int,
    private val windowWidth: Int,
    private val windowHeight: Int
) : GameWindow
{
    private val buffer: Image = Image.create(gameWidth, gameHeight)
    private val frame: JFrame = JFrame()
    private val panel: JPanel = object : JPanel()
    {
        override fun paintComponent(graphics: Graphics)
        {
            super.paintComponent(graphics)
            _redraw(graphics)
        }
    }
    private var maximized = false

    init
    {
        panel.setSize(windowWidth, windowHeight)
        panel.setDoubleBuffered(false) // We're doing our own!

        frame.setVisible(true)
        val insets = frame.getInsets()
        val outerWidth = windowWidth + insets.left + insets.right
        val outerHeight = windowHeight + insets.top + insets.bottom
        frame.setSize(outerWidth, outerHeight)
        frame.add(panel)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    }

    override fun clearBuffer()
    {
        buffer.clearRect(0, 0, gameWidth, gameHeight)
    }

    override fun render(image: Image, pixel: Pixel)
    {
        buffer.drawImage(image, pixel.x, pixel.y)
    }

    override fun redraw()
    {
        panel.repaint()
    }

    override fun maximize()
    {
        // https://stackoverflow.com/a/35108575
        GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .setFullScreenWindow(frame)
    }

    override fun restore()
    {
        // https://stackoverflow.com/a/35108575
        GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .setFullScreenWindow(null)
    }

    override fun toggleMaximized()
    {
        if (maximized) restore() else maximize()
        maximized = !maximized
    }

    override fun addKeyListener(keyListener: KeyListener) = frame.addKeyListener(keyListener)
    override fun addMouseListener(mouseListener: MouseAdapter)
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
            1.0 * panel.getWidth() / windowWidth,
            1.0 * panel.getHeight() / windowHeight
        )
        val width = min(
            (1.0 * windowWidth * scaleFactor).roundToInt(),
            panel.getWidth()
        )
        val height = min(
            (1.0 * windowHeight * scaleFactor).roundToInt(),
            panel.getHeight()
        )
        return Dimensions(width, height)
    }

    private fun _redraw(graphics: Graphics)
    {
        val (width, height) = _getScaledDimensions()
        val left = (panel.getWidth() - width) / 2
        val top = (panel.getHeight() - height) / 2
        (buffer as ImageAWT).drawOnto(graphics, left, top, width, height)
    }

    /**
     * @param x X coordinate relative to [frame]
     * @param y Y coordinate relative to [frame]
     * @return coordinates relative to [buffer]
     */
    private fun _mapPixel(x: Int, y: Int): Pixel
    {
        val renderer = GameRenderer.getInstance()
        val (width, height) = _getScaledDimensions()
        val scaleFactor = 1.0 * width / renderer.gameWidth // should get the same result with height / HEIGHT

        // coordinates of the scaled buffer relative to the panel
        val panelLeft = (panel.getWidth() - width) / 2
        val panelTop = (panel.getHeight() - height) / 2

        // coordinates of the pixel relative to the scaled buffer
        val insets = frame.getInsets()
        val panelX = x - panelLeft - insets.left
        val panelY = y - panelTop - insets.top

        return Pixel(
            (panelX / scaleFactor).roundToInt(),
            (panelY / scaleFactor).roundToInt()
        )
    }
}