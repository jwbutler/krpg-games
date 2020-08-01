package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.AttackCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Overlay
import com.jwbutler.krpg.entities.OverlayFactory
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.krpg.utils.getTargetedEnemies
import com.jwbutler.krpg.utils.pixelToCoordinates
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

private typealias CommandSupplier = (Unit) -> Command

class MousePlayer : HumanPlayer()
{
    private var queuedCommands = mutableMapOf<Unit, CommandSupplier>()

    init
    {
        GameWindow.getInstance().addMouseListener(_getMouseListener())
    }

    override fun chooseCommand(unit: Unit): Command
    {
        return queuedCommands.remove(unit)
            ?.invoke(unit)
            ?: StayCommand(unit)
    }

    override fun getOverlays(): Map<Coordinates, Overlay>
    {
        val overlays = mutableMapOf<Coordinates, Overlay>()

        val playerUnits = getPlayerUnits()
        for (unit in playerUnits)
        {
            overlays[unit.getCoordinates()] = OverlayFactory.createPlayerOverlay(unit.getCoordinates(), true)
        }

        val targetUnits = getTargetedEnemies(playerUnits)
        for (unit in targetUnits)
        {
            overlays[unit.getCoordinates()] = OverlayFactory.createEnemyoverlay(unit.getCoordinates(), true)
        }

        val targetCoordinates = _getTargetedCoordinates(playerUnits)
        for (coordinates in targetCoordinates)
        {
            overlays[coordinates] = OverlayFactory.createPositionOverlay(coordinates, true)
        }

        return overlays
    }

    private fun _getMouseListener(): MouseListener
    {
        return object : MouseAdapter()
        {
            override fun mouseReleased(e: MouseEvent)
            {
                val pixel = Pixel.fromPoint(e.getPoint())
                val coordinates = pixelToCoordinates(pixel)
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

        private fun _getTargetedCoordinates(units: Collection<Unit>): Collection<Coordinates>
        {
            val coordinates = mutableListOf<Coordinates>()
            for (unit in units)
            {
                when (val command = unit.getCommand())
                {
                    is MoveCommand -> coordinates += command.target
                }
            }
            return coordinates
        }
    }

}