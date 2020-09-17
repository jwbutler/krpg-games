package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.graphics.awt.GameWindowAWT
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter

interface GameWindow
{
    fun render()
    fun maximize()
    fun restore()
    fun toggleMaximized()
    fun addKeyListener(keyListener: KeyListener)
    fun addMouseListener(mouseListener: MouseAdapter)

    companion object : SingletonHolder<GameWindow>(::GameWindowAWT)
}