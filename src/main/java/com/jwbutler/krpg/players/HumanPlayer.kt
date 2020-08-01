package com.jwbutler.krpg.players

import com.jwbutler.krpg.entities.Overlay
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.GameWindow
import java.awt.event.KeyAdapter
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseListener

abstract class HumanPlayer : AbstractPlayer()
{
    init
    {
        // IntelliJ doesn't like this but it seems ok
        GameWindow.getInstance().addKeyListener(getKeyListener())
        GameWindow.getInstance().addMouseListener(getMouseListener())
    }

    abstract fun getOverlays(): Map<Coordinates, Overlay>
    open fun getKeyListener(): KeyListener = object : KeyAdapter() {}
    open fun getMouseListener(): MouseListener = object : MouseAdapter() {}
}