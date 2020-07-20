package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.geometry.Pixel
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.system.exitProcess

class GameWindow private constructor()
{
    private val buffer: Image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
    private val frame: JFrame = JFrame()
    private val panel: JPanel = JPanel()

    init
    {
        panel.setSize(SCALED_WIDTH, SCALED_HEIGHT)
        frame.setSize(SCALED_WIDTH, SCALED_HEIGHT)
        frame.add(panel)
        frame.setVisible(true)
        frame.addWindowListener(object : WindowAdapter()
        {
            override fun windowClosing(e: WindowEvent?)
            {
                exitProcess(0)
            }
        })
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
        val scaled = buffer.getScaledInstance(SCALED_WIDTH, SCALED_HEIGHT, Image.SCALE_FAST)
        panel.getGraphics().drawImage(scaled, 0, 0, null)
    }

    fun addKeyListener(keyListener: KeyListener)
    {
        frame.addKeyListener(keyListener)
    }

    companion object
    {
        const val WIDTH = 320
        const val HEIGHT = 180

        const val SCALED_WIDTH = 1280
        const val SCALED_HEIGHT = 720

        private var INSTANCE: GameWindow? = null

        fun initialize()
        {
            INSTANCE = GameWindow()
        }

        fun getInstance(): GameWindow = INSTANCE ?: error("GameWindow was not initialized")
    }
}