package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Entity

class GameRenderer(private val window: GameWindow)
{
    fun render()
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        window.clearBuffer()

        val rendered: List<Pair<Entity, Renderable>> = entities.map { it to it.render() }
            .sortedBy { it.second.layer }
            .sortedBy { it.first.getCoordinates().y }

        rendered.forEach { (_, renderable) ->
            val (image, pixel) = renderable
            window.render(image, pixel)
        }

        run {
            val (image, pixel) = HUDRenderer.render()
            window.render(image, pixel)
        }

        window.redraw()
    }
}