package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.awt.GameWindowAWT
import com.jwbutler.krpg.graphics.images.Image
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter

interface GameWindow
{
    fun clearBuffer()
    fun render(image: Image, pixel: Pixel)
    fun redraw()
    fun maximize()
    fun restore()
    fun toggleMaximized()
    fun addKeyListener(keyListener: KeyListener)
    fun addMouseListener(mouseListener: MouseAdapter)

    companion object : SingletonHolder<GameWindow>(::GameWindowAWT)
}