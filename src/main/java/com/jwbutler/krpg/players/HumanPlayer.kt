package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.TileOverlay
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.ui.GameWindow
import com.jwbutler.krpg.graphics.Renderable
import java.awt.event.KeyAdapter
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter

abstract class HumanPlayer : AbstractPlayer()
{
    init
    {
        // IntelliJ doesn't like this but it seems ok
        GameWindow.getInstance().addKeyListener(getKeyListener())
        GameWindow.getInstance().addMouseListener(getMouseListener())
    }

    abstract fun getQueuedCommand(unit: Unit): Command?
    abstract fun getTileOverlays(): Map<Coordinates, TileOverlay>
    abstract fun getUIOverlays(): Collection<Renderable>

    open fun getKeyListener(): KeyListener = object : KeyAdapter() {}
    open fun getMouseListener(): MouseAdapter = object : MouseAdapter() {}
    open fun getSelectedUnits(): Set<Unit> = setOf()
}