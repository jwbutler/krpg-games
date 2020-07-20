package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.DirectionalAttackCommand
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
    private val keysPressedThisTurn = mutableSetOf<Int>()
    private val keysToRelease = mutableSetOf<Int>()

    init
    {
        GameWindow.getInstance().addKeyListener(_getKeyListener())
    }

    override fun chooseCommand(unit: Unit): Command
    {
        val state = GameState.getInstance()
        keysPressed.removeAll(keysToRelease)
        keysPressedThisTurn.clear()
        keysToRelease.clear()

        var dx = 0
        var dy = 0
        if (arrayOf(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D).any(keysPressed::contains))
        {
            if (keysPressed.contains(KeyEvent.VK_W))
            {
                dy--
            }
            if (keysPressed.contains(KeyEvent.VK_S))
            {
                dy++
            }
            if (keysPressed.contains(KeyEvent.VK_A))
            {
                dx--
            }
            if (keysPressed.contains(KeyEvent.VK_D))
            {
                dx++
            }
        }
        if (dx != 0) dx /= abs(dx)
        if (dy != 0) dy /= abs(dy)

        if (dx != 0 || dy != 0)
        {
            val coordinates = Coordinates(unit.getCoordinates().x + dx, unit.getCoordinates().y + dy)

            if (state.containsCoordinates(coordinates))
            {
                if (keysPressed.contains(KeyEvent.VK_SHIFT))
                {
                    return DirectionalAttackCommand(unit, coordinates)
                }
                else
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
            keysPressedThisTurn.add(e.keyCode)
        }

        override fun keyReleased(e: KeyEvent)
        {
            val keyCode = e.keyCode
            if (!keysPressedThisTurn.contains(keyCode))
            {
                keysPressed.remove(keyCode)
            }
            else
            {
                keysToRelease.add(keyCode)
            }
        }
    }
}