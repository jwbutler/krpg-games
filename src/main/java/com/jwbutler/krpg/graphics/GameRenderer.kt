package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.players.HumanPlayer

class GameRenderer
{
    private val window = GameWindow.initialize()
    fun render()
    {
        window.clearBuffer()

        val renderables = _getRenderables()
        renderables.forEach { (_, renderable) ->
            val (image, pixel) = renderable
            window.render(image, pixel)
        }

        run {
            val (image, pixel) = HUDRenderer.render()
            window.render(image, pixel)
        }

        window.redraw()
    }

    private fun _getRenderables(): List<Pair<Entity, Renderable>>
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        val players = state.getPlayers()
        val overlays = players.filterIsInstance<HumanPlayer>()
            .flatMap { it.getOverlays().values }

        val renderables = entities.plus(overlays).map { it to it.render() }

        return renderables
            .sortedBy { it.second.layer }
            .sortedBy { it.first.getCoordinates().y }
    }

    companion object : SingletonHolder<GameRenderer>(::GameRenderer)
}