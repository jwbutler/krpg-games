package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.DirectionalAttackCommand
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.TileOverlay
import com.jwbutler.krpg.entities.TileOverlayFactory
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.ui.UIOverlays
import com.jwbutler.krpg.utils.getEnemyUnits
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.krpg.utils.getUnitsInPixelRect
import com.jwbutler.krpg.utils.pixelToCoordinates
import com.jwbutler.krpg.utils.rectFromPixels
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities.isLeftMouseButton
import javax.swing.SwingUtilities.isRightMouseButton

private typealias CommandSupplier = (Unit) -> Command

class MousePlayer : HumanPlayer()
{
    private val queuedCommands = mutableMapOf<Unit, CommandSupplier>()
    private val selectedUnits = mutableSetOf<Unit>()
    var selectionStart: Pixel? = null
    var selectionEnd: Pixel? = null

    override fun chooseCommand(unit: Unit): Command
    {
        return queuedCommands.remove(unit)
            ?.invoke(unit)
            ?: StayCommand(unit)
    }

    override fun getQueuedCommand(unit: Unit): Command?
    {
        return queuedCommands[unit]?.invoke(unit)
    }

    override fun getSelectedUnits() = selectedUnits

    override fun getTileOverlays(): Map<Coordinates, TileOverlay>
    {
        val overlays = mutableMapOf<Coordinates, TileOverlay>()

        val enemyUnits = getEnemyUnits()
        for (unit in enemyUnits)
        {
            overlays[unit.getCoordinates()] = TileOverlayFactory.enemyOverlay(unit.getCoordinates(), false)
        }

        val playerUnits = getPlayerUnits()
        for (unit in playerUnits)
        {
            val command = getQueuedCommand(unit) ?: unit.getCommand()
            when (command)
            {
                is AttackCommand ->
                {
                    val target = command.target
                    if (target.exists())
                    {
                        val coordinates = command.target.getCoordinates()
                        overlays[coordinates] = TileOverlayFactory.enemyOverlay(coordinates, true)
                    }
                }
                is DirectionalAttackCommand ->
                {
                    val coordinates = command.target
                    overlays[coordinates] = TileOverlayFactory.enemyOverlay(coordinates, true)
                }
                is MoveCommand ->
                {
                    val coordinates = command.target
                    overlays[coordinates] = TileOverlayFactory.positionOverlay(coordinates, true)
                }
            }

            overlays[unit.getCoordinates()] = TileOverlayFactory.playerOverlay(unit.getCoordinates(), selectedUnits.contains(unit))
        }

        return overlays
    }

    override fun getUIOverlays(): Collection<Renderable>
    {
        if (selectionStart != null && selectionEnd != null)
        {
            return listOf(UIOverlays.getSelectionRect(selectionStart!!, selectionEnd!!))
        }
        return listOf()
    }

    override fun getKeyListener(): KeyListener
    {
        return object : KeyAdapter()
        {
            override fun keyReleased(e: KeyEvent)
            {
                when (e.getKeyCode())
                {
                    KeyEvent.VK_SPACE -> GameEngine.getInstance().togglePause()
                    KeyEvent.VK_ENTER ->
                    {
                        if (e.isAltDown())
                        {
                            GameWindow.getInstance().toggleMaximized()
                        }
                    }
                    KeyEvent.VK_A ->
                    {
                        if (e.isControlDown())
                        {
                            selectedUnits.clear()
                            selectedUnits.addAll(getPlayerUnits())
                        }
                    }
                    KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5 ->
                    {
                        _handleNumberKey(e)
                    }
                }
            }

            private fun _handleNumberKey(e: KeyEvent)
            {
                val keyCode = e.getKeyCode()
                val i = keyCode - KeyEvent.VK_1
                val playerUnits = getPlayerUnits()
                if (playerUnits.lastIndex >= i)
                {
                    if (e.isControlDown())
                    {
                        val unit = playerUnits[i]
                        if (selectedUnits.contains(unit))
                        {
                            selectedUnits.remove(unit)
                        }
                        else
                        {
                            selectedUnits.add(unit)
                        }
                    }
                    else
                    {
                        selectedUnits.clear()
                        selectedUnits.add(playerUnits[i])
                    }
                }
            }
        }
    }

    override fun getMouseListener(): MouseAdapter
    {
        return object : MouseAdapter()
        {
            override fun mousePressed(e: MouseEvent)
            {
                if (isLeftMouseButton(e))
                {
                    selectionStart = Pixel.fromPoint(e.getPoint())
                }

                if (isRightMouseButton(e))
                {
                    val pixel = Pixel.fromPoint(e.getPoint())
                    val coordinates = pixelToCoordinates(pixel)
                    if (GameState.getInstance().containsCoordinates(coordinates))
                    {
                        for (unit in selectedUnits)
                        {
                            val command: CommandSupplier = { u ->
                                _tryAttack(u, coordinates)
                                    ?: _tryMove(u, coordinates)
                                    ?: _stay(u, coordinates)
                            }

                            queuedCommands[unit] = command
                        }
                    }
                }
            }

            override fun mouseDragged(e: MouseEvent)
            {
                if (isLeftMouseButton(e))
                {
                    selectionEnd = Pixel.fromPoint(e.getPoint())
                }
            }

            override fun mouseReleased(e: MouseEvent)
            {
                if (isLeftMouseButton(e))
                {
                    if (selectionStart != null && selectionEnd != null)
                    {
                        val selectionRect = rectFromPixels(selectionStart!!, selectionEnd!!)
                        selectedUnits.clear()
                        selectedUnits.addAll(getUnitsInPixelRect(selectionRect))
                    }
                    selectionStart = null
                    selectionEnd = null
                }
            }

            override fun mouseClicked(e: MouseEvent)
            {
                if (isLeftMouseButton(e))
                {
                    selectionStart = null
                    selectionEnd = null
                    selectedUnits.clear()
                }
            }
        }
    }

    companion object
    {
        private fun _tryAttack(u: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != u.getCoordinates())
            {
                val targetUnit = GameState.getInstance().getUnit(coordinates)
                if (targetUnit != null && !(targetUnit.getPlayer() is HumanPlayer))
                {
                    return AttackCommand(u, targetUnit)
                }
            }
            return null
        }

        private fun _tryMove(u: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != u.getCoordinates())
            {
                return MoveCommand(u, coordinates)
            }
            return null
        }

        private fun _stay(u: Unit, coordinates: Coordinates) = StayCommand(u)
    }
}