package com.jwbutler.krpg.core

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.krpg.entities.TileOverlayFactory
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.graphics.Renderable

class KeyboardGameView : GameView
{
    override val tileDimensions = Dimensions(24, 12)
    override val gameDimensions = Dimensions(320, 180)
    override val initialWindowDimensions = Dimensions(1280, 720)

    override fun getUIOverlays() = listOf<Renderable>()
    override fun getCameraCoordinates() = _getPlayerUnit().getCoordinates()

    private fun _getPlayerUnit(): Unit
    {
        val humanPlayer = GameState.getInstance().getHumanPlayer()
        return humanPlayer.getUnits().first()
    }

    override fun getTileOverlays(): Map<Coordinates, TileOverlay>
    {
        val humanPlayer = GameState.getInstance().getHumanPlayer()
        val overlays = mutableMapOf<Coordinates, TileOverlay>()
        val unit = _getPlayerUnit()

        overlays[unit.getCoordinates()] = TileOverlayFactory.playerOverlay(unit.getCoordinates(), true)
        val targetCoordinates = unit.getCoordinates() + unit.getDirection()
        GameState.getInstance()
            .getUnits()
            .filter { u -> u.getPlayer() != humanPlayer } // TODO better hostility check
            .forEach { enemyUnit ->
                val isTargeted = unit.getActivity() == RPGActivity.ATTACKING && enemyUnit.getCoordinates() == targetCoordinates
                overlays[enemyUnit.getCoordinates()] = TileOverlayFactory.enemyOverlay(enemyUnit.getCoordinates(), isTargeted)
            }

        return overlays
    }
}