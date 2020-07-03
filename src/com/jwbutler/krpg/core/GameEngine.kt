package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity

class GameEngine
{
    fun doLoop()
    {
        val state: GameState = GameState.getInstance()
        val entities = state.getEntities()
        entities.forEach(Entity::update)
        entities.forEach(Entity::render)
        state.ticks++
    }
}