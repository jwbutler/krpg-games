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

class HumanPlayer : AbstractPlayer()
{
    private val keysPressed = mutableSetOf<Int>()

    init
    {
        GameWindow.getInstance().addKeyListener(_getKeyListener())
    }

    override fun chooseCommand(unit: Unit): Command
    {
        val state = GameState.getInstance()

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

        if (dx != 0 || dy != 0)
        {
            val coordinates = Coordinates(unit.getCoordinates().x + dx, unit.getCoordinates().y + dy)
            val targetUnit: Unit? = state.getUnit(coordinates)

            if (keysPressed.contains(KeyEvent.VK_SHIFT) && targetUnit != null)
            {
                return AttackCommand(unit, targetUnit)
            }
            else
            {
                return MoveCommand(unit, coordinates)
            }
        }
        else
        {
            return StayCommand(unit)
        }
    }

    private fun _getKeyListener() = object : KeyAdapter()
    {
        override fun keyPressed(e: KeyEvent)
        {
            keysPressed.add(e.keyCode)
        }

        override fun keyReleased(e: KeyEvent)
        {
            keysPressed.remove(e.keyCode)
        }
    }
}