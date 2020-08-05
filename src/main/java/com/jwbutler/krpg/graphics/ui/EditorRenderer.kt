package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.players.HumanPlayer

class EditorRenderer(window: GameWindow) : AbstractRenderer(window)
{
    private val tileRenderer = EditorTileRenderer()
    /**
     * Very similar to [GameRenderer._getRenderables]
     * but without the HUD
     */
    override fun _getRenderables(): List<Renderable>
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        val players = state.getPlayers()
        val tileOverlays = players.filterIsInstance<HumanPlayer>()
            .flatMap { it.getTileOverlays().values }
        val uiOverlays = players.filterIsInstance<HumanPlayer>()
            .flatMap { it.getUIOverlays() }

        val renderables = entities.plus(tileOverlays).map { it to it.render() }

        return renderables
            .sortedBy { it.second.layer }
            .sortedBy { it.first.getCoordinates().y }
            .map { it.second }
            .plus(uiOverlays) // TODO: Get these into the sort somehow
            .plus(_renderSelector())
    }

    private fun _renderSelector(): Renderable
    {
        val image = tileRenderer.render()
        return Renderable(
            image,
            Pixel(0, 0),
            RenderLayer.UI_OVERLAY
        )
    }
}