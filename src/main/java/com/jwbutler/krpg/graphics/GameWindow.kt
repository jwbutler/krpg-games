package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.geometry.Pixel
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

class GameWindow private constructor()
{
    private val buffer: Image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
    private val frame: JFrame = JFrame()
    private val panel: JPanel = JPanel()

    init
    {
        panel.setSize(WIDTH, HEIGHT)
        frame.setSize(WIDTH, HEIGHT)
        frame.add(panel)
        frame.setVisible(true)
    }

    fun clearBuffer()
    {
        buffer.getGraphics().clearRect(0, 0, WIDTH, HEIGHT)
    }

    fun render(image: Image, pixel: Pixel)
    {
        buffer.getGraphics().drawImage(image, pixel.x, pixel.y, null)
    }

    fun redraw()
    {
        panel.getGraphics().drawImage(buffer, 0, 0, null)
    }

    fun addKeyListener(keyListener: KeyListener)
    {
        frame.addKeyListener(keyListener)
    }

    companion object
    {
        const val WIDTH = 640
        const val HEIGHT = 360

        private var INSTANCE: GameWindow? = null

        fun initialize()
        {
            INSTANCE = GameWindow()
        }

        fun getInstance(): GameWindow = INSTANCE ?: error("GameWindow was not initialized")
    }
}