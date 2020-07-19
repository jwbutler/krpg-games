package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.GameWindow

class GameEngine(private val state: GameState, private val window: GameWindow)
{
    fun doLoop()
    {
        // Update
        val entities = state.getEntities()
        entities.forEach(Entity::update)

        // Render
        window.clearBuffer()
        entities.forEach { entity ->
            val (image, pixel) = entity.render()
            window.render(image, pixel)
        }
        window.redraw()

        // Increment ticks
        state.ticks++
        state.getEntities().forEach(Entity::afterRender)
    }
}