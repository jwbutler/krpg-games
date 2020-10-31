package com.jwbutler.rpglib.players

import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.graphics.GameWindow
import com.jwbutler.rpglib.graphics.Renderable
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

    abstract fun getSelectedUnits(): Set<Unit>
    abstract fun getTileOverlays(): Map<Coordinates, TileOverlay>
    abstract fun getUIOverlays(): Collection<Renderable>
    abstract fun getCameraCoordinates(): Coordinates

    open fun getKeyListener(): KeyListener = object : KeyAdapter() {}
    open fun getMouseListener(): MouseAdapter = object : MouseAdapter() {}
}