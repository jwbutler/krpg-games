package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.Renderable

/**
 * This class is responsible for executing the main loop
 */
class GameEngine(private val state: GameState, private val window: GameWindow)
{
    fun doLoop()
    {
        // Update
        val entities = state.getEntities()
        entities.forEach(Entity::update)

        // Render
        _render(entities)

        // Increment ticks
        state.ticks++
        state.getEntities().forEach(Entity::afterRender)
    }

    private fun _render(entities: Collection<Entity>)
    {
        window.clearBuffer()

        val rendered: List<Pair<Entity, Renderable>> = entities.map { it to it.render() }
            .sortedBy { it.second.layer }
            .sortedBy { it.first.getCoordinates().x + it.first.getCoordinates().y }

        rendered.forEach { (_, renderable) ->
            val (image, pixel) = renderable
            window.render(image, pixel)
        }

        window.redraw()
    }
}