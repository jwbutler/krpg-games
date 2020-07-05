package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.GameWindow

class GameEngine(private val state: GameState, private val window: GameWindow)
{
    fun doLoop()
    {
        val entities = state.getEntities()
        entities.forEach(Entity::update)
        entities.forEach { entity ->
            val (image, pixel) = entity.render()
            window.render(image, pixel)
        }
        window.redraw()
        state.ticks++
    }
}