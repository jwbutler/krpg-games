package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.GameWindow

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
        entities.forEach { entity ->
            val (image, pixel) = entity.render()
            window.render(image, pixel)
        }
        window.redraw()
    }
}