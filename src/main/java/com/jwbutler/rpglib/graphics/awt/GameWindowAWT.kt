package com.jwbutler.rpglib.graphics.awt

import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.geometry.Pixel
import com.jwbutler.rpglib.graphics.GameRenderer
import com.jwbutler.rpglib.graphics.GameWindow
import com.jwbutler.rpglib.graphics.images.Image
import com.jwbutler.rpglib.input.DelegatingMouseListener
import java.awt.Graphics
import java.awt.GraphicsEnvironment
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * See https://stackoverflow.com/a/17865740 for the general approach here
 */
class GameWindowAWT(initialDimensions: Dimensions) : GameWindow
{
    private val frame: JFrame = JFrame()
    private val panel: JPanel = object : JPanel()
    {
        override fun paintComponent(graphics: Graphics)
        {
            super.paintComponent(graphics)
            _redraw(graphics)
        }
    }
    private var imageDimensions: Dimensions
    private var maximized = false

    init
    {
        panel.setSize(initialDimensions.width, initialDimensions.height)
        panel.setDoubleBuffered(false) // We're doing our own!
        imageDimensions = Dimensions(panel.width, panel.height)

        frame.setVisible(true)
        val insets = frame.getInsets()
        val outerWidth = initialDimensions.width + insets.left + insets.right
        val outerHeight = initialDimensions.height + insets.top + insets.bottom
        frame.setSize(outerWidth, outerHeight)
        frame.add(panel)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    }

    override fun render()
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

    private fun _getScaledDimensions(image: Image): Dimensions
    {
        val scaleFactor = min(
            1.0 * panel.getWidth() / image.width,
            1.0 * panel.getHeight() / image.height
        )
        val width = min(
            (1.0 * image.width * scaleFactor).roundToInt(),
            panel.getWidth()
        )
        val height = min(
            (1.0 * image.height * scaleFactor).roundToInt(),
            panel.getHeight()
        )
        return Dimensions(width, height)
    }

    private fun _redraw(graphics: Graphics)
    {
        val image = GameRenderer.getInstance().getImage()
        val (width, height) = _getScaledDimensions(image)
        val left = (panel.getWidth() - width) / 2
        val top = (panel.getHeight() - height) / 2
        (image as ImageAWT).drawOnto(graphics, left, top, width, height)
        imageDimensions = Dimensions(width, height)
    }

    /**
     * @param x X coordinate relative to [frame]
     * @param y Y coordinate relative to [frame]
     * @return coordinates relative to [panel]
     */
    private fun _mapPixel(x: Int, y: Int): Pixel
    {
        val gameDimensions = GameView.getInstance().gameDimensions
        val (width, height) = imageDimensions
        val insets = frame.getInsets()

        val panelLeft = (panel.getWidth() - width) / 2
        val panelTop = (panel.getHeight() - height) / 2
        return Pixel(
            (x - panelLeft - insets.left) * gameDimensions.width / width,
            (y - panelTop - insets.top) * gameDimensions.height / height
        )

        /*val (width, height) = imageDimensions
        val scaleFactor = 1.0 * width / panel.width // should get the same result with height / HEIGHT

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
        )*/
    }
}