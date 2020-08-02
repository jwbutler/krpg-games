package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.DirectionalAttackCommand
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Overlay
import com.jwbutler.krpg.entities.OverlayFactory
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.krpg.utils.pixelToCoordinates
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

private typealias CommandSupplier = (Unit) -> Command

class MousePlayer : HumanPlayer()
{
    private var queuedCommands = mutableMapOf<Unit, CommandSupplier>()

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

    override fun getOverlays(): Map<Coordinates, Overlay>
    {
        val overlays = mutableMapOf<Coordinates, Overlay>()

        val playerUnits = getPlayerUnits()
        for (unit in playerUnits)
        {
            overlays[unit.getCoordinates()] = OverlayFactory.playerOverlay(unit.getCoordinates(), true)

            val command = getQueuedCommand(unit) ?: unit.getCommand()
            when (command)
            {
                is AttackCommand ->
                {
                    val target = command.target
                    if (target.exists())
                    {
                        val coordinates = command.target.getCoordinates()
                        overlays[coordinates] = OverlayFactory.enemyOverlay(coordinates, true)
                    }
                }
                is DirectionalAttackCommand ->
                {
                    val coordinates = command.target
                    overlays[coordinates] = OverlayFactory.enemyOverlay(coordinates, true)
                }
                is MoveCommand ->
                {
                    val coordinates = command.target
                    overlays[coordinates] = OverlayFactory.positionOverlay(coordinates, true)
                }
            }
        }

        return overlays
    }

    override fun getKeyListener(): KeyListener
    {
        return object : KeyAdapter()
        {
            override fun keyReleased(e: KeyEvent)
            {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    GameEngine.getInstance().togglePause()
                }
            }
        }
    }

    override fun getMouseListener(): MouseListener
    {
        return object : MouseAdapter()
        {
            override fun mouseReleased(e: MouseEvent)
            {
                val pixel = Pixel.fromPoint(e.getPoint())
                val coordinates = pixelToCoordinates(pixel)
                if (GameState.getInstance().containsCoordinates(coordinates))
                {
                    for (unit in getPlayerUnits())
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