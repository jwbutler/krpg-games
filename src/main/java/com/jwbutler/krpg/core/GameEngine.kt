package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.HUDRenderer
import com.jwbutler.krpg.graphics.Renderable

/**
 * This class is responsible for executing the main loop,
 * including rendering
 */
class GameEngine(private val state: GameState, private val window: GameWindow)
{
    fun doLoop()
    {
        // Update
        val entities = state.getEntities()
        entities.forEach(Entity::update)

        // Render
        _render()

        // Increment ticks
        state.ticks++
        state.getEntities().forEach(Entity::afterRender)
    }

    /**
     * TODO: This should probably be encapsulated in a GameRenderer.
     * But for now, it's only a couple of lines
     */
    private fun _render()
    {
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