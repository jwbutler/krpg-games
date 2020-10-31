package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.krpg.entities.TileOverlayFactory
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.graphics.Renderable
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import kotlin.math.abs

class KeyboardPlayer : HumanPlayer()
{
    private val queuedDirections = mutableSetOf<Int>()
    private val heldDirections = mutableSetOf<Int>()
    private val heldModifiers = mutableSetOf<Int>()

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
        val coordinates = Coordinates(
            unit.getCoordinates().x + dx,
            unit.getCoordinates().y + dy
        )

        if ((dx != 0 || dy != 0) && GameState.getInstance().containsCoordinates(coordinates))
        {
            if (heldModifiers.contains(KeyEvent.VK_SHIFT) && unit.isActivityReady(RPGActivity.ATTACKING))
            {
                return Pair(RPGActivity.ATTACKING, Direction.from(dx, dy))
            }
            else if (unit.isActivityReady(RPGActivity.WALKING))
            {
                return Pair(RPGActivity.WALKING, Direction.from(dx, dy))
            }
        }
        return Pair(RPGActivity.STANDING, unit.getDirection())
    }

    override fun getTileOverlays(): Map<Coordinates, TileOverlay>
    {
        val overlays = mutableMapOf<Coordinates, TileOverlay>()
        val unit = _getUnit()

        overlays[unit.getCoordinates()] = TileOverlayFactory.playerOverlay(unit.getCoordinates(), true)
        val targetCoordinates = unit.getCoordinates() + unit.getDirection()
        GameState.getInstance()
            .getUnits()
            .filter { u -> u.getPlayer() != this } // TODO better hostility check
            .forEach { enemyUnit ->
                val isTargeted = unit.getActivity() == RPGActivity.ATTACKING && enemyUnit.getCoordinates() == targetCoordinates
                overlays[enemyUnit.getCoordinates()] = TileOverlayFactory.enemyOverlay(enemyUnit.getCoordinates(), isTargeted)
            }

        return overlays
    }

    override fun getUIOverlays() = listOf<Renderable>()
    override fun getCameraCoordinates() = _getUnit().getCoordinates()

    override fun getKeyListener() = object : KeyAdapter()
    {
        val player = this@KeyboardPlayer
        override fun keyPressed(e: KeyEvent)
        {
            when (e.keyCode)
            {
                KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D ->
                {
                    if (!heldDirections.contains(e.keyCode))
                    {
                        queuedDirections.add(e.keyCode)
                        heldDirections.add(e.keyCode)
                    }
                }
                KeyEvent.VK_SHIFT ->
                {
                    heldModifiers.add(e.keyCode)
                }
            }
        }

        override fun keyReleased(e: KeyEvent)
        {
            when (e.keyCode)
            {
                KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D ->
                {
                    heldDirections.remove(e.keyCode)
                }
                KeyEvent.VK_SHIFT ->
                {
                    heldModifiers.remove(e.keyCode)
                }
            }
        }
    }

    private fun _getUnit() = GameState.getInstance().getUnits(this)[0]

    override fun getSelectedUnits() = setOf(_getUnit())
}