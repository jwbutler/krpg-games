package com.jwbutler.rpglib.graphics

import com.jwbutler.rpglib.core.SingletonHolder
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

    companion object : SingletonHolder<GameWindow>()
}