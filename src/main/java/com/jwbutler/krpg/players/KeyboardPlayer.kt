package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.TileOverlay
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.utils.getPlayerUnits
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import kotlin.math.abs

class KeyboardPlayer : HumanPlayer()
{
    private val queuedDirections = mutableSetOf<Int>()
    private val heldDirections = mutableSetOf<Int>()
    private val queuedModifiers = mutableSetOf<Int>()

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        var (dx, dy) = Pair(0, 0)

        for (directionKey in queuedDirections)
        {
            when (directionKey)
            {
                KeyEvent.VK_W -> dy--
                KeyEvent.VK_A -> dx--
                KeyEvent.VK_S -> dy++
                KeyEvent.VK_D -> dx++
                else -> {}
            }
        }
        queuedDirections.clear()

        if (dx != 0) dx /= abs(dx)
        if (dy != 0) dy /= abs(dy)
        val coordinates = Coordinates(unit.getCoordinates().x + dx, unit.getCoordinates().y + dy)

        if ((dx != 0 || dy != 0) && GameState.getInstance().containsCoordinates(coordinates))
        {
            if (queuedModifiers.contains(KeyEvent.VK_SHIFT))
            {
                return Pair(Activity.ATTACKING, Direction.from(IntPair.of(dx, dy)))
            }
            else
            {
                return Pair(Activity.WALKING, Direction.from(IntPair.of(dx, dy)))
            }
        }
        return Pair(Activity.STANDING, unit.getDirection())
    }

    override fun getTileOverlays() = mutableMapOf<Coordinates, TileOverlay>()

    override fun getUIOverlays() = listOf<Renderable>()

    override fun getKeyListener() = object : KeyAdapter()
    {
        override fun keyPressed(e: KeyEvent)
        {
            if (arrayOf(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D).contains(e.keyCode))
            {
                if (!heldDirections.contains(e.keyCode))
                {
                    queuedDirections.add(e.keyCode)
                    heldDirections.add(e.keyCode)
                }
            }
            else if (arrayOf(KeyEvent.VK_SHIFT).contains(e.keyCode))
            {
                queuedModifiers.add(e.keyCode)
            }
        }

        override fun keyReleased(e: KeyEvent)
        {
            if (arrayOf(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D).contains(e.keyCode))
            {
                heldDirections.remove(e.keyCode)
            }
            if (arrayOf(KeyEvent.VK_SHIFT).contains(e.keyCode))
            {
                queuedModifiers.remove(e.keyCode)
            }
        }
    }

    override fun getSelectedUnits() = getPlayerUnits().toSet()
}