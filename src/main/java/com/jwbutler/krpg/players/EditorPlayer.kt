package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.entities.TileOverlay
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.Renderable

class EditorPlayer : HumanPlayer()
{
    override fun getQueuedCommand(unit: Unit) = null
    override fun getTileOverlays() = mutableMapOf<Coordinates, TileOverlay>()
    override fun getUIOverlays() = listOf<Renderable>()
    override fun chooseCommand(unit: Unit) = StayCommand(unit)
}