package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.krpg.behavior.commands.BashCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.CommandType
import com.jwbutler.krpg.behavior.commands.DirectionalAttackCommand
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.RepeatingAttackCommand
import com.jwbutler.krpg.behavior.commands.SingleAttackCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.entities.TileOverlayFactory
import com.jwbutler.krpg.graphics.ui.UIOverlayFactory
import com.jwbutler.krpg.utils.getAverageCoordinates
import com.jwbutler.krpg.utils.getEnemyUnits
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.krpg.utils.getUnitsInPixelRect
import com.jwbutler.krpg.utils.rectFromPixels
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.geometry.Pixel
import com.jwbutler.rpglib.geometry.pixelToCoordinates
import com.jwbutler.rpglib.graphics.GameWindow
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.rpglib.players.HumanPlayer
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities.isLeftMouseButton
import javax.swing.SwingUtilities.isRightMouseButton

private typealias CommandSupplier = (Unit) -> Command

class MousePlayer : HumanPlayer()
{
    private val currentCommands = mutableMapOf<Unit, Command>()
    private val queuedCommands = mutableMapOf<Unit, CommandSupplier>()
    private val selectedUnits = mutableSetOf<Unit>()
    private var cameraCoordinates: Coordinates = Coordinates(0, 0)
    var selectionStart: Pixel? = null
    var selectionEnd: Pixel? = null

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        val currentCommand = currentCommands[unit] ?: StayCommand(unit)
        val nextCommand = _chooseCommand(unit)
        val command: Command
        if (currentCommand.isComplete())
        {
            command = nextCommand
        }
        // TODO: Replace reference to hardcoded command type
        else if (currentCommand.isPreemptible() && nextCommand.type != CommandType.STAY)
        {
            command = nextCommand
        }
        else
        {
            command = currentCommand
        }
        currentCommands[unit] = command
        return command.chooseActivity()
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
            val command = queuedCommands[unit]?.invoke(unit)
                ?: currentCommands[unit]
                ?: StayCommand(unit) // TODO - can we just initialize currentCommands with some of these?

            when (command)
            {
                is SingleAttackCommand ->
                {
                    val target = command.target
                    if (target.exists())
                    {
                        val coordinates = command.target.getCoordinates()
                        overlays[coordinates] = TileOverlayFactory.enemyOverlay(coordinates, true)
                    }
                }
                is RepeatingAttackCommand ->
                {
                    val target = command.target
                    if (target.exists())
                    {
                        val coordinates = command.target.getCoordinates()
                        overlays[coordinates] = TileOverlayFactory.enemyOverlay(coordinates, true)
                    }
                }
                is BashCommand ->
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
            return listOf(UIOverlayFactory.getSelectionRect(selectionStart!!, selectionEnd!!))
        }
        return listOf()
    }

    override fun getCameraCoordinates() = cameraCoordinates

    override fun getKeyListener() = object : KeyAdapter()
    {
        private val player = this@MousePlayer
        override fun keyReleased(event: KeyEvent)
        {
            when (event.getKeyCode())
            {
                KeyEvent.VK_SPACE -> GameEngine.getInstance().togglePause()
                KeyEvent.VK_ENTER ->
                {
                    if (event.isAltDown())
                    {
                        GameWindow.getInstance().toggleMaximized()
                    }
                }
                KeyEvent.VK_A ->
                {
                    if (event.isControlDown())
                    {
                        selectedUnits.clear()
                        selectedUnits.addAll(getPlayerUnits())
                    }
                }
                KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5 ->
                {
                    _handleNumberKey(event)
                }
                KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT ->
                {
                    _handleMoveCamera(event)
                }
                KeyEvent.VK_W ->
                {
                    if (event.isControlDown())
                    {
                        GameState.getInstance().getLevel().forceVictory = true
                    }
                }
                KeyEvent.VK_C ->
                {
                    if (selectedUnits.isNotEmpty())
                    {
                        player.cameraCoordinates = getAverageCoordinates(
                            selectedUnits.map(Unit::getCoordinates)
                        )
                    }
                }
            }
        }

        private fun _handleNumberKey(event: KeyEvent)
        {
            val keyCode = event.getKeyCode()
            val i = keyCode - KeyEvent.VK_1
            val playerUnits = getPlayerUnits()
            if (playerUnits.lastIndex >= i)
            {
                if (event.isControlDown())
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

        private fun _handleMoveCamera(event: KeyEvent)
        {
            val cameraCoordinates = player.cameraCoordinates
            val (x, y) = cameraCoordinates

            var (dx, dy) = Pair(0, 0)

            when (event.getKeyCode())
            {
                KeyEvent.VK_UP    -> dy--
                KeyEvent.VK_DOWN  -> dy++
                KeyEvent.VK_LEFT  -> dx--
                KeyEvent.VK_RIGHT -> dx++
                else -> {}
            }

            val newCoordinates = Coordinates(x + dx, y + dy)
            if (GameState.getInstance().containsCoordinates(newCoordinates))
            {
                player.cameraCoordinates = newCoordinates
            }
        }
    }

    override fun getMouseListener() = object : MouseAdapter()
    {
        override fun mousePressed(event: MouseEvent)
        {
            if (_isLeftClick(event))
            {
                selectionStart = Pixel.fromPoint(event.getPoint())
            }
            else if (_isRightClick(event))
            {
                val pixel = Pixel.fromPoint(event.getPoint())
                val coordinates = pixelToCoordinates(pixel)
                if (GameState.getInstance().containsCoordinates(coordinates))
                {
                    for (unit in selectedUnits)
                    {
                        val queuedCommand: CommandSupplier

                        if (event.isControlDown())
                        {
                            queuedCommand = { u ->
                                _tryBash(u, coordinates)
                                    ?: _tryAttack(u, coordinates)
                                    ?: _tryMove(u, coordinates)
                                    ?: _stay(u, coordinates)
                            }
                        }
                        else
                        {
                            queuedCommand = { u ->
                                _tryAttack(u, coordinates)
                                    ?: _tryMove(u, coordinates)
                                    ?: _stay(u, coordinates)
                            }
                        }

                        queuedCommands[unit] = queuedCommand
                    }
                }
            }
        }

        override fun mouseDragged(event: MouseEvent)
        {
            if (_isLeftClick(event))
            {
                selectionEnd = Pixel.fromPoint(event.getPoint())
            }
        }

        override fun mouseReleased(event: MouseEvent)
        {
            if (_isLeftClick(event))
            {
                if (selectionStart != null && selectionEnd != null)
                {
                    val selectionRect = rectFromPixels(selectionStart!!, selectionEnd!!)
                    selectedUnits.clear()
                    selectedUnits.addAll(
                        getUnitsInPixelRect(selectionRect).filter { u -> u.getPlayer().isHuman }
                    )
                }
                selectionStart = null
                selectionEnd = null
            }
        }

        override fun mouseClicked(event: MouseEvent)
        {
            if (_isLeftClick(event))
            {
                selectionStart = null
                selectionEnd = null
                selectedUnits.clear()
            }
        }

        private fun _isLeftClick(event: MouseEvent): Boolean
        {
            return isLeftMouseButton(event) && !event.isControlDown()
        }

        private fun _isRightClick(event: MouseEvent): Boolean
        {
            return isRightMouseButton(event)
                || (isLeftMouseButton(event) && event.isControlDown())
        }
    }

    private fun _chooseCommand(unit: Unit): Command
    {
        val queuedCommand = queuedCommands.remove(unit)
        if (queuedCommand != null)
        {
            return queuedCommand(unit)
        }
        else
        {
            return StayCommand(unit)
        }
    }

    companion object
    {
        private fun _tryAttack(unit: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != unit.getCoordinates())
            {
                if (unit.isActivityReady(RPGActivity.ATTACKING))
                {
                    val targetUnit = GameState.getInstance().getUnit(coordinates)
                    if (targetUnit != null && !(targetUnit.getPlayer() is HumanPlayer))
                    {
                        return RepeatingAttackCommand(unit, targetUnit)
                    }
                }
            }
            return null
        }

        private fun _tryBash(unit: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != unit.getCoordinates())
            {
                if (unit.isActivityReady(RPGActivity.BASHING))
                {
                    val targetUnit = GameState.getInstance().getUnit(coordinates)
                    if (targetUnit != null && !(targetUnit.getPlayer() is HumanPlayer))
                    {
                        return BashCommand(unit, targetUnit)
                    }
                }
            }
            return null
        }

        private fun _tryMove(unit: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != unit.getCoordinates())
            {
                return MoveCommand(unit, coordinates)
            }
            return null
        }

        private fun _stay(unit: Unit, coordinates: Coordinates) = StayCommand(unit)
    }
}