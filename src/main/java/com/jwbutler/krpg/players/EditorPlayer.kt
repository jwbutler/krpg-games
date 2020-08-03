package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.TileOverlay
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.geometry.pixelToCoordinates
import com.jwbutler.krpg.graphics.Renderable
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.lang.IllegalStateException

/**
 * Mostly stubbed out, for use in the editor.  Might contain overlays and stuff.
 */
class EditorPlayer : HumanPlayer()
{
    override fun getQueuedCommand(unit: Unit): Command? = null
    override fun getTileOverlays() = mutableMapOf<Coordinates, TileOverlay>()
    override fun getUIOverlays() = listOf<Renderable>()
    override fun chooseCommand(unit: Unit) = throw IllegalStateException()

    override fun getMouseListener(): MouseAdapter
    {
        return object : MouseAdapter()
        {
            override fun mouseReleased(e: MouseEvent)
            {
                val state = GameState.getInstance()

                val pixel = Pixel.fromPoint(e.getPoint())
                val coordinates = pixelToCoordinates(pixel)

                if (coordinates.x >= 0 && coordinates.y >= 0)
                {
                    state.setTile(coordinates, Tile(coordinates))
                }
            }
        }
    }
}