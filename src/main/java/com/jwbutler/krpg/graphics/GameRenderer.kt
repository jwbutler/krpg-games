package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.graphics.ui.HUDRenderer

class GameRenderer
{
    fun render()
    {
        val window = GameWindow.getInstance()
        window.clearBuffer()

        for ((image, pixel) in _getRenderables())
        {
            window.render(image, pixel)
        }

        val (image, pixel) = HUDRenderer.render()
        window.render(image, pixel)

        window.redraw()
    }

    private fun _getRenderables(): List<Renderable>
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        val humanPlayer = state.getHumanPlayer()
        val tileOverlays = humanPlayer.getTileOverlays().values
        val uiOverlays = humanPlayer.getUIOverlays()

        val renderables = entities.plus(tileOverlays).map { it to it.render() }

        return renderables
            .sortedBy { (_, renderable) -> renderable.layer }
            .sortedBy { (entity, _) -> entity.getCoordinates().y }
            .map { (_, renderable) -> renderable }
            .plus(uiOverlays) // TODO: Get these into the sort somehow
    }

    companion object : SingletonHolder<GameRenderer>(::GameRenderer)
}