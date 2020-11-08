package com.jwbutler.rpglib.players

import com.jwbutler.rpglib.graphics.GameWindow
import java.awt.event.KeyAdapter
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter

abstract class HumanPlayer : AbstractPlayer()
{
    final override val isHuman = true

    init
    {
        // IntelliJ doesn't like this but it seems ok
        GameWindow.getInstance().addKeyListener(getKeyListener())
        GameWindow.getInstance().addMouseListener(getMouseListener())
    }

    open fun getKeyListener(): KeyListener = object : KeyAdapter() {}
    open fun getMouseListener(): MouseAdapter = object : MouseAdapter() {}
}