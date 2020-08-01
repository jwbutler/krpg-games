package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.GameRenderer

/**
 * This class is responsible for executing the main loop,
 * including rendering
 */
class GameEngine(private val state: GameState, private val renderer: GameRenderer)
{
    fun doLoop()
    {
        // Update
        val entities = state.getEntities()
        for (entity in entities)
        {
            // Unfortunately we have to do this superfluous-looking check here
            // because they could die (or kill each other) during update() methods
            if (entity.exists())
            {
                entity.update()
            }
        }

        // Render
        renderer.render()

        // Increment ticks
        state.ticks++
        state.getEntities().forEach(Entity::afterRender)
    }
}