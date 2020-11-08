package com.jwbutler.krpg.core

import com.jwbutler.krpg.behavior.commands.BashCommand
import com.jwbutler.krpg.behavior.commands.DirectionalAttackCommand
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.RepeatingAttackCommand
import com.jwbutler.krpg.behavior.commands.SingleAttackCommand
import com.jwbutler.krpg.entities.TileOverlayFactory
import com.jwbutler.krpg.graphics.ui.HUDRenderer
import com.jwbutler.krpg.graphics.ui.UIOverlayFactory
import com.jwbutler.krpg.players.MousePlayer
import com.jwbutler.krpg.utils.getEnemyUnits
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.geometry.Pixel
import com.jwbutler.rpglib.graphics.Renderable

class RPGGameView : GameView
{
    override val tileDimensions = Dimensions(24, 12)
    override val gameDimensions = Dimensions(320, 180)
    override val initialWindowDimensions = Dimensions(1280, 720)

    private var cameraCoordinates: Coordinates = Coordinates(0, 0)
    var selectionStart: Pixel? = null
    var selectionEnd: Pixel? = null

    private val hudRenderer = HUDRenderer(gameDimensions.width, gameDimensions.height)

    override fun getTileOverlays(): Map<Coordinates, TileOverlay>
    {
        // throws an exception if the player is not of the expected type
        val humanPlayer = GameState.getInstance().getHumanPlayer() as MousePlayer

        val overlays = mutableMapOf<Coordinates, TileOverlay>()

        val enemyUnits = getEnemyUnits()
        for (unit in enemyUnits)
        {
            overlays[unit.getCoordinates()] = TileOverlayFactory.enemyOverlay(unit.getCoordinates(), false)
        }

        val playerUnits = getPlayerUnits()
        for (unit in playerUnits)
        {
            val command = humanPlayer.getCommand(unit)

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

            val coordinates = unit.getCoordinates()
            val isSelected = humanPlayer.getSelectedUnits().contains(unit)
            overlays[coordinates] = TileOverlayFactory.playerOverlay(coordinates, isSelected)
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

    fun setCameraCoordinates(coordinates: Coordinates)
    {
        val gameState = GameState.getInstance()
        check(gameState.containsCoordinates(coordinates))

        cameraCoordinates = coordinates
    }

    override fun getRenderables(): List<Renderable>
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        val tileOverlays = getTileOverlays().values
        val uiOverlays = getUIOverlays()

        val renderedEntities = entities.plus(tileOverlays).map { it to it.render() }

        val sortedRenderedEntities = renderedEntities
            .sortedBy { (_, renderable) -> renderable.layer }
            .sortedBy { (entity, _) -> entity.getCoordinates().y }
            .map { (_, renderable) -> renderable }

        return sortedRenderedEntities
            .plus(uiOverlays)
            .plus(hudRenderer.render())
    }
}