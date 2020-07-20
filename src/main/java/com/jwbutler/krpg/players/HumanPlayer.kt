package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.GameWindow
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.lang.Math.abs

class HumanPlayer : AbstractPlayer()
{
    private val keysPressed = mutableSetOf<Int>()
    private val keysToRelease = mutableSetOf<Int>()

    init
    {
        GameWindow.getInstance().addKeyListener(_getKeyListener())
    }

    override fun chooseCommand(unit: Unit): Command
    {
        val state = GameState.getInstance()
        val keysPressedThisTurn = keysPressed.toSet()
        keysPressed.removeAll(keysToRelease)

        var dx = 0
        var dy = 0
        if (arrayOf(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D).any(keysPressedThisTurn::contains))
        {
            if (keysPressedThisTurn.contains(KeyEvent.VK_W))
            {
                dx--
                dy--
            }
            if (keysPressedThisTurn.contains(KeyEvent.VK_S))
            {
                dx++
                dy++
            }
            if (keysPressedThisTurn.contains(KeyEvent.VK_A))
            {
                dx--
                dy++
            }
            if (keysPressedThisTurn.contains(KeyEvent.VK_D))
            {
                dx++
                dy--
            }
        }
        if (dx != 0) dx /= abs(dx)
        if (dy != 0) dy /= abs(dy)

        if (dx != 0 || dy != 0)
        {
            val coordinates = Coordinates(unit.getCoordinates().x + dx, unit.getCoordinates().y + dy)
            val targetUnit: Unit? = state.getUnit(coordinates)

            if (keysPressedThisTurn.contains(KeyEvent.VK_SHIFT) && targetUnit != null)
            {
                return AttackCommand(unit, targetUnit)
            }
            else
            {
                if (state.containsCoordinates(coordinates))
                {
                    return MoveCommand(unit, coordinates)
                }
            }
        }
        // fall through, default

        return StayCommand(unit)
    }

    private fun _getKeyListener() = object : KeyAdapter()
    {
        override fun keyPressed(e: KeyEvent)
        {
            keysPressed.add(e.keyCode)
        }

        override fun keyReleased(e: KeyEvent)
        {
            keysToRelease.add(e.keyCode)
        }
    }
}